package cn.rejiejay.application.component;

import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 登录 和 主动刷新 Token凭证
 * <p>
 * 刷新凭证 是需要 密码 和 账号（不需要正确的旧Token凭证，免去登录的操作
 */
public class Login extends HTTP {
    // 只需要返回Boolean即可 因为不需要封装
    private ObservableEmitter<Boolean> emitter;
    private Handler handler;

    /**
     * RxAndroid 封装 请求
     */
    public Observable<Boolean> observable() {

        return Observable.create(
                new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> thisEmitter) {
                        emitter = thisEmitter; // 核心

                        try {
                            rxSubscribe(); // 方法不宜嵌套过于深

                        } catch (Exception e) {
                            // 弹出报错框 代码执行报错 不需要Consequent封装
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

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("username", "1938167")
                .build();

        Request request = new Request.Builder()
                .url(getUrl("/login/refresh/rejiejay"))
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .post(formBody)
                .build();
    }

    /**
     * 线程通讯
     */
    private void rxHandle(Message msg) {
    }
}
