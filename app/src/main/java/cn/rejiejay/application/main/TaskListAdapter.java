package cn.rejiejay.application.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import cn.rejiejay.application.utils.Consequent;
import io.reactivex.Observer;


public class TaskListAdapter extends BaseAdapter {
    private Context context;

    private List<TaskFragmentListDate> listData;

    TaskListAdapter(Context context, ListView listView, List<TaskFragmentListDate> listData, Observer<Consequent> observer) {
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

        return view;
    }
}

