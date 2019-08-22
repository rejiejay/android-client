package cn.rejiejay.application.component;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 登录 和 主动刷新 Token凭证
 * <p>
 * 刷新凭证 是需要 密码 和 账号（不需要正确的旧Token凭证，免去登录的操作
 */
public class Login extends HTTP {
    // 只需要返回Boolean即可 因为不需要封装
    private ObservableEmitter<Boolean> emitter;

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

                        } catch (Exception e) {
                            // 弹出报错框 代码执行报错 不需要Consequent封装
                            thisEmitter.onError(e); // In case there are network errors
                        }
                    }
                });
    }
}
