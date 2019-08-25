package cn.rejiejay.application.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.rejiejay.application.R;
import cn.rejiejay.application.utils.DateFormat;

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
            holder.eventModel.setVisibility(View.GONE);
        } else {
            holder.recordModel.setVisibility(View.GONE);
            holder.eventModel.setVisibility(View.VISIBLE);
        }

        // 根据标 加载 图片（暂时不实现
        // String imgId = item.getImageidentity();
        // String url = item.getImageurl();
        String url = "https://rejiejay-1251940173.cos.ap-guangzhou.myqcloud.com/myweb/mobile-list/articles-1.png";
        if (type.equals("record")) {
            holder.recordImage.setVisibility(View.VISIBLE); // 为了解决高度自适应问题
            holder.eventImage.setVisibility(View.GONE);
            Glide.with(context)
                    .load(url)
                    .into(holder.recordImage);
        } else {
            holder.recordImage.setVisibility(View.GONE); // 为了解决高度自适应问题
            holder.eventImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(url)
                    .into(holder.eventImage);
        }

        String myTag = item.getTag();
        if (myTag.equals("")) {
            myTag = "无分类";
        }
        holder.recordTab.setText(myTag);
        holder.recordTitle.setText(item.getRecordtitle());

        String recordThinkStr = item.getRecordmaterial().trim().replace("\\n", "\n");;
        if (recordThinkStr.equals("") || recordThinkStr.length() == 0) {
            holder.recordThink.setVisibility(View.GONE);
        } else {
            holder.recordThink.setVisibility(View.VISIBLE);
        }
        holder.recordThink.setText(recordThinkStr);
        holder.recordContent.setText(item.getRecordcontent().trim().replace("\\n", "\n"));

        DateFormat thisDate = new DateFormat(new Date(item.getTimestamp()));
        // 获取天数
        String dd = String.valueOf(thisDate.getDay());
        // 获取星期
        String EEEE = thisDate.getWeekCn();

        String eventDateStr = String.valueOf(item.getFullyear()) + "y " + dd + "d " + String.valueOf(item.getMonth()) + "月 第" + String.valueOf(item.getWeek()) + "周 " + EEEE;
        holder.eventDate.setText(eventDateStr);
        String eventCauseStr = "起因: " + item.getEventcause();
        holder.eventCause.setText(eventCauseStr);
        String eventHandleStr = "过程: " + item.getEventprocess().trim().replace("\\n", "\n");
        holder.eventHandle.setText(eventHandleStr);
        String eventResultStr = "结果: " + item.getEventresult().trim().replace("\\n", "\n");
        holder.eventResult.setText(eventResultStr);
        String eventConclusionStr = "结论: " + item.getEventconclusion().trim().replace("\\n", "\n");
        holder.eventConclusion.setText(eventConclusionStr);

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

