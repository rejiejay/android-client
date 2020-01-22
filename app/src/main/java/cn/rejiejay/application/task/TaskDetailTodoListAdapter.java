package cn.rejiejay.application.task;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import cn.rejiejay.application.R;
import cn.rejiejay.application.utils.Consequent;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;


public class TaskDetailTodoListAdapter extends BaseAdapter {
    private Context context;

    // RxAndroid 进行页面通信
    private Observer<Consequent> observer;
    private LayoutInflater inflater;

    private List<TaskDetailTodoListData> listData;

    public TaskDetailTodoListAdapter(Context context, ListView listView, List<TaskDetailTodoListData> listData, Observer<Consequent> observer) {
        this.context = context;
        this.observer = observer;
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
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

    /**
     * 获取页面
     * 作用: 通过Override覆盖, 实现自定义组件
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TaskDetailTodoListData item = listData.get(i); // 数据

        /**
         * 目的: 缓存机制
         * 为什么: 解决 点击加载更多 出现的预期外的问题
         */
        ViewHolder holder;
        if (view == null) { // 目的: 第一次不缓存
            view = inflater.inflate(R.layout.task_detail_todo_list, null); // 初始化这个 View

            holder = new ViewHolder(); // 初始化

            holder.observer = observer; // 目的: 防御性代码; 避免报错

            view.setTag(holder); // 目的: 持久化;利用Tag机制
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }


    class ViewHolder {
        Observer<Consequent> observer;
    }
}

