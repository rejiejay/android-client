package cn.rejiejay.application.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class InternalStorage {
    /**
     * 数据写入
     */
    public static void dataWrite(Context mContext, String key, String value) {
        try {
            FileOutputStream fos = mContext.openFileOutput(key, Context.MODE_PRIVATE);
            fos.write(key.getBytes());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * 数据读取
     */
    public static String dataRead(Context mContext, String key) {
        StringBuilder sb = new StringBuilder();

        try {
            FileInputStream fis = mContext.openFileInput(key);
            byte[] buff = new byte[1024];
            int hasRead = 0;

            //读取文件内容,hasRead表示已经读取的
            while ((hasRead = fis.read(buff)) > 0) {
                sb.append(new String(buff, 0, hasRead));
            }

            fis.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

        return sb.toString();
    }
}
