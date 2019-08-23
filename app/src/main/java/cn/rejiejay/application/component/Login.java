package cn.rejiejay.application.component;

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
import java.util.Objects;

import cn.rejiejay.application.utils.Consequent;
import cn.rejiejay.application.utils.DigitalSignature;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 登录 和 主动刷新 Token凭证
 * 此处不做UI处理
 * <p>
 * 刷新凭证 是需要 密码 和 账号（不需要正确的旧Token凭证，免去登录的操作
 */
public class Login extends HTTP {
    private ObservableEmitter<Consequent> emitter;
    private Handler handler;

    /**
     * RxAndroid 封装 请求
     */
    public Observable<Consequent> observable() {

        return Observable.create(
                new ObservableOnSubscribe<Consequent>() {
                    @Override
                    public void subscribe(ObservableEmitter<Consequent> thisEmitter) {
                        emitter = thisEmitter; // 核心

                        try {
                            rxSubscribe(); // 方法不宜嵌套过于深

                        } catch (Exception e) {
                            thisEmitter.onError(e); // In case there are network errors
                        }
                    }
                });
    }

    /**
     * 请求 线程 和 HTTP
     */
    private void rxSubscribe() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                rxHandle(msg); // 方法不宜嵌套过于深

                return false;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                rxThread(); // 方法不宜嵌套过于深
            }
        }).start();

    }

    /**
     * 新线程
     */
    private void rxThread() {
        Message msg = new Message();
        String refreshTokenBody = "{\"password\":\"1938167\"}";

        String signature;
        try {
            signature = DigitalSignature.EncryptSignature(refreshTokenBody, "rejiejay", "token"); // 因为这里只校验格式，不会校验token
        } catch (Exception e) {
            msg.what = 3;
            msg.obj = e;
            handler.sendMessage(msg);
            e.printStackTrace();
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

                msg.what = 1;
                msg.obj = responsebody;
                handler.sendMessage(msg);

            } else {
                msg.what = 2;
                emitter.onError(new Throwable(connection.getResponseMessage())); // In case there are network errors
            }
        } catch (Exception e) {
            msg.what = 3;
            msg.obj = e;
            handler.sendMessage(msg);
            e.printStackTrace();
        }
    }


    /**
     * 线程通讯
     */
    private void rxHandle(Message msg) {

        if (msg.what == 1) {
            String resultString = msg.obj.toString();

            // 判断JSON格式是否有误
            if (!isJSONValid(resultString)) {
                cancelProgressDialog();
                emitter.onError(new Throwable(resultString));
                return;
            }

            JSONObject resultJSON = JSON.parseObject(resultString);

            int code = resultJSON.getInteger("result");
            String mag = resultJSON.getString("message");

            Consequent consequent = new Consequent();

            if (code == 1) {
                emitter.onNext(consequent.setData(resultJSON.getJSONObject("data")).setSuccess());
                emitter.onComplete();

            } else {

                emitter.onNext(consequent.setMessage(mag));
                emitter.onComplete();
            }

        } else {

            emitter.onError((Throwable) msg.obj);
        }
    }
}
