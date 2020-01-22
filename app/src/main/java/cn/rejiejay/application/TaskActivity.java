package cn.rejiejay.application;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.rejiejay.application.component.AutoHeightListView;
import cn.rejiejay.application.task.TaskDetailTodoListAdapter;
import cn.rejiejay.application.task.TaskDetailTodoListData;
import cn.rejiejay.application.utils.Consequent;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class TaskActivity extends AppCompatActivity {
    private Context mContext;

    public List<TaskDetailTodoListData> listData = new ArrayList<>();

    public TaskDetailTodoListAdapter mAdapter;

    /**
     * 生命周期
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);

        mContext = this;

        // 初始化 绑定 组件的方法
        initPageComponent();

        // listView
        initListViewComponent();
    }

    /**
     * 初始化 绑定 组件的方法
     */
    public void initPageComponent() {

        // 假数据记得删除
        listData.add(new TaskDetailTodoListData());
        listData.add(new TaskDetailTodoListData());
    }

    /**
     * 初始化 listView
     */
    public void initListViewComponent() {
        AutoHeightListView listViewComponent = findViewById(R.id.task_list_view);
        
        /**
         * Observer
         * 作用: 注册 RxAndroid 进行页面通信
         */
        Observer<Consequent> observer = new Observer<Consequent>() {
            @Override
            public void onSubscribe(Disposable d) {/** 不需要处理 */}

            @Override
            public void onNext(Consequent value) {/** 不需要处理 */}

            @Override
            public void onError(Throwable e) {/** 暂不处理 */}

            @Override
            public void onComplete() {/** 不需要处理 */}
        };

        mAdapter = new TaskDetailTodoListAdapter(mContext, listViewComponent, listData, observer);
        listViewComponent.setAdapter(mAdapter);
    }
}
