package cn.rejiejay.application.task;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.rejiejay.application.R;

public class TaskDetailDescriptionActivity extends AppCompatActivity {
    private Context mContext;

    /**
     * 生命周期
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }
}
