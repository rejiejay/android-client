package cn.rejiejay.application.component;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import cn.rejiejay.application.BuildConfig;
import cn.rejiejay.application.utils.Consequent;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 根据 cn.rejiejay.application.component.RxGet 抽象出来的方法就放在这里
 */
public class HTTP {
    @NotNull
    @Contract(pure = true)
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
                        Log.d("kwwl", "res==" + Objects.requireNonNull(response.body()).string());
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
    public static Observable<Consequent> rxGet(final String url) {

        /**
         * 创建 返回 观察者
         */
        return Observable.create(
                new ObservableOnSubscribe<Consequent>() {
                    @Override
                    public void subscribe(ObservableEmitter<Consequent> emitter) {
                        try {
                            rxGetSubscribe(url, emitter);
                        } catch (Exception e) {
                            /**
                             * 弹出报错框 代码执行报错 不需要Consequent封装
                             */
                            showErrorModal("请求出错,RxjAVA数据流", e.toString());
                            emitter.onError(e); // In case there are network errors
                        }
                    }
                });
    }

    /**
     * RxAndroid 封装 get 请求 subscribe 方法（方法不宜嵌套过于深
     */
    public static void rxGetSubscribe(final String url, final ObservableEmitter<Consequent> emitter) throws Exception {
        /**
         * 实例化Handler将自动与当前运行线程相关联,并且这个Handler将与当前运行的线程使用同一个消息队列，并且可以处理该队列中的消息、
         *
         * 加一个大括号表示重写里面的方法(右键生成即可重写
         */
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                /**
                 * what 1 标识成功 2 表示请求成功但是服务器数据有误 3表示请求失败
                 */
                switch (msg.what) {
                    case 1:
                        String resultString = msg.obj.toString();
                        Log.d("resultString", resultString);

                        /**
                         * 判断JSON格式是否有误
                         */
                        if (!isJSONValid(resultString)) {
                            showErrorModal("服务器数据有误", resultString);
                            emitter.onError(new Throwable(resultString));
                            break;
                        }

                        JSONObject resultJSON = JSON.parseObject(resultString);

                        int result = resultJSON.getInteger("result");
                        String mag = resultJSON.getString("message");

                        Consequent consequent = new Consequent();
                        switch (result) {
                            case 1:
                                emitter.onNext(consequent.setData(resultJSON.getJSONObject("data")).setSuccess());
                                emitter.onComplete();
                                break;

                            /**
                             * 表示 需要前端主动刷新token（暂时不写
                             */
                            case 40004:

                                break;
                            default:
                                showErrorModal("提示", mag);
                                emitter.onNext(consequent.setMessage(mag));
                                emitter.onComplete();
                        }

                    case 2:
                        showErrorModal("服务器数据有误", msg.obj.toString());
                        emitter.onError((Throwable) msg.obj);
                        break;

                    /**
                     * 3以上的都是轻轻出错
                     */
                    default:
                        showErrorModal("请求出错", msg.obj.toString());
                        emitter.onError((Throwable) msg.obj);
                }

                return false;
            }
        });

        /**
         * android 中开启子线程
         *
         * 注意：这里是子线程，所以不要做UI相关的操作
         *
         * 直接new 一个线程类，传入参数实现Runnable接口的对象（new Runnable）通过重写 run 实现
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();

                // 写子线程中的操作
                OkHttpClient client = new OkHttpClient(); // 创建OkHttpClient对象
                Request request = new Request.Builder()
                        .url(getUrl(url))
                        .build();

                try {
                    Response response = client.newCall(request).execute(); // 得到Response 对象

                    if (response.isSuccessful() && response.code() == 200) {
                        /**
                         * 线程通讯 该Message发送给对应的Handler Handler.sendMessage(msg);
                         */
                        // msg.what = 1; // 标识
                        // msg.arg1 = 123; // 传入简单的数据
                        // msg.arg2 = 321; // 简单数据
                        // msg.obj = null; // Object类型任意数据
                        // msg.setData(null); // 写入和读取Bundle类型的数据

                        String body = response.body().string();
                        msg.what = 1;
                        msg.obj = body;
                        Log.d("responsebodytoString", response.body().toString());
                        handler.sendMessage(msg);

                    } else {
                        msg.what = 2;
                        showErrorModal("服务器数据有误", response.message());
                        emitter.onError(new Throwable(response.message())); // In case there are network errors
                    }

                } catch (Exception e) {
                    msg.what = 3;
                    msg.obj = e;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 弹出 报错模态框
     *
     * @param message
     */
    public static void showErrorModal(String title, String message) {

    }

    /**
     * 判断JSON是否合法
     */
    public static boolean isJSONValid(String test) {
        try {
            JSONObject.parseObject(test);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
