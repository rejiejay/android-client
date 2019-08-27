package cn.rejiejay.application;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    }

    /**
     * 初始化节点
     * 这东西本质是个 ListView
     */
    public void initTreeNode() {
        ListView mListView = findViewById(R.id.data_tree_node);

        mAdapter = new NodeTreeAdapter(this, mListView, mLinkedList);
        mListView.setAdapter(mAdapter);

        getStatisticData(); // 获取统计数据
    }

    /**
     * 初始化数据
     */
    private void initData() {
        List<Node> data = new ArrayList<>();
        addOne(data);
        mLinkedList.addAll(NodeHelper.sortNodes(data));
        mAdapter.notifyDataSetChanged();
    }


    private void addOne(List<Node> data) {
        data.add(new Dept(1, 0, "总公司"));//可以直接注释掉此项，即可构造一个森林
        data.add(new Dept(2, 1, "一级部一级部门一级部门一级部门门级部门一级部门级部门一级部门一级部门门级部一级"));
        data.add(new Dept(3, 1, "一级部门"));
        data.add(new Dept(4, 1, "一级部门"));

        data.add(new Dept(222, 5, "二级部门--测试1"));
        data.add(new Dept(223, 5, "二级部门--测试2"));

        data.add(new Dept(5, 1, "一级部门"));

        data.add(new Dept(224, 5, "二级部门--测试3"));
        data.add(new Dept(225, 5, "二级部门--测试4"));

        data.add(new Dept(6, 1, "一级部门"));
        data.add(new Dept(7, 1, "一级部门"));
        data.add(new Dept(8, 1, "一级部门"));
        data.add(new Dept(9, 1, "一级部门"));
        data.add(new Dept(10, 1, "一级部门"));

        for (int i = 2; i <= 10; i++) {
            for (int j = 0; j < 10; j++) {
                data.add(new Dept(1 + (i - 1) * 10 + j, i, "二级部门" + j));
            }
        }

        for (int i = 0; i < 5; i++) {
            data.add(new Dept(101 + i, 11, "三级部门" + i));
        }

        for (int i = 0; i < 5; i++) {
            data.add(new Dept(106 + i, 22, "三级部门" + i));
        }
        for (int i = 0; i < 5; i++) {
            data.add(new Dept(111 + i, 33, "三级部门" + i));
        }
        for (int i = 0; i < 5; i++) {
            data.add(new Dept(115 + i, 44, "三级部门" + i));
        }

        for (int i = 0; i < 5; i++) {
            data.add(new Dept(401 + i, 101, "四级部门" + i));
        }
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
            data.add(new Dept(id, parentId, name));
        }

        mLinkedList.addAll(NodeHelper.sortNodes(data));
        mAdapter.notifyDataSetChanged();
    }
}
