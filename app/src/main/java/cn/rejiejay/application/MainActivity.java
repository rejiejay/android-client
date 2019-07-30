package cn.rejiejay.application;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import cn.rejiejay.application.main.ActivityTabButton;
import cn.rejiejay.application.main.HomeFragment;
import cn.rejiejay.application.main.MainFragmentAdapter;
import cn.rejiejay.application.main.RecordFragment;

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
        /**
         * PagerAdapter 如何使用?
         * PagerAdapter 是 android.support.v4包中的类，它的子类有FragmentPagerAdapter, FragmentStatePagerAdapter，这两个adapter都是Fragment的适配器，用于实现Fragment的滑动效果
         */
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(new HomeFragment()); // 添加Fragment到集合里设置setAdapter适配器
        list.add(new RecordFragment());

        PagerAdapter myPagerAdapter = new MainFragmentAdapter(getSupportFragmentManager(), list);

        /**
         * 为 ViewPager 设置 Adapter 适配器
         */
        myViewPager.setAdapter(myPagerAdapter);

        /**
         *
         */
        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                selectedTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
