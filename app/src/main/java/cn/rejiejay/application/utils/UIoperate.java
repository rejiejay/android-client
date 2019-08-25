package cn.rejiejay.application.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * UI操作封装
 */
public class UIoperate {
    /**
     * 弹出 报错模态框
     */
    public static void showErrorModal(Context mContext, String title, String message) {
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
