package cn.rejiejay.application.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.util.ArrayList;
import java.util.List;

import cn.rejiejay.application.R;
import cn.rejiejay.application.component.AutoHeightListView;
import cn.rejiejay.application.task.TaskDetailTimelineAdapter;
import cn.rejiejay.application.task.TaskDetailTimelineData;
import cn.rejiejay.application.task.TaskDetailTodoListAdapter;
import cn.rejiejay.application.task.TaskDetailTodoListData;
import cn.rejiejay.application.utils.Consequent;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 任务列表
 */
public class TaskActivity extends AppCompatActivity {
    private Context mContext;

    /**
     * todo
     */
    public List<TaskDetailTodoListData> todoList = new ArrayList<>();
    public TaskDetailTodoListAdapter todoAdapter;

    /**
     * 已完成 时间轴
     */
    private List<TaskDetailTimelineData> timelineList = new ArrayList<>();
    public TaskDetailTimelineAdapter timelineAdapter;

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

        initTodoListViewComponent();

        // okListView
        initOkListViewComponent();

        jumpToDetailDescription();
    }

    /**
     * 初始化 绑定 组件的方法
     */
    public void initPageComponent() {

        // 假数据记得删除
        todoList.add(new TaskDetailTodoListData());
        todoList.add(new TaskDetailTodoListData());
        timelineList.add(new TaskDetailTimelineData("2016-05-25 17:48:00", "[沈阳市] [沈阳和平五部]的派件已签收 感谢使用中通快递,期待再次为您服务!"));
        timelineList.add(new TaskDetailTimelineData("2016-05-25 14:13:00", "[沈阳市] [沈阳和平五部]的东北大学代理点正在派件 电话:18040xxxxxx 请保持电话畅通、耐心等待"));
        timelineList.add(new TaskDetailTimelineData("2016-05-25 13:01:04", "[沈阳市] 快件到达 [沈阳和平五部]"));
        timelineList.add(new TaskDetailTimelineData("2016-05-25 12:19:47", "[沈阳市] 快件离开 [沈阳中转]已发往[沈阳和平五部]"));
        timelineList.add(new TaskDetailTimelineData("2016-05-25 11:12:44", "[沈阳市] 快件到达 [沈阳中转]"));
        timelineList.add(new TaskDetailTimelineData("2016-05-24 03:12:12", "[嘉兴市] 快件离开 [杭州中转部]已发往[沈阳中转]"));
        timelineList.add(new TaskDetailTimelineData("2016-05-23 21:06:46", "[杭州市] 快件到达 [杭州汽运部]"));
        timelineList.add(new TaskDetailTimelineData("2016-05-23 18:59:41", "[杭州市] 快件离开 [杭州乔司区]已发往[沈阳]"));
        timelineList.add(new TaskDetailTimelineData("2016-05-23 18:35:32", "[杭州市] [杭州乔司区]的市场部已收件 电话:18358xxxxxx"));
    }

    /**
     * 初始化 TodolistView
     */
    public void initTodoListViewComponent() {
        AutoHeightListView listViewComponent = findViewById(R.id.task_todo_list_view);
        
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

        todoAdapter = new TaskDetailTodoListAdapter(mContext, listViewComponent, todoList, observer);
        listViewComponent.setAdapter(todoAdapter);
    }

    /**
     * 初始化 oklistView
     */
    public void initOkListViewComponent() {
        RecyclerView listViewComponent = (RecyclerView) findViewById(R.id.task_ok_list_view);

        timelineAdapter = new TaskDetailTimelineAdapter(this, timelineList);
        listViewComponent.setLayoutManager(new LinearLayoutManager(this));
        listViewComponent.setAdapter(timelineAdapter);
    }

    /**
     * 任务 描述 跳转
     */
    public void jumpToDetailDescription() {
        QMUISpanTouchFixTextView titleComponent = findViewById(R.id.task_des_title);
        QMUISpanTouchFixTextView intentComponent = findViewById(R.id.task_des_intent);
        QMUISpanTouchFixTextView guideComponent = findViewById(R.id.task_des_guide);
        QMUISpanTouchFixTextView planComponent = findViewById(R.id.task_des_plan);

        class TaskDetailType {
            public String intent = "intent";
            public String guide = "guide";
            public String plan = "plan";
        }

        class TaskDetailJump {
            TaskDetailJump(String type) {
                Intent intent = new Intent();

                intent.setClass(TaskActivity.this, TaskDetailDescriptionActivity.class);
                intent.putExtra("type", type);
                startActivityForResult(intent, 32112);
            }
        }
        
        titleComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                new TaskDetailJump(new TaskDetailType().intent);
            }
        });
        
        intentComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                new TaskDetailJump(new TaskDetailType().intent);
            }
        });
        
        guideComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                new TaskDetailJump(new TaskDetailType().guide);
            }
        });
        
        planComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                new TaskDetailJump(new TaskDetailType().plan);
            }
        });
    }
}
