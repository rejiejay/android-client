package cn.rejiejay.application.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class HomeFragmentAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;

    /**
     * 构造函数
     * 初始化 list 集合
     *
     * @param fm
     * @param list
     */
    public HomeFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list=list;
    }

    /**
     * 设置每一页的内容
     */
    @Override
    public Fragment getItem(int arg) {
        return list.get(arg);
    }

    /**
     * 设置有多少页
     */
    @Override
    public int getCount() {
        return list.size();
    }
}
