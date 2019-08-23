package cn.rejiejay.application.component;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import cn.rejiejay.application.utils.Consequent;
import cn.rejiejay.application.utils.InternalStorage;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RxGet extends HTTP {
    private Context mContext;
    private String url;
    private ObservableEmitter<Consequent> emitter;
    private Handler handler;

    /**
     * 构造函数 初始化 持久化全局变量
     */
    public RxGet(Context mContext, String url) {
        this.mContext = mContext;
        this.url = url;
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
     * 请求操作, 子线程 OkHttp
     */
    private void rxGetThread() {
        Message msg = new Message();

//        OkHttpClient client = new OkHttpClient(); // 创建OkHttpClient对象
//        Request request = new Request.Builder()
//                .url(getUrl(url))
//                .build();
//
//
//        try {
//            Response response = client.newCall(request).execute(); // 得到Response 对象
//
//            if (response.isSuccessful() && response.code() == 200) {
//                // 线程通讯 该Message发送给对应的Handler Handler.sendMessage(msg);
//
//                // msg.what = 1; // 标识
//                // msg.arg1 = 123; // 传入简单的数据
//                // msg.arg2 = 321; // 简单数据
//                // msg.obj = null; // Object类型任意数据
//                // msg.setData(null); // 写入和读取Bundle类型的数据
//
//                msg.what = 1;
//                msg.obj = Objects.requireNonNull(response.body()).string();
//                handler.sendMessage(msg);
//
//            } else {
//                msg.what = 2;
//                // cancelProgressDialog(); // 此处不能有UI操作
//                // showErrorModal(mContext, "服务器数据有误", response.message());
//                emitter.onError(new Throwable(response.message())); // In case there are network errors
//            }
//
//        } catch (Exception e) {
//            msg.what = 3;
//            msg.obj = e;
//            handler.sendMessage(msg);
//            e.printStackTrace();
//        }

        // 获取 token
        String tokene = InternalStorage.dataRead(mContext, "x-rejiejay-token");
        String tokenexpired = InternalStorage.dataRead(mContext, "x-rejiejay-token-expired");

//        if () {
//
//        }


        try {
            URL myUrl = new URL(getUrl(url));
            // 得到connection对象。
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//        connection.setRequestProperty("x-rejiejay-authorization", signature);


        } catch (Exception e) {
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
                emitter.onError((Throwable) msg.obj);
                break;

            /**
             * 3以上的都是请求出错
             */
            default:
                cancelProgressDialog();
                showErrorModal(mContext, "请求出错", msg.obj.toString());
                emitter.onError((Throwable) msg.obj);
        }
    }

    /**
     * 主动刷新token
     */
    private void tokenRefresh() {

        Observer<Consequent> observer = new Observer<Consequent>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Consequent value) {
                cancelProgressDialog();

                if (value.getResult() == 1) {
                    JSONObject data = value.getData();
                    data.getString("token");
                    data.getString("tokenexpired");

                    InternalStorage.dataWrite(mContext, "x-rejiejay-token", data.getString("token"));
                    InternalStorage.dataWrite(mContext, "x-rejiejay-token-expired", data.getString("tokenexpired"));

                    cancelProgressDialog();
                    rxGetSubscribe(); // 再执行一次请求

                } else {

                    Consequent consequent = new Consequent();
                    cancelProgressDialog();
                    showErrorModal(mContext, "主动刷新token失败", value.getMessage());
                    emitter.onNext(consequent.setMessage(value.getMessage()));
                    emitter.onComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                Consequent consequent = new Consequent();
                cancelProgressDialog();
                showErrorModal(mContext, "主动刷新token失败", e.toString());
                emitter.onNext(consequent.setMessage(e.toString()));
                emitter.onComplete();
            }

            @Override
            public void onComplete() {
            }
        };

        Login httpLogin = new Login();
        httpLogin.observable().subscribe(observer);
    }
}
