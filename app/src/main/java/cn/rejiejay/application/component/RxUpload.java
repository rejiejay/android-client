package cn.rejiejay.application.component;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import cn.rejiejay.application.utils.Consequent;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Post请求
 */
public class RxUpload extends HTTP {
    private Context mContext;
    private String url;
    private String body;
    private ObservableEmitter<Consequent> emitter;
    private Handler handler;

    /**
     * 构造函数 初始化 持久化全局变量
     */
    public RxUpload(Context mContext, String url, String body) {
        this.mContext = mContext;
        this.url = url;
        this.body = body;
    }

    /**
     * RxAndroid 封装 post 请求
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
                            rxPostSubscribe(); // 方法不宜嵌套过于深

                        } catch (Exception e) {
                            // 弹出报错框 代码执行报错 不需要Consequent封装
                            cancelProgressDialog();
                            showErrorModal(mContext, "请求出错,RxjAVA数据流", e.toString());
                            thisEmitter.onError(e); // In case there are network errors
                        }
                    }
                }
        );
    }

    /**
     * 继续封装 post 请求
     */
    private void rxPostSubscribe() {
        // 线程通讯
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                rxPostHandle(msg); // 方法不宜嵌套过于深
                return false;
            }
        });

        // 开启子线程准备请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                rxPostThread(); // 方法不宜嵌套过于深
            }
        }).start();

    }

    /**
     * 请求部分
     */
    private void rxPostThread() {
        Message msg = new Message(); // 线程通讯

        try {
            URL myUrl = new URL(getUrl(url));
            // 得到connection对象。
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            // 设置请求方式
            connection.setRequestMethod("POST");
            connection.setDoOutput(true); // 允许写出
            connection.setDoInput(true); // 允许读入
            connection.setUseCaches(false); // 不使用缓存
            connection.setConnectTimeout(600000);
            connection.setReadTimeout(600000);
            connection.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
            connection.setRequestProperty("x-rejiejay-authorization", "1938167");

            // 连接
            connection.connect();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(body);
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

                msg.what = 1;
                msg.obj = responsebody;
                handler.sendMessage(msg);

                inputStream.close();

            } else {
                msg.what = 2;
                msg.obj = connection.getResponseMessage();
                handler.sendMessage(msg);
            }

            connection.disconnect();

        } catch (Exception e) {
            msg.what = 3;
            msg.obj = e;
            handler.sendMessage(msg);
            e.printStackTrace();
        }
    }

    /**
     * 处理 请求（线程通讯 返回的数据
     */
    private void rxPostHandle(Message msg) {

        switch (msg.what) {
            case 1:
                String resultString = msg.obj.toString();
                Log.d("rxPostHandle", resultString);
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
                if (code == 1) {
                    cancelProgressDialog();
                    emitter.onNext(consequent.setSuccess(resultJSON.getJSONObject("data")));
                    emitter.onComplete();
                } else {
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

            /*
             * 3以上的都是请求出错
             */
            default:
                cancelProgressDialog();
                showErrorModal(mContext, "请求出错", msg.obj.toString());
                emitter.onError(new Throwable(msg.obj.toString()));
        }
    }
}
