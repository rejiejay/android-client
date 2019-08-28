package cn.rejiejay.application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.rejiejay.application.component.RxGet;
import cn.rejiejay.application.selectdate.Dept;
import cn.rejiejay.application.selectdate.Node;
import cn.rejiejay.application.selectdate.NodeHelper;
import cn.rejiejay.application.selectdate.NodeTreeAdapter;
import cn.rejiejay.application.utils.Consequent;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SelectDateActivity extends AppCompatActivity {
    private Context mContext;
    private NodeTreeAdapter mAdapter;
    private LinkedList<Node> mLinkedList = new LinkedList<>();

    // 生命周期的 onCreate 周期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectdate);

        mContext = this;

        initTreeNode(); // 初始化节点
        initSelectAll(); // 初始化选择全部
    }

    /**
     * 初始化节点
     * 这东西本质是个 ListView
     */
    public void initTreeNode() {
        ListView mListView = findViewById(R.id.data_tree_node);

        // 注册 RxAndroid 进行页面通信
        Observer<Consequent> observer = new Observer<Consequent>() {
            @Override
            public void onSubscribe(Disposable d) { /* 不需要处理 */}

            @Override
            public void onNext(Consequent value) {
                Log.d("注册 RxAndroid 进行页面通信", value.getJsonStringMessage());
                if (value.getResult() == 1) {
                    JSONObject thisData = value.getData();
                    String name = thisData.getString("name");
                    long minTimestamp = thisData.getLongValue("minTimestamp");
                    long maxTimestamp = thisData.getLongValue("maxTimestamp");
                    submitConfirm(name, minTimestamp, maxTimestamp);
                }
            }

            @Override
            public void onError(Throwable e) {/* 暂不处理 */}

            @Override
            public void onComplete() { /* 不需要处理 */}
        };

        mAdapter = new NodeTreeAdapter(this, mListView, mLinkedList, observer);
        mListView.setAdapter(mAdapter);

        getStatisticData(); // 获取统计数据
    }

    // 获取统计数据
    private void getStatisticData() {
        Observer<Consequent> observer = new Observer<Consequent>() {
            @Override
            public void onSubscribe(Disposable d) {/* 不需要操作*/ }

            @Override
            public void onNext(Consequent value) {
                Log.d("加载页面数据", value.getJsonStringMessage());

                if (value.getResult() == 1) {
                    JSONArray handleData = handleAjaxData(value.getData());
                    jsonArrayToNodeList(handleData);
                } else {
                    /* 暂不实现*/
                }
            }

            @Override
            public void onError(Throwable e) { /* 暂不实现*/ }

            @Override
            public void onComplete() {/* 不需要操作*/}
        };

        RxGet httpRxGet = new RxGet(mContext, "/android/recordevent/date/get/", "");
        httpRxGet.observable().subscribe(observer);
    }

    // 生成唯一标识的东西
    private class CreateId {
        public int id;

        CreateId() {
            id = 1;
        }

        public CreateId add() {
            id++;
            return this;
        }

        public int get() {
            return Integer.parseInt(String.valueOf(id));
        }
    }

    // 处理统计数据
    // 目标格式
    // [{
    // id: 1,
    // parentId: 1,
    // name: "1月 (8)",
    // minTimestamp: 1535731200000,
    // maxTimestamp: 1535731200000,
    // }]
    private JSONArray handleAjaxData(JSONObject handleData) {
        JSONArray statisticArray = handleData.getJSONArray("statistic");
        JSONArray handleArray = new JSONArray();

        CreateId myId = new CreateId();

        for (int i = 0; i < statisticArray.size(); i++) {

            JSONObject data = new JSONObject();
            JSONObject item = statisticArray.getJSONObject(i);

            // 年份唯一标识
            int thisId = myId.add().get();
            data.put("id", thisId);
            data.put("parentId", 0);
            String name = item.getString("name") + " (" + item.getInteger("count") + ")";
            data.put("name", name);
            data.put("minTimestamp", String.valueOf(item.getLong("minTimestamp")));
            data.put("maxTimestamp", String.valueOf(item.getLong("maxTimestamp")));

            handleArray.add(data);

            // 转换季节
            handleDataSeason(thisId, myId, handleArray, item.getJSONArray("data"));
        }

        return handleArray;
    }

    // 季节 数据处理
    private void handleDataSeason(int parentId, CreateId myId, JSONArray handleArray, JSONArray handleData) {
        for (int i = 0; i < handleData.size(); i++) {
            JSONObject data = new JSONObject();
            JSONObject item = handleData.getJSONObject(i);

            int thisId = myId.add().get(); // 新的唯一标识
            data.put("id", thisId);
            data.put("parentId", parentId);
            String name = item.getString("name") + " (" + item.getInteger("count") + ")";
            data.put("name", name);
            data.put("minTimestamp", String.valueOf(item.getLong("minTimestamp")));
            data.put("maxTimestamp", String.valueOf(item.getLong("maxTimestamp")));

            handleArray.add(data);

            // 判断是否有月份
            JSONArray monthArray = item.getJSONArray("data");
            if (monthArray != null && monthArray.size() > 0) {
                // 有数据的情况下，处理月份
                handleDataMonth(thisId, myId, handleArray, monthArray);
            }

        }
    }

    // 月份 数据处理
    private void handleDataMonth(int parentId, CreateId myId, JSONArray handleArray, JSONArray handleData) {
        for (int i = 0; i < handleData.size(); i++) {
            JSONObject data = new JSONObject();
            JSONObject item = handleData.getJSONObject(i);

            int thisId = myId.add().get(); // 新的唯一标识
            data.put("id", thisId);
            data.put("parentId", parentId);
            String name = item.getString("name") + " (" + item.getInteger("count") + ")";
            data.put("name", name);
            data.put("minTimestamp", String.valueOf(item.getLong("minTimestamp")));
            data.put("maxTimestamp", String.valueOf(item.getLong("maxTimestamp")));

            handleArray.add(data);

            // 判断是否有月份
            JSONArray weekArray = item.getJSONArray("data");
            if (weekArray != null && weekArray.size() > 0) {
                // 有数据的情况下，处理星期
                handleDataWeek(thisId, myId, handleArray, weekArray);
            }
        }
    }

    // 星期 数据处理
    private void handleDataWeek(int parentId, CreateId myId, JSONArray handleArray, JSONArray handleData) {
        for (int i = 0; i < handleData.size(); i++) {
            JSONObject data = new JSONObject();
            JSONObject item = handleData.getJSONObject(i);

            int thisId = myId.add().get(); // 新的唯一标识
            data.put("id", thisId);
            data.put("parentId", parentId);
            String name = item.getString("name") + " (" + item.getInteger("count") + ")";
            data.put("name", name);
            data.put("minTimestamp", String.valueOf(item.getLong("minTimestamp")));
            data.put("maxTimestamp", String.valueOf(item.getLong("maxTimestamp")));

            handleArray.add(data);
        }
    }

    private void jsonArrayToNodeList(JSONArray jsonArray) {
        List<Node> data = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            int id = item.getIntValue("id");
            int parentId = item.getIntValue("parentId");
            String name = item.getString("name");
            long minTimestamp = item.getLongValue("minTimestamp");
            long maxTimestamp = item.getLongValue("maxTimestamp");
            data.add(new Dept(id, parentId, name, minTimestamp, maxTimestamp));
        }

        mLinkedList.addAll(NodeHelper.sortNodes(data));
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 保存并返回上一页
     */
    public void initSelectAll() {
        LinearLayout selectAll = findViewById(R.id.select_date_select_all);

        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitConfirm("", 0, 0);
            }
        });
    }

    /**
     * 保存并返回上一页
     */
    public void submitConfirm(String name, long minTimestamp, long maxTimestamp) {
        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("name", minTimestamp);
        intent.putExtra("name", maxTimestamp);
        setResult(20133, intent); // 代码是固定的
        finish();
    }
}
