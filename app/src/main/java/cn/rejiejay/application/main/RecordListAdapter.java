package cn.rejiejay.application.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import cn.rejiejay.application.R;

public class RecordListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<RecordFragmentListDate> listData;

    public RecordListAdapter(Context context, ListView listView, List listData) {
        this.context = context;
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);

        // 用于绑定点击事件
        // listView;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 加载布局为一个视图
        View recordlisttest = inflater.inflate(R.layout.main_record_list, null);

        return recordlisttest;
    }
}

