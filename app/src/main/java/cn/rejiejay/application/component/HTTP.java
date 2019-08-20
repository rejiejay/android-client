package cn.rejiejay.application.component;

import android.util.Log;

import cn.rejiejay.application.BuildConfig;
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

                try  {
                    Response response = client.newCall(request).execute(); // 得到Response 对象

                    if (response.isSuccessful()) {
                        Log.d("kwwl","response.code()=="+response.code());
                        Log.d("kwwl","response.message()=="+response.message());
                        Log.d("kwwl","res=="+response.body().string());
                    }

                } catch (Exception e) {
                    Log.e("error", e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
