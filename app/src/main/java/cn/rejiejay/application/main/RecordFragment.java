package cn.rejiejay.application.main;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.rejiejay.application.SelectDateActivity;
import cn.rejiejay.application.R;
import cn.rejiejay.application.RecordEventActivity;
import cn.rejiejay.application.SelectTabActivity;
import cn.rejiejay.application.component.AutoHeightListView;
import cn.rejiejay.application.component.RxGet;
import cn.rejiejay.application.utils.Consequent;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import mehdi.sakout.fancybuttons.FancyButton;

import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.util.ArrayList;
import java.util.List;

public class RecordFragment extends Fragment {
    private Context mContext;

    // 页面组件
    public QMUISpanTouchFixTextView sequenceBtn; // 排序按钮
    public QMUISpanTouchFixTextView dataTypeBtn; // 数据类型按钮
    public QMUISpanTouchFixTextView tabBtn; // 标签按钮
    public QMUISpanTouchFixTextView dateBtn; // 日期按钮

    public QMUISpanTouchFixTextView addRecordBtn; // 新增
    public QMUISpanTouchFixTextView addEventBtn;

    public RecordListAdapter mAdapter;

    public FancyButton loadMore; // 加载更多

    // 数据
    public List<RecordFragmentListDate> listData = new ArrayList<>();
    public int pageNo = 1; // 当前页码
    public int dataTotal = 0; // 总页数 默认0页
    public Boolean isAdd = false; // 是否添加页面数据

    // 排序
    public String sort = "time"; // 目前2种排序 random 默认 time
    public String sortType = "all"; // 目前3种排序 record event 默认 all
    public String sortTag = "all"; // 默认 all
    public long minTimestamp = 0; // 时间戳排序
    public long maxTimestamp = 0;

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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化 绑定 组件的方法
        initPageComponent(view);

        // listView
        initListViewComponent(view);

        // 页面数据
        initPageData();

        // 加载更多
        initLoadMore();

        // 排序
        initSortHandle();

        // 数据类型
        initSortType();

        // 跳转到 标签选择页面
        jumpToSelectTabActivity();

        // 跳转到 日期选择页面
        jumpToSelectDateActivity();

        // 跳转到 新增页面
        jumpToAddActivity();
    }

    /**
     * 初始化 绑定 组件的方法
     */
    public void initPageComponent(View view) {
        sequenceBtn = view.findViewById(R.id.main_record_sequence);
        dataTypeBtn = view.findViewById(R.id.main_record_type);
        tabBtn = view.findViewById(R.id.main_record_tab);
        dateBtn = view.findViewById(R.id.main_record_date);
        addRecordBtn = view.findViewById(R.id.add_record_btn);
        addEventBtn = view.findViewById(R.id.add_event_btn);
        loadMore = view.findViewById(R.id.main_record_load_more);
    }

    /**
     * 初始化 listView
     */
    public void initListViewComponent(View view) {
        AutoHeightListView listViewComponent = view.findViewById(R.id.record_event_list_view);

        // 注册 RxAndroid 进行页面通信
        Observer<Consequent> observer = new Observer<Consequent>() {
            @Override
            public void onSubscribe(Disposable d) { /** 不需要处理 */}

            @Override
            public void onNext(Consequent value) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RecordEventActivity.class);

                switch (value.getResult()) {
                    case 1931:
                        initPageData(); // 1931 表示删除成功 刷新页面
                        break;

                    case 1932: // 1932 表示编辑 跳转到 编辑记录页面

                        // 在Intent对象当中添加一个键值对
                        intent.putExtra("type", "record");
                        intent.putExtra("isEdit", true);
                        intent.putExtra("editRecordEventJSON", value.getData().toJSONString());
                        startActivityForResult(intent, 32201);
                        break;

                    case 1933: // 1933 表示编辑 跳转到 编辑事件页面

                        // 在Intent对象当中添加一个键值对
                        intent.putExtra("type", "event");
                        intent.putExtra("isEdit", true);
                        intent.putExtra("editRecordEventJSON", value.getData().toJSONString());
                        startActivityForResult(intent, 32202);
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {/** 暂不处理 */}

            @Override
            public void onComplete() { /** 不需要处理 */}
        };

        mAdapter = new RecordListAdapter(mContext, listViewComponent, listData, observer);
        listViewComponent.setAdapter(mAdapter);
    }

    /**
     * 加载页面数据
     */
    public void initPageData() {
        Observer<Consequent> observer = new Observer<Consequent>() {
            @Override
            public void onSubscribe(Disposable d) {/* 不需要操作*/ }

            @Override
            public void onNext(Consequent value) {
                Log.d("加载页面数据", value.getJsonStringMessage());

                if (value.getResult() == 1) {
                    // 总页码
                    dataTotal = value.getData().getIntValue("total");

                    // 数据库数据转换为页面数据
                    JSONArray dataList = value.getData().getJSONArray("list");

                    if (!isAdd) {
                        listData.clear();
                    }
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

                    // 加载更多按钮
                    isAdd = false; // 返回初始化状态
                    if (sort.equals("random")) {
                        loadMore.setVisibility(View.VISIBLE);
                    } else if ((pageNo * 10) > dataTotal) {
                        loadMore.setVisibility(View.GONE);
                    } else {
                        loadMore.setVisibility(View.VISIBLE);
                    }

                    mAdapter.notifyDataSetChanged();

                } else {
                    // 弹出重新加载（暂不实现
                }

            }

            @Override
            public void onError(Throwable e) { /* 暂不实现*/ }

            @Override
            public void onComplete() {/* 不需要操作*/}
        };

        String url = "/android/recordevent/list?sort=" + sort + "&pageNo=" + pageNo;

        if (!sortType.equals("all")) {
            url += "&type=" + sortType;
        }

        if (!sortTag.equals("all")) {
            url += "&tag=" + sortTag;
        }

        /*
         * 判断是否日期选择
         */
        if (minTimestamp > 0 && maxTimestamp > 0) {
            url = "/android/recordevent/getbytime?pageNo=" + pageNo
                    + "&minTimestamp=" + minTimestamp
                    + "&maxTimestamp=" + maxTimestamp;
        }

        RxGet httpRxGet = new RxGet(mContext, url, "");
        httpRxGet.observable().subscribe(observer);
    }


    /**
     * 初始化加载更多的方法
     */
    public void initLoadMore() {
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                isAdd = true;
                pageNo++;
                initPageData();
            }
        });
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

                int checkingItem = 0;
                if (sort.equals("random")) {
                    checkingItem = 1;
                }

                builder.setSingleChoiceItems(single_list, checkingItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            sort = "time";
                            pageNo = 1;
                            sequenceBtn.setText("时间排序");
                            initPageData();
                        } else {
                            sort = "random";
                            pageNo = 1;
                            sequenceBtn.setText("随机排序");
                            initPageData();
                        }

                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /**
     * 初始化 数据类型 的方法
     */
    public void initSortType() {
        dataTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                String[] single_list = {"所有", "记录类型", "事件类型", "取消选择"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("请选择数据类型");

                int checkeItem = 0;
                if (sortType.equals("record")) {
                    checkeItem = 1;

                } else if (sortType.equals("event")) {
                    checkeItem = 2;

                }

                builder.setSingleChoiceItems(single_list, checkeItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                sortType = "all";
                                pageNo = 1;
                                dataTypeBtn.setText("数据类型");
                                initPageData();

                            case 1:
                                sortType = "record";
                                pageNo = 1;
                                dataTypeBtn.setText("记录类型");
                                initPageData();

                            case 2:
                                sortType = "event";
                                pageNo = 1;
                                dataTypeBtn.setText("事件类型");
                                initPageData();
                        }

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
     * 跳转到 日期选择页面
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
                String tag = data.getStringExtra("tag");
                if (tag != null && tag.length() > 0) {

                    if (tag.equals("all")) {
                        sortTag = "";
                        tabBtn.setText("标签");
                    } else {
                        sortTag = tag;
                        tabBtn.setText(tag);
                    }

                    initPageData();
                }

                break;

            // 日期 选择
            case 20133:
                String name = data.getStringExtra("name");
                long thisMinTimestamp = data.getLongExtra("minTimestamp", 0);
                long thisMaxTimestamp = data.getLongExtra("maxTimestamp", 0);

                // 判断是否选择全部
                if (thisMinTimestamp > 0 && thisMaxTimestamp > 0) {
                    dateBtn.setText(name);
                    minTimestamp = thisMinTimestamp;
                    maxTimestamp = thisMaxTimestamp;
                    isAdd = false;
                    int pageNo = 1;
                    initPageData();

                } else {
                    dateBtn.setText("日期");
                    minTimestamp = 0;
                    maxTimestamp = 0;
                    isAdd = false;
                    int pageNo = 1;
                    initPageData();
                }

                break;

            // 新增 记录
            case 32201:
                initPageData();
                break;

            // 新增 事件
            case 32202:
                Log.d("新增事件", "1");
                initPageData();
                break;

        }
    }
}

