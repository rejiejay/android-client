package cn.rejiejay.application;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.util.ArrayList;

import cn.rejiejay.application.recordevent.UploadImageAdapter;

public class RecordEventActivity extends AppCompatActivity {
    private Context mContext;
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源

    /**
     * 页面状态
     * record 记录页面 event 事件页面
     */
    String pageType = "record";

    QMUISpanTouchFixTextView recordTabView;
    QMUISpanTouchFixTextView eventTabView;

    // 生命周期的 onCreate 周期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordevent);

        mContext = this;

        // 取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            pageType = intent.getStringExtra("type");
        }

        initComponent(); // 初始化頁面组件

        initPageType(); // 根据 pageType 改变页面状态
        initTopTab(); // 初始化 顶部

        initGridView(); // 初始化展示上传图片的GridView
    }

    /**
     * 初始化頁面组件
     */
    public void initComponent() {
        recordTabView = findViewById(R.id.record_event_tab_left);
        eventTabView = findViewById(R.id.record_event_tab_right);
    }

    /**
     * 改变页面状态
     */
    public void initPageType() {

        if (pageType.equals("record")) {
            // 记录
            recordTabView.setTextColor(Color.rgb(255, 255, 255)); // 记录的文字是白色
            recordTabView.setBackgroundColor(Color.rgb(33, 150, 243)); // 背景是蓝色
            eventTabView.setTextColor(Color.rgb(144, 147, 153));
            eventTabView.setBackgroundColor(Color.rgb(255, 255, 255));

        } else {
            // 事件
            recordTabView.setTextColor(Color.rgb(144, 147, 153));
            recordTabView.setBackgroundColor(Color.rgb(255, 255, 255));
            eventTabView.setTextColor(Color.rgb(255, 255, 255)); // 事件的文字是白色
            eventTabView.setBackgroundColor(Color.rgb(33, 150, 243)); // 背景是蓝色

        }
    }

    /**
     * 初始化顶部
     */
    public void initTopTab() {
        recordTabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                pageType = "record";
                initPageType();
            }
        });

        eventTabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                pageType = "event";
                initPageType();
            }
        });
    }

    // 初始化展示上传图片的GridView
    private void initGridView() {
        GridView gridView = findViewById(R.id.record_event_grid);
        UploadImageAdapter mGridViewAddImgAdapter = new UploadImageAdapter(mContext, mPicList);

        gridView.setAdapter(mGridViewAddImgAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    // 如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过1张，才能点击
                    if (mPicList.size() == 1) {
                        // 点击图片查看大图（最多添加1张图片
//                        viewPluImg(position);
                    } else {
                        // 添加凭证图片
//                        selectPic(MainConstant.MAX_SELECT_PIC_NUM - mPicList.size());
                    }
                } else {
                    // 点击图片查看大图
//                    viewPluImg(position);
                }
            }
        });
    }

    /**
     * 查看大图
     *
     * @param position
     */
    private void viewPluImg(int position) {
//        Intent intent = new Intent(mContext, PlusImageActivity.class);
//        intent.putStringArrayListExtra(MainConstant.IMG_LIST, mPicList);
//        intent.putExtra(MainConstant.POSITION, position);
//        startActivityForResult(intent, MainConstant.REQUEST_CODE_MAIN);
    }
}
