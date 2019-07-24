package cn.rejiejay.application;

import android.app.Activity;
import android.os.Bundle;

/**
 * 这个“不”是启动类；
 * 这是一个 Activity（核心概念）类似于一个Vue的页面，所以具有生命周期
 */
public class MainActivity extends Activity {

    // 生命周期的 onCreate 周期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 可以暂时理解为入口，右键可以点进去
    }
}
