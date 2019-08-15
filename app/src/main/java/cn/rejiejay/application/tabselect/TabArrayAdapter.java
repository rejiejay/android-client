package cn.rejiejay.application.tabselect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cn.rejiejay.application.R;

/**
 * 自定义Adapter，创建一个类继承BaseAdapter。
 * 因为BaseAdapter中有四个抽象的方法：
 * public int getCount()  // 用来返回数据的数量的。
 * public Object getItem(int position) // 该方法使用来获得每一条ListView中的Item的，这里我们返回position即可，position是指每条Item在ListView中的位置（0， 1， 2……）。
 * public long getItemId(int position)  // 　该方法是来获得ListView中每条Item的Id的，这里我们依然返回position即可。
 * public View getView(int position, View convertView，ViewGroup viewGroup) // 该方法是自定义Adapter最重要的方法，在这个方法中我们需要将数据一一对应的映射或者添加到我们自己定义的View中。然后返回view。
 * 因此在继承BaseAdapter类后必须实现这四个方法。
 */
public class TabArrayAdapter extends BaseAdapter {

    private List<Tab> mData; // 定义数据
    private LayoutInflater mInflater; // 定义Inflater,加载我们自定义的布局。

    // 构造函数，在Activity创建对象Adapter的时候将数据data和Inflater传入自定义的Adapter中进行处理。
    public TabArrayAdapter(LayoutInflater inflater, List<Tab> data) {
        mData = data;
        mInflater = inflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // 获得ListView中的view
        View viewTab = mInflater.inflate(R.layout.activity_tab_select_item, null);

        // 获得标签对象
        Tab myTab = mData.get(position);
        myTab.getImage();

        // 获得自定义布局中每一个控件的对象
        // ImageView imagePhoto = (ImageView) viewStudent.findViewById(R.id.image_photo);

        // 将数据添加到自定义的布局中。
        // imagePhoto.setText(myTab.getName());

        return viewTab;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

