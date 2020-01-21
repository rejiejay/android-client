package cn.rejiejay.application;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TaskActivity extends AppCompatActivity {
    private Context mContext;

    /**
     * 生命周期
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);

        mContext = this;
    }
}
