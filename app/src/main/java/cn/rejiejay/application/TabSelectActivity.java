package cn.rejiejay.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TabSelectActivity extends AppCompatActivity {
    ListView myListView;
    private String[] data = {"Apple",
            "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple",
            "Strawberry", "Cherry", "Mango"};

    // 生命周期的 onCreate 周期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tab);

        initPageComponent(); // 初始化 绑定 组件的方法

        initListView(); // 初始化 标签列表
    }

    /**
     * 初始化 绑定 组件的方法
     */
    public void initPageComponent() {
        myListView = findViewById(R.id.tab_list_view);
    }

    /**
     * 初始化 标签列表
     */
    public void initListView() {
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//            TabSelectActivity.this,   // Context上下文
//                myListView,  // 子项布局id
//                data);

    }
}
