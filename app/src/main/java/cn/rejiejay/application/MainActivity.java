package cn.rejiejay.application;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import cn.rejiejay.application.main.ActivityTabButton;

/**
 * 这个“不”是启动类；
 * 这是一个 Activity（核心概念）类似于一个Vue的页面，所以具有生命周期
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 顶部按钮
     */
    public ActivityTabButton homeTab;
    public ActivityTabButton recordTab;

    /**
     * ViewPager 分页组件
     */
    public ViewPager myViewPager;


    // 生命周期的 onCreate 周期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 可以暂时理解为入口，右键可以点进去（大概作用是与自己写的XML文件互相匹配(绑定

        initPageComponent(); // 初始化 绑定 组件的方法
        initViewPager(); // 初始化 ViewPager 分页组件
    }

    /**
     * 初始化 绑定 组件的方法
     */
    public void initPageComponent() {
        homeTab = findViewById(R.id.ActivityTabHome);
        recordTab = findViewById(R.id.ActivityTabRecord);

        homeTab.setTextContentAndAlpha(true, "首页");
        recordTab.setTextContentAndAlpha(false, "记录");

        myViewPager = findViewById(R.id.MyViewPager);
    }

    /**
     * 初始化 ViewPager 分页组件
     */
    public void initViewPager() {
        myViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            /**
             * 设置 Fragment 页面
             */
            @Override
            public Fragment getItem(int i) {
                return null;
            }

            /**
             * 设置 ViewPager 多少页数量
             */
            @Override
            public int getCount() {
                return 2;
            }
        });
    }
}
