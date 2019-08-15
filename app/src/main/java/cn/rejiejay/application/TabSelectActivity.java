package cn.rejiejay.application;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.rejiejay.application.tabselect.Tab;
import cn.rejiejay.application.tabselect.TabArrayAdapter;

public class TabSelectActivity extends AppCompatActivity {

    // 生命周期的 onCreate 周期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_select);

        initListView(); // 初始化 标签列表

        initCreateNewTab(); // 初始化 新增标签
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
//                TabSelectActivity.this,   // Context上下文
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

        // 从服务器初始化数据
        // initData();
        List<Tab> mData = new ArrayList<>();
        mData.add(new Tab("张三", "30"));
        mData.add(new Tab("张三", "30"));
        mData.add(new Tab("张三", "30"));
        mData.add(new Tab("张三", "30"));

        TabArrayAdapter adapter = new TabArrayAdapter(inflater, mData);

        myListView.setAdapter(adapter);
    }

    /**
     * 初始化 新增标签
     */
    public void initCreateNewTab() {
        LinearLayout createNewTabView = findViewById(R.id.createNewTab);

        createNewTabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TabSelectActivity.this);
                builder.setTitle("请输入要新建的分类");    // 设置对话框标题
                builder.setIcon(android.R.drawable.btn_star);   // 设置对话框标题前的图标

                final EditText edit = new EditText(TabSelectActivity.this);

                builder.setView(edit);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(TabSelectActivity.this, "你输入的是: " + edit.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(TabSelectActivity.this, "你点了取消", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setCancelable(true);    // 设置按钮是否可以按返回键取消,false则不可以取消
                final AlertDialog dialog = builder.create();  // 创建对话框
                dialog.setCanceledOnTouchOutside(true); // 设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏

                dialog.show();
            }
        });

    }
}
