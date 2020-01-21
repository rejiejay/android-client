package cn.rejiejay.application.main;

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


public class TaskListAdapter extends BaseAdapter {
    private Context context;

    // RxAndroid 进行页面通信
    private Observer<Consequent> observer;
    private LayoutInflater inflater;

    private List<TaskFragmentListDate> listData;

    TaskListAdapter(Context context, ListView listView, List<TaskFragmentListDate> listData, Observer<Consequent> observer) {
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
        TaskFragmentListDate item = listData.get(i); // 数据

        /**
         * 目的: 缓存机制
         * 为什么: 解决 点击加载更多 出现的预期外的问题
         */
        ViewHolder holder;
        if (view == null) { // 目的: 第一次不缓存
            view = inflater.inflate(R.layout.main_task_list, null); // 初始化这个 View

            holder = new ViewHolder(); // 初始化

            holder.observer = observer; // 目的: 防御性代码; 避免报错

            holder.viewModel = view.findViewById(R.id.task_list_view);

            view.setTag(holder); // 目的: 持久化;利用Tag机制
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // 页面跳转
        initLoadDetail(i, holder);

        return view;
    }


    class ViewHolder {
        Observer<Consequent> observer;

        LinearLayout viewModel;
    }

    /**
     * 页面跳转 任务详情
     */
    private void initLoadDetail(int i, final ViewHolder holder) {
        final TaskFragmentListDate item = listData.get(i);

        holder.viewModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                /**
                 * 为什么: Adapter里无法跳转
                 */
                Log.d("跳转", "JumpToTaskDetail");
                final Consequent consequent = new Consequent();

                consequent.setData(JSON.parseObject(JSON.toJSONString(item))).setResult(1930);

                Observable.create(new ObservableOnSubscribe<Consequent>() {
                    @Override
                    public void subscribe(ObservableEmitter<Consequent> thisEmitter) {
                        thisEmitter.onNext(consequent);
                        thisEmitter.onComplete();
                    }
                }).subscribe(holder.observer);
            }
        });
    }
}

