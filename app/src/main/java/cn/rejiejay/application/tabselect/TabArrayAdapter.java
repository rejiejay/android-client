package cn.rejiejay.application.tabselect;


import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class TabArrayAdapter extends ArrayAdapter<Tab> {

    // 构造函数
    public TabArrayAdapter(Context context, int textViewResourceId, List<Tab> objects) {
        super(context, textViewResourceId, objects);
    }
}

