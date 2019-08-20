package cn.rejiejay.application.component;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import cn.rejiejay.application.BuildConfig;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HTTP {
    public static String getUrl(String url) {
        return BuildConfig.BASE_URL + url;
    }

    /**
     * 先弄一个简单的请求，其他的先不搞
     *
     * @param url
     */
    public static void get(final String url) {
        /**
         * 实例化Handler将自动与当前运行线程相关联,并且这个Handler将与当前运行的线程使用同一个消息队列，并且可以处理该队列中的消息、
         *
         * 加一个大括号表示重写里面的方法(右键生成即可重写
         */
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });

        /**
         * android 中开启子线程
         * 直接new 一个线程类，传入参数实现Runnable接口的对象（new Runnable）通过重写 run 实现
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 写子线程中的操作
                OkHttpClient client = new OkHttpClient(); // 创建OkHttpClient对象
                Request request = new Request.Builder()
                        .url(getUrl(url))
                        .build();

                try {
                    Response response = client.newCall(request).execute(); // 得到Response 对象

                    if (response.isSuccessful()) {
                        /**
                         * 线程通讯
                         */
                        Log.d("kwwl", "response.code()==" + response.code());
                        Log.d("kwwl", "response.message()==" + response.message());
                        Log.d("kwwl", "res==" + response.body().string());
                    }

                } catch (Exception e) {
                    Log.e("error", e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * RxAndroid 封装 get 请求
     * 文档: https://github.com/chuanzh/RxJava2.0
     *
     * @param url
     */
    public static Observable<String> rxGet(final String url) {
        return Observable.create(
                new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) {
                        try {
                            emitter.onNext("hello");
                            emitter.onNext("word");
                            emitter.onComplete();

                        } catch (Exception e) {
                            emitter.onError(e); // In case there are network errors
                        }
                    }
                });
    }
}
