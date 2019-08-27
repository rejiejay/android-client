package cn.rejiejay.application;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.rejiejay.application.component.RxGet;
import cn.rejiejay.application.selecttab.Tab;
import cn.rejiejay.application.selecttab.TabArrayAdapter;
import cn.rejiejay.application.utils.Consequent;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SelectTabActivity extends AppCompatActivity {
    private Context mContext;

    private List<Tab> mData = new ArrayList<>();
    private TabArrayAdapter adapter;

    private Boolean isDelete = false; // 是否删除

    // 生命周期的 onCreate 周期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecttab);

        mContext = this;

        initListView(); // 列表

        initCreateNewTag(); // 新增

        // initDelTag(); // 删除

        initSelectTag(R.id.select_tab_all_tab_btn, "all"); // 所有
        // initSelectTag(R.id.select_tab_record_tab_btn, "all", "record"); // 记录
        // initSelectTag(R.id.select_tab_event_tab_btn, "all", "event"); // 事件
    }

    /**
     * 初始化 标签列表
     */
    public void initListView() {
        ListView myListView = findViewById(R.id.tab_list_view);

        /**
         * ArrayAdapter期望接受的样式文件里只含有一个textView，然后它把接受到的数据toString后展示在textView里。
         * 如果我们需要展示的内容是一仅一个textView承载不了的，还需要其它组件、这就需要写一个类继承自ArrayAdapter并且重写getView方法
         */
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                SelectTabActivity.this,   // Context上下文
//                R.layout.activity_tab_select_item,  // 子项布局id
//                data);
//
//        myListView.setAdapter(adapter);

        /**
         * LayoutInflater 是一个抽象类 主要是用于加载布局的（加载布局的任务通常都是在Activity中调用setContentView()方法来完成，内部也是使用LayoutInflater来加载布局的，只不过这部分源码是internal的，不太容易查看到。
         * 其实是调用 LayoutInflater.from(context)。
         * 本质是都是调用 context.getSystemService()
         */
        LayoutInflater inflater = getLayoutInflater(); // 调用Activity的getLayoutInflater() 获得 LayoutInflater 实例

        // 注册 RxAndroid 进行页面通信
        Observer<Consequent> observer = new Observer<Consequent>() {
            @Override
            public void onSubscribe(Disposable d) { /** 不需要处理 */}

            @Override
            public void onNext(Consequent value) {
                Log.d("注册 RxAndroid 进行页面通信", value.getJsonStringMessage());

                if (value.getResult() == 1930) {
                    submitConfirm(value.getData().getString("name"));
                }
            }

            @Override
            public void onError(Throwable e) {/** 暂不处理 */}

            @Override
            public void onComplete() { /** 不需要处理 */}
        };

        adapter = new TabArrayAdapter(inflater, mData, observer);

        myListView.setAdapter(adapter);

        initData();
    }

    // 从服务器初始化数据
    public void initData() {

        Observer<Consequent> observer = new Observer<Consequent>() {
            @Override
            public void onSubscribe(Disposable d) {/* 不需要操作*/ }

            @Override
            public void onNext(Consequent value) {
                Log.d("加载页面数据", value.getJsonStringMessage());

                if (value.getResult() == 1) {

                    JSONArray dataList = value.getData().getJSONArray("list");

                    Log.d("dataList", dataList.toJSONString());

                    mData.clear();
                    for (int i = 0; i < dataList.size(); i++) {
                        JSONObject item = JSONObject.parseObject(JSON.toJSONString(dataList.get(i)));

                        mData.add(new Tab(item.getIntValue("tagid"), item.getString("tagname"), false));
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) { /* 暂不实现*/ }

            @Override
            public void onComplete() {/* 不需要操作*/}
        };

        RxGet httpRxGet = new RxGet(mContext, "/android/recordevent/tag/get/", "");
        httpRxGet.observable().subscribe(observer);
    }

    /**
     * 初始化 新增标签
     */
    public void initCreateNewTag() {
        LinearLayout createNewTabView = findViewById(R.id.createNewTab);

        class CreateNewTag {
            CreateNewTag(String editText) {

                Observer<Consequent> observer = new Observer<Consequent>() {
                    @Override
                    public void onSubscribe(Disposable d) {/* 不需要操作*/ }

                    @Override
                    public void onNext(Consequent value) {

                        if (value.getResult() == 1) {
                            initData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) { /* 暂不实现*/ }

                    @Override
                    public void onComplete() {/* 不需要操作*/}
                };

                RxGet httpRxGet = new RxGet(mContext, "/android/recordevent/tag/add?", "tag=" + editText);
                httpRxGet.observable().subscribe(observer);
            }
        }

        createNewTabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SelectTabActivity.this);
                builder.setTitle("请输入要新建的分类");    // 设置对话框标题

                final EditText edit = new EditText(SelectTabActivity.this);

                builder.setView(edit);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String editText = edit.getText().toString();
                        if (editText.length() > 0) {
                            new CreateNewTag(editText);
                        } else {
                            Toast.makeText(SelectTabActivity.this, "输入的标签不能为空 ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { /* 不作操作 */ }
                });
                builder.setCancelable(true);    // 设置按钮是否可以按返回键取消,false则不可以取消
                final AlertDialog dialog = builder.create();  // 创建对话框
                dialog.setCanceledOnTouchOutside(true); // 设置弹出框失去焦点是否隐藏, 即点击屏蔽其它地方是否隐藏
                dialog.show();
            }
        });

    }

    /**
     * 初始化 删除标签 (不需要实现
     * ListView数据动态刷新: https://blog.csdn.net/cshichao/article/details/9333497
     */
    //public void initDelTag() {
    //    LinearLayout delTabView = findViewById(R.id.delTabView);
    //
    //    delTabView.setOnClickListener(new View.OnClickListener() {
    //        @Override
    //        public void onClick(View thisView) {
    //            isDelete = !isDelete;
    //
    //            for (int index = 0; index < mData.size(); index++) {
    //                mData.get(index).setDelete(isDelete);
    //            }
    //
    //            adapter.notifyDataSetChanged();
    //        }
    //    });
    //}

    /**
     * 初始化 通用标签
     */
    public void initSelectTag(int id, final String tag) {
        LinearLayout allTabBtn = findViewById(id);

        allTabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                submitConfirm(tag);
            }
        });
    }

    /**
     * 返回上一页
     */
    public void submitConfirm(String tag) {
        Intent intent = new Intent();
        intent.putExtra("tag", tag);
        setResult(20132, intent); // 代码是固定的
        finish();
    }
}
