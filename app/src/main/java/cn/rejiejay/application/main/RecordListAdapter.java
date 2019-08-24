package cn.rejiejay.application.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.util.List;

import cn.rejiejay.application.R;

public class RecordListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<RecordFragmentListDate> listData;

    public RecordListAdapter(Context context, ListView listView, List listData) {
        this.context = context;
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);

        // 用于绑定点击事件
        // listView;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RecordFragmentListDate item = listData.get(i);

        String type = item.getType();

        // ViewHolder模式 是一种缓存机制
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.main_record_list, null);

            holder = new ViewHolder();

            // 记录模块
            holder.recordModel = view.findViewById(R.id.record_item_view);
            holder.recordImage = view.findViewById(R.id.record_item_image); // 图片
            holder.recordTab = view.findViewById(R.id.record_item_tab); // 标签
            holder.recordTitle = view.findViewById(R.id.record_item_title); // 标题
            holder.recordThink = view.findViewById(R.id.record_item_think); // 联想
            holder.recordContent = view.findViewById(R.id.record_item_content); // 内容


            // 事件模块
            holder.eventModel = view.findViewById(R.id.event_item_view);
            holder.eventImage = view.findViewById(R.id.event_item_image); // 图片
            holder.eventDate = view.findViewById(R.id.event_item_date); // 日期
            holder.eventCause = view.findViewById(R.id.event_item_cause); // 原因
            holder.eventHandle = view.findViewById(R.id.event_item_handle); // 过程
            holder.eventResult = view.findViewById(R.id.event_item_result); // 结果
            holder.eventConclusion = view.findViewById(R.id.event_item_conclusion); // 结论

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // 根据标签显示
        if (type.equals("record")) {
            holder.recordModel.setVisibility(View.VISIBLE);
        } else {
            holder.eventModel.setVisibility(View.VISIBLE);
        }

        // 加载数据


        return view;
    }

    class ViewHolder {
        LinearLayout recordModel; // 记录模块
        ImageView recordImage;
        QMUISpanTouchFixTextView recordTab;
        QMUISpanTouchFixTextView recordTitle;
        QMUISpanTouchFixTextView recordThink;
        QMUISpanTouchFixTextView recordContent;


        LinearLayout eventModel; // 事件模块
        ImageView eventImage;
        QMUISpanTouchFixTextView eventDate;
        QMUISpanTouchFixTextView eventCause;
        QMUISpanTouchFixTextView eventHandle;
        QMUISpanTouchFixTextView eventResult;
        QMUISpanTouchFixTextView eventConclusion;
    }
}

