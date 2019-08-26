package cn.rejiejay.application.selecttab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.util.List;

import cn.rejiejay.application.R;
import cn.rejiejay.application.utils.Consequent;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;

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

    // RxAndroid 进行页面通信
    private Observer<Consequent> observer;

    private List<Tab> mData; // 定义数据
    private LayoutInflater mInflater; // 定义Inflater,加载我们自定义的布局。

    // 构造函数 （在Activity创建对象Adapter的时候将数据data和Inflater传入自定义的Adapter中进行处理。
    public TabArrayAdapter(LayoutInflater inflater, List<Tab> data, Observer<Consequent> observer) {
        mData = data;
        mInflater = inflater;
        this.observer = observer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // 获得ListView中的view
        View viewTab = mInflater.inflate(R.layout.selecttab_item, null);

        initPageDate(position, viewTab); // 初始化页面

        initConfirmSubmit(position, viewTab); // 初始点击

        return viewTab;
    }

    // 初始化页面
    private void initPageDate(int position, View viewTab) {
        // 获得标签对象
        Tab myTab = mData.get(position);

        // 获得自定义布局中每一个控件的对象
        QMUISpanTouchFixTextView tabName = viewTab.findViewById(R.id.tab_item_name);
        ImageView tabAddImageView = viewTab.findViewById(R.id.tab_add_icon);
        ImageView tabDelImageView = viewTab.findViewById(R.id.tab_del_icon);

        // 将数据添加到自定义的布局中。
        tabName.setText(myTab.getName());

        // 删除功能(废弃
        if (myTab.getDelete()) {
            tabAddImageView.setVisibility(View.INVISIBLE);
            tabDelImageView.setVisibility(View.VISIBLE);
        } else {
            tabAddImageView.setVisibility(View.VISIBLE);
            tabDelImageView.setVisibility(View.INVISIBLE);
        }

    }

    // 初始点击
    private void initConfirmSubmit(int position, View viewTab) {
        final Tab myTab = mData.get(position);

        RelativeLayout mainBtn = viewTab.findViewById(R.id.tab_item_main);

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {

                Observable.create(new ObservableOnSubscribe<Consequent>() {
                    @Override
                    public void subscribe(ObservableEmitter<Consequent> thisEmitter) {
                        Consequent consequent = new Consequent();
                        JSONObject selectedData = new JSONObject();
                        selectedData.put("id", myTab.getId());
                        selectedData.put("name", myTab.getName());
                        consequent.setData(selectedData).setResult(1930);
                        thisEmitter.onNext(consequent);
                        thisEmitter.onComplete();
                    }
                }).subscribe(observer);

            }
        });
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

