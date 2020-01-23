package cn.rejiejay.application.task;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.rejiejay.application.R;

/**
 * 任务 介绍
 */
public class TaskDetailDescriptionActivity extends AppCompatActivity {
    private Context mContext;

    String pageType = allPageType.intent;

    /**
     * 生命周期
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_detail_description);

        mContext = this;

        // 初始化 绑定 组件的方法
        initPageComponent();
    }

    /**
     * 初始化 绑定 组件的方法
     */
    public void initPageComponent() {
        class TaskDetailType {
            public String intent = "intent";
            public String guide = "guide";
            public String plan = "plan";
        }

        // 取得从上一个Activity当中传递过来的Intent对象
        final Intent intent = getIntent();
        // 从Intent当中根据key取得value
        if (intent != null) {
            pageType = intent.getStringExtra("type");
        } else {
            TaskDetailType allPageType = new TaskDetailType();
            pageType = allPageType.intent;
        }

    }
}
