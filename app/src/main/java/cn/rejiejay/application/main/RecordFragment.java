package cn.rejiejay.application.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import cn.rejiejay.application.SelectDateActivity;
import cn.rejiejay.application.R;
import cn.rejiejay.application.RecordEventActivity;
import cn.rejiejay.application.SelectTabActivity;
import cn.rejiejay.application.component.AutoHeightListView;
import cn.rejiejay.application.component.RxGet;
import cn.rejiejay.application.utils.Consequent;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.util.ArrayList;
import java.util.List;


public class RecordFragment extends Fragment {
    private Context mContext;

    // 页面组件
    public QMUISpanTouchFixTextView sequenceBtn; // 排序按钮
    public QMUISpanTouchFixTextView tabBtn; // 标签按钮
    public QMUISpanTouchFixTextView dateBtn; // 日期按钮
    public QMUISpanTouchFixTextView addRecordBtn;
    public QMUISpanTouchFixTextView addEventBtn;
    public RecordListAdapter mAdapter;

    // 数据
    public List<RecordFragmentListDate> listData = new ArrayList<>();
    public int dataTotal = 0; // 页数 默认0页


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();

        // 绑定 layout
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

        // 加载图片示例
//        String url = "https://rejiejay-1251940173.cos.ap-guangzhou.myqcloud.com/myweb/mobile-list/articles-1.png";
//        ImageView myImage = view.findViewById(R.id.record_item_img);
//        ImageView myImage2 = view.findViewById(R.id.record_item_img2);
//        Glide.with(this)
//                .load(url)
//                .into(myImage);
//        Glide.with(this)
//                .load(url)
//                .into(myImage2);

        // 初始化 绑定 组件的方法
        initPageComponent(view);

        // 初始化 listView
        initListViewComponent(view);

        // 初始化排序的方法
        initSortHandle();

        // 跳转到 标签选择页面
        jumpToSelectTabActivity();

        // 跳转到 日期选择页面
        jumpToSelectDateActivity();

        // 跳转到 新增页面
        jumpToAddActivity();

        // 加载页面数据
        initPageData();
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
     * 初始化 listView
     */
    public void initListViewComponent(View view) {
        AutoHeightListView listViewComponent = view.findViewById(R.id.record_event_list_view);

        mAdapter = new RecordListAdapter(mContext, listViewComponent, listData);
        listViewComponent.setAdapter(mAdapter);
    }

    /**
     * 加载页面数据
     */
    public void initPageData() {
        Observer<Consequent> observer = new Observer<Consequent>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Consequent value) {
                if (value.getResult() == 1) {
                    // 总页码
                    dataTotal = value.getData().getIntValue("total");

                    // 数据库数据转换为页面数据
                    JSONArray dataList = value.getData().getJSONArray("list");

                    listData.clear();
                    for (int i = 0; i < dataList.size(); i++) {
                        RecordFragmentListDate targetItem = new RecordFragmentListDate();
                        JSONObject item = (JSONObject) dataList.get(i);

                        targetItem.setAndroidid(item.getIntValue("androidid"));
                        targetItem.setType(item.getString("type"));
                        targetItem.setImageidentity(item.getString("imageidentity"));
                        targetItem.setImageurl(item.getString("imageurl"));
                        targetItem.setTimestamp(item.getLong("timestamp"));
                        targetItem.setFullyear(item.getIntValue("fullyear"));
                        targetItem.setMonth(item.getIntValue("month"));
                        targetItem.setWeek(item.getIntValue("week"));
                        targetItem.setTag(item.getString("tag"));

                        targetItem.setRecordtitle(item.getString("recordtitle"));
                        targetItem.setRecordmaterial(item.getString("recordmaterial"));
                        targetItem.setRecordcontent(item.getString("recordcontent"));

                        targetItem.setEventcause(item.getString("eventcause"));
                        targetItem.setEventprocess(item.getString("eventprocess"));
                        targetItem.setEventresult(item.getString("eventresult"));
                        targetItem.setEventconclusion(item.getString("eventconclusion"));

                        listData.add(targetItem);
                    }

                    mAdapter.notifyDataSetChanged();

                } else {
                    // 弹出重新加载（暂不实现
                }

            }

            @Override
            public void onError(Throwable e) {
                // 弹出重新加载（暂不实现
            }

            @Override
            public void onComplete() {
            }
        };

        RxGet httpRxGet = new RxGet(mContext, "/android/recordevent/list", "");
        httpRxGet.observable().subscribe(observer);
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

                startActivityForResult(intent, 20132);
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

                startActivityForResult(intent, 20133);
            }
        });
    }

    /**
     * 初始化跳转到 新增页面
     * <p>
     * intent传值 https://blog.csdn.net/chenliguan/article/details/47188243
     */
    public void jumpToAddActivity() {
        // 跳转到 新增记录
        addRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RecordEventActivity.class);

                // 在Intent对象当中添加一个键值对
                intent.putExtra("type", "record");
                startActivityForResult(intent, 32201);
            }
        });

        // 跳转到 新增事件
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RecordEventActivity.class);
                // 在Intent对象当中添加一个键值对
                intent.putExtra("type", "event");
                startActivityForResult(intent, 32202);
            }
        });
    }

    /**
     * 选择标签页面的回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("resultCode", String.valueOf(resultCode));

        switch (resultCode) {
            // 标签 选择
            case 20132:
                String result = data.getStringExtra("tab");
                Log.d("标签 选择", result);
                break;

            // 新增 记录
            case 32201:
                initPageData();
                break;

            // 新增 事件
            case 32202:
                initPageData();
                break;

        }
    }
}

