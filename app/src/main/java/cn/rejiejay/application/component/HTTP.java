package cn.rejiejay.application.component;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import cn.rejiejay.application.BuildConfig;

/**
 * 根据 cn.rejiejay.application.component.RxGet 抽象出来的方法就放在这里
 */
class HTTP {
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
    void buildProgressDialog(Context mContext, String message) {
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
    void cancelProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
    }

    /**
     * 判断JSON是否合法
     */
    boolean isJSONValid(String test) {
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

    /**
     * 弹出 报错模态框
     */
    void showErrorModal(Context mContext, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);

        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true);
        builder.setCancelable(true);    // 设置按钮是否可以按返回键取消,false则不可以取消

        //设置正面按钮
        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
