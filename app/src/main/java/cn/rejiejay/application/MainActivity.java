package cn.rejiejay.application;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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
    public ArrayList<ActivityTabButton> tabBtnList;

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
        tabBtnList = new ArrayList<>();
        ActivityTabButton activityTabHome = findViewById(R.id.ActivityTabHome);
        ActivityTabButton activityTabRecord = findViewById(R.id.ActivityTabRecord);

        tabBtnList.add(activityTabHome);
        tabBtnList.add(activityTabRecord);

        activityTabHome.setTextContentAndAlpha(true, "首页");
        activityTabRecord.setTextContentAndAlpha(false, "记录");

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
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment()); // 添加Fragment到集合里设置setAdapter适配器
        fragmentList.add(new RecordFragment());

        PagerAdapter myPagerAdapter = new MainFragmentAdapter(getSupportFragmentManager(), fragmentList);

        /**
         * 为 ViewPager 设置 Adapter 适配器
         */
        myViewPager.setAdapter(myPagerAdapter);

        /**
         * 监听页面改变的事件
         */
        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            /**
             * 设置导航按钮显示和激活
             *
             * @param position 当前选中的视图
             */
            @Override
            public void onPageSelected(int position) {
                /**
                 * 先执行一遍清空操作
                 */
                for (int i = 0; i < tabBtnList.size(); i++) {
                    tabBtnList.get(i).setSelected(false);
                }

                /**
                 * 再根据选中视图的下标设置激活
                 */
                tabBtnList.get(position).setSelected(true);

                // 切换到当前页面，重置高度
                myViewPager.requestLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        /**
         * 绑定 Tab 的点击事件
         */
        for (int i = 0; i < tabBtnList.size(); i++) {
            ActivityTabButton thisTabBtnList = tabBtnList.get(i);

            thisTabBtnList.setTag(i); // 保存当前的索引值

            thisTabBtnList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisView) {
                    // 根据上面的索引值 修改页面
                    myViewPager.setCurrentItem((Integer) thisView.getTag());
                }
            });
        }
    }


}
