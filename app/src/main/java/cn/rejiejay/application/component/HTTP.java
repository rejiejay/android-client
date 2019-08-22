package cn.rejiejay.application.component;

import android.app.ProgressDialog;
import android.content.Context;

import cn.rejiejay.application.BuildConfig;

/**
 * 根据 cn.rejiejay.application.component.RxGet 抽象出来的方法就放在这里
 */
public class HTTP {
    private ProgressDialog progressDialog;

    /**
     * 获取Url链接
     */
    String getUrl(String url) {
        return BuildConfig.BASE_URL + url;
    }

    /**
     * 显示加载框
     */
    public void buildProgressDialog(Context mContext, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * 关闭加载框
     */
    public void cancelProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
    }
}
