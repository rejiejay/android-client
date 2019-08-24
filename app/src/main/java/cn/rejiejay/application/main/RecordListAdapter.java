package cn.rejiejay.application.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
        RecordFragmentListDate item = listData.get(i);

        // ViewHolder模式 是一种缓存机制
        ViewHolder holder;
        if (view == null) {
            if (item.getType().equals("record")) {
                view = inflater.inflate(R.layout.main_record_list_record, null);
            } else {
                view = inflater.inflate(R.layout.main_record_list_event, null);

            }

            holder = new ViewHolder();

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }

    class ViewHolder {
    }
}

