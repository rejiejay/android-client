package cn.rejiejay.application.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

import cn.rejiejay.application.utils.Consequent;
import cn.rejiejay.application.utils.DigitalSignature;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class RxGet extends HTTP {
    private Context mContext;
    private String url;
    private String parameter;
    private ObservableEmitter<Consequent> emitter;
    private Handler handler;

    /**
     * 构造函数 初始化 持久化全局变量
     */
    public RxGet(Context mContext, String url, String parameter) {
        this.mContext = mContext;
        this.url = url;
        this.parameter = parameter;
    }

    /**
     * RxAndroid 封装 get 请求
     * 文档: https://github.com/chuanzh/RxJava2.0
     */
    public Observable<Consequent> observable() {
        buildProgressDialog(mContext, "正在加载...");

        return Observable.create(
                new ObservableOnSubscribe<Consequent>() {
                    @Override
                    public void subscribe(ObservableEmitter<Consequent> thisEmitter) {
                        emitter = thisEmitter; // 核心

                        try {
                            rxGetSubscribe(); // 方法不宜嵌套过于深

                        } catch (Exception e) {
                            // 弹出报错框 代码执行报错 不需要Consequent封装
                            cancelProgressDialog();
                            showErrorModal(mContext, "请求出错,RxjAVA数据流", e.toString());
                            thisEmitter.onError(e); // In case there are network errors
                        }
                    }
                });
    }

    /**
     * 封装 get 请求
     * RxAndroid subscribe 方法
     */
    private void rxGetSubscribe() {
        handler = new Handler(new Handler.Callback() {
            /**
             * 实例化Handler将自动与当前运行线程相关联,并且这个Handler将与当前运行的线程使用同一个消息队列，并且可以处理该队列中的消息、
             *
             * 加一个大括号表示重写里面的方法(右键生成即可重写
             */
            @Override
            public boolean handleMessage(Message msg) {
                rxGetHandle(msg); // 方法不宜嵌套过于深
                return false;
            }
        });

        new Thread(new Runnable() {
            /**
             * android 中开启子线程
             *
             * 注意：这里是子线程，所以不要做UI相关的操作
             *
             * 直接new 一个线程类，传入参数实现Runnable接口的对象（new Runnable）通过重写 run 实现
             */
            @Override
            public void run() {
                rxGetThread(); // 方法不宜嵌套过于深
            }
        }).start();
    }

    /**
     * 子线程 的 请求操作 
     */
    private void rxGetThread() {
        Message msg = new Message();

        // 获取 token
        SharedPreferences tokenSharedPreferences = mContext.getSharedPreferences("token", mContext.MODE_PRIVATE);

        String token = tokenSharedPreferences.getString("x-rejiejay-token", null);
        String tokenExpiredStr = tokenSharedPreferences.getString("x-rejiejay-token-expired", null);

        // 只要有一个是null 就重新获取
        if (token == null || tokenExpiredStr == null) {
            // 获取token
            tokenRefresh();
            return;
        }

        // 判断日期
        if (new Date().getTime() > Long.parseLong(tokenExpiredStr)) {
            // 获取token
            tokenRefresh();
            return;
        }

        // 加密签名
        String signature;
        try {
            signature = DigitalSignature.EncryptSignature(parameter, "rejiejay", token);
        } catch (Exception e) {
            msg.what = 3;
            msg.obj = e;
            handler.sendMessage(msg);
            e.printStackTrace();
            return;
        }

        try {
            URL myUrl = new URL(getUrl(url + parameter));
            // 得到connection对象。
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false); // 禁止 URL 连接进行输出，默认为“false”
            connection.setDoInput(true); // 使用 URL 连接进行输入，默认为“true”
            connection.setUseCaches(false); // 忽略缓存
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("x-rejiejay-authorization", signature);

            // 连接
            connection.connect();

            // 得到响应码
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                //得到响应流
                InputStream inputStream = connection.getInputStream();
                //将响应流转换成字符串
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String reponse = sb.toString();
                String responsebody = sb.toString();

                msg.what = 1;
                msg.obj = responsebody;
                handler.sendMessage(msg);

            } else {
                cancelProgressDialog();
                msg.what = 2;
                msg.obj = connection.getResponseMessage();
                handler.sendMessage(msg);
            }

        } catch (Exception e) {
            cancelProgressDialog();
            msg.what = 3;
            msg.obj = e;
            handler.sendMessage(msg);
            e.printStackTrace();
        }
    }

    /**
     * 处理请求
     * <p>
     * what 1 标识成功 2 表示请求成功但是服务器数据有误 3表示请求失败
     */
    private void rxGetHandle(Message msg) {

        switch (msg.what) {
            case 1:
                String resultString = msg.obj.toString();

                // 判断JSON格式是否有误
                if (!isJSONValid(resultString)) {
                    cancelProgressDialog();
                    showErrorModal(mContext, "服务器数据有误", resultString);
                    emitter.onError(new Throwable(resultString));
                    break;
                }

                JSONObject resultJSON = JSON.parseObject(resultString);

                int code = resultJSON.getInteger("result");
                String mag = resultJSON.getString("message");

                Consequent consequent = new Consequent();
                // 服务器返回的code
                switch (code) {
                    case 1:
                        cancelProgressDialog();
                        emitter.onNext(consequent.setData(resultJSON.getJSONObject("data")).setSuccess());
                        emitter.onComplete();
                        break;

                    // 主动刷新token
                    case 40004:
                        tokenRefresh();
                        break;

                    default:
                        cancelProgressDialog();
                        showErrorModal(mContext, "提示", mag);
                        emitter.onNext(consequent.setMessage(mag));
                        emitter.onComplete();
                }

                break;
            case 2:
                cancelProgressDialog();
                showErrorModal(mContext, "服务器数据有误", msg.obj.toString());
                emitter.onError(new Throwable(msg.obj.toString()));
                break;

            /**
             * 3以上的都是请求出错
             */
            default:
                cancelProgressDialog();
                showErrorModal(mContext, "请求出错", msg.obj.toString());
                emitter.onError(new Throwable(msg.obj.toString()));
        }
    }

    /**
     * 主动刷新token
     * 因为这个本身是在子线程，所以并不需要直接创建新的线程
     */
    private void tokenRefresh() {
        String refreshTokenBody = "{\"password\":\"1938167\"}";

        String signature;
        try {
            signature = DigitalSignature.EncryptSignature(refreshTokenBody, "rejiejay", "token"); // 因为这里只校验格式，不会校验token
        } catch (Exception e) {
            String message = e.toString();

            Consequent consequent = new Consequent();

            cancelProgressDialog();
            showErrorModal(mContext, "主动刷新token失败,原因创建签名出错", message);
            emitter.onNext(consequent.setMessage(message));
            emitter.onComplete();
            return;
        }


        try {
            URL url = new URL(getUrl("/login/refresh/rejiejay"));
            // 得到connection对象。
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方式
            connection.setRequestMethod("POST");
            connection.setDoOutput(true); // 允许写出
            connection.setDoInput(true); // 允许读入
            connection.setUseCaches(false); // 不使用缓存
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("x-rejiejay-authorization", signature);

            // 连接
            connection.connect();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(refreshTokenBody);
            writer.close();

            // 得到响应码
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 得到响应流
                InputStream inputStream = connection.getInputStream();

                // 将响应流转换成字符串
                byte[] data = new byte[1024];
                StringBuffer sb = new StringBuffer();

                int length = 0;
                while ((length = inputStream.read(data)) != -1) {
                    String s = new String(data, Charset.forName("utf-8"));
                    sb.append(s);
                }

                String responsebody = sb.toString();

                // 判断JSON格式是否有误
                if (!isJSONValid(responsebody)) {
                    cancelProgressDialog();
                    showErrorModal(mContext, "服务器数据错误", "JSON格式有误");
                    emitter.onError(new Throwable(responsebody));
                    return;
                }

                JSONObject resultJSON = JSON.parseObject(responsebody);

                int code = resultJSON.getInteger("result");
                String mag = resultJSON.getString("message");

                if (code == 1) {
                    JSONObject dataJSON = resultJSON.getJSONObject("data");

                    SharedPreferences token = mContext.getSharedPreferences("token", mContext.MODE_PRIVATE);
                    SharedPreferences.Editor editor = token.edit(); // 获取Editor
                    editor.putString("x-rejiejay-token", dataJSON.getString("token"));
                    editor.putString("x-rejiejay-token-expired", dataJSON.getString("tokenexpired"));
                    editor.apply(); //提交修改

                    rxGetThread(); // 再执行一次请求

                } else {
                    cancelProgressDialog();

                    Consequent consequent = new Consequent();
                    showErrorModal(mContext, "主动刷新token失败", mag);
                    emitter.onNext(consequent.setMessage(mag));
                    emitter.onComplete();
                }


            } else {
                String message = connection.getResponseMessage();

                Consequent consequent = new Consequent();

                cancelProgressDialog();
                showErrorModal(mContext, "主动刷新token失败，数据有误", message);
                emitter.onNext(consequent.setMessage(message));
                emitter.onComplete();
            }

        } catch (Exception e) {
            String message = e.toString();

            Consequent consequent = new Consequent();

            cancelProgressDialog();
            showErrorModal(mContext, "主动刷新token失败, 原因创建签名出错", message);
            emitter.onNext(consequent.setMessage(message));
            emitter.onComplete();

            e.printStackTrace();
        }
    }
}
