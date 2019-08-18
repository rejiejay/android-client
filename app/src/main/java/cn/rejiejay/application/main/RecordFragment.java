package cn.rejiejay.application.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.rejiejay.application.SelectDateActivity;
import cn.rejiejay.application.R;
import cn.rejiejay.application.RecordEventActivity;
import cn.rejiejay.application.SelectTabActivity;

import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;


public class RecordFragment extends Fragment {
    public QMUISpanTouchFixTextView sequenceBtn; // 排序按钮
    public QMUISpanTouchFixTextView tabBtn; // 标签按钮
    public QMUISpanTouchFixTextView dateBtn; // 日期按钮
    QMUISpanTouchFixTextView addRecordBtn;
    QMUISpanTouchFixTextView addEventBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * 绑定 layout
         */
        return inflater.inflate(R.layout.main_record, container, false);
    }

    /**
     * 这里加载图片会报错
     */
    // @Override
    // public void onCreate(@Nullable Bundle savedInstanceState) {
    //     super.onCreate(savedInstanceState);
    // }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * 加载图片示例
         */
        String url = "https://rejiejay-1251940173.cos.ap-guangzhou.myqcloud.com/myweb/mobile-list/articles-1.png";
        ImageView myImage = view.findViewById(R.id.record_Item_img);
        ImageView myImage2 = view.findViewById(R.id.record_Item_img2);
        Glide.with(this)
                .load(url)
                .into(myImage);
        Glide.with(this)
                .load(url)
                .into(myImage2);

        initPageComponent(view); // 初始化 绑定 组件的方法

        /**
         * 初始化排序的方法
         */
        initSortHandle();

        /**
         * 跳转到 标签选择页面
         */
        jumpToSelectTabActivity();

        /**
         * 跳转到 日期选择页面
         */
        jumpToSelectDateActivity();

        /**
         * 跳转到 新增页面
         */
        jumpToAddActivity();
    }

    /**
     * 初始化 绑定 组件的方法
     */
    public void initPageComponent(View view) {
        sequenceBtn = view.findViewById(R.id.main_record_sequence);
        tabBtn = view.findViewById(R.id.main_record_tab);
        dateBtn = view.findViewById(R.id.main_record_date);
        addRecordBtn = view.findViewById(R.id.add_record_btn);
        addEventBtn = view.findViewById(R.id.add_event_btn);
    }

    /**
     * 初始化排序的方法
     */
    public void initSortHandle() {
        sequenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                String[] single_list = {"时间排序", "随机排序", "取消选择"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("请选择排序方式");

                builder.setSingleChoiceItems(single_list, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /**
     * 跳转到 标签选择页面
     */
    public void jumpToSelectTabActivity() {
        tabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SelectTabActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 跳转到 标签选择页面
     */
    public void jumpToSelectDateActivity() {
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SelectDateActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化跳转到 新增页面
     * <p>
     * intent传值 https://blog.csdn.net/chenliguan/article/details/47188243
     */
    public void jumpToAddActivity() {
        /**
         * 跳转到 新增记录
         */
        addRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RecordEventActivity.class);

                // 在Intent对象当中添加一个键值对
                intent.putExtra("type", "record");
                startActivity(intent);
            }
        });

        /**
         * 跳转到 新增事件
         */
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RecordEventActivity.class);
                // 在Intent对象当中添加一个键值对
                intent.putExtra("type", "event");
                startActivity(intent);
            }
        });
    }
}

