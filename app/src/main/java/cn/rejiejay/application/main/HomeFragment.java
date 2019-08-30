package cn.rejiejay.application.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import cn.rejiejay.application.R;
import cn.rejiejay.application.RecordEventActivity;

public class HomeFragment extends Fragment {
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();

        /*
         * 绑定 layout
         */
        return inflater.inflate(R.layout.main_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // 跳转 新增 记录事件
        initJumpAddRecordEvent(view);
    }


    // 跳转 新增 记录事件
    public void initJumpAddRecordEvent(View view) {
        RelativeLayout addRecord = view.findViewById(R.id.main_home_add_record);
        RelativeLayout addEvent = view.findViewById(R.id.main_home_add_event);

        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                Intent intent = new Intent();
                intent.setClass(mContext, RecordEventActivity.class);
                intent.putExtra("type", "record");
                startActivityForResult(intent, 32201);
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                Intent intent = new Intent();
                intent.setClass(mContext, RecordEventActivity.class);
                intent.putExtra("type", "event");
                startActivityForResult(intent, 32202);
            }
        });
    }
}

