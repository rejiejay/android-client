package cn.rejiejay.application.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.util.Date;
import java.util.List;

import cn.rejiejay.application.R;
import cn.rejiejay.application.component.RxPost;
import cn.rejiejay.application.utils.Consequent;
import cn.rejiejay.application.utils.DateFormat;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RecordListAdapter extends BaseAdapter {
    private Context context;

    // RxAndroid 进行页面通信
    private Observer<Consequent> observer;

    private LayoutInflater inflater;
    private List<RecordFragmentListDate> listData;

    RecordListAdapter(Context context, ListView listView, List<RecordFragmentListDate> listData, Observer<Consequent> observer) {
        this.context = context;
        this.observer = observer;
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
            holder.observer = observer; // 这个也缓存下来，因为好像会丢失

            // 记录模块
            holder.recordModel = view.findViewById(R.id.record_item_view);
            holder.recordImage = view.findViewById(R.id.record_item_image); // 图片
            holder.recordTab = view.findViewById(R.id.record_item_tab); // 标签
            holder.recordTitle = view.findViewById(R.id.record_item_title); // 标题
            holder.recordThink = view.findViewById(R.id.record_item_think); // 联想
            holder.recordContent = view.findViewById(R.id.record_item_content); // 内容
            holder.recordDel = view.findViewById(R.id.record_item_del); // 删除


            // 事件模块
            holder.eventModel = view.findViewById(R.id.event_item_view);
            holder.eventImage = view.findViewById(R.id.event_item_image); // 图片
            holder.eventDate = view.findViewById(R.id.event_item_date); // 日期
            holder.eventCause = view.findViewById(R.id.event_item_cause); // 原因
            holder.eventHandle = view.findViewById(R.id.event_item_handle); // 过程
            holder.eventResult = view.findViewById(R.id.event_item_result); // 结果
            holder.eventConclusion = view.findViewById(R.id.event_item_conclusion); // 结论
            holder.eventDel = view.findViewById(R.id.event_item_del); // 删除

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        initPageType(type, holder); // 根据标签显示不同页面

        initImage(item, holder); // 根据标 加载 图片（暂时不实现

        initPageDate(item, holder); // 加载页面 数据

        initDel(i, holder); // 绑定删除

        initEdit(i, holder); // 绑定编辑

        return view;
    }

    class ViewHolder {
        Observer<Consequent> observer;

        LinearLayout recordModel; // 记录模块
        ImageView recordImage;
        QMUISpanTouchFixTextView recordTab;
        QMUISpanTouchFixTextView recordTitle;
        QMUISpanTouchFixTextView recordThink;
        QMUISpanTouchFixTextView recordContent;
        ImageView recordDel;


        LinearLayout eventModel; // 事件模块
        ImageView eventImage;
        QMUISpanTouchFixTextView eventDate;
        QMUISpanTouchFixTextView eventCause;
        QMUISpanTouchFixTextView eventHandle;
        QMUISpanTouchFixTextView eventResult;
        QMUISpanTouchFixTextView eventConclusion;
        ImageView eventDel;
    }

    /**
     * 根据标签显示不同页面
     */
    private void initPageType(String type, ViewHolder holder) {
        if (type.equals("record")) {
            holder.recordModel.setVisibility(View.VISIBLE);
            holder.eventModel.setVisibility(View.GONE);
        } else {
            holder.recordModel.setVisibility(View.GONE);
            holder.eventModel.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化图片（暂时不实现
     */
    private void initImage(RecordFragmentListDate item, ViewHolder holder) {
        String type = item.getType();

        // String imgId = item.getImageidentity();
        String url = item.getImageurl();
        if (url == null || url.length() <= 0) {
            holder.recordImage.setVisibility(View.GONE); // 为了解决高度自适应问题
            holder.eventImage.setVisibility(View.GONE);
            return;
        }
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

    }

    /**
     * 加载页面
     */
    private void initPageDate(RecordFragmentListDate item, ViewHolder holder) {
        String myTag = item.getTag();
        if (myTag.equals("")) {
            myTag = "无分类";
        }
        holder.recordTab.setText(myTag);
        holder.recordTitle.setText(item.getRecordtitle());

        String recordThinkStr = item.getRecordmaterial().trim().replace("\\n", "\n");
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
    }

    /**
     * 绑定删除
     */
    private void initDel(int i, final ViewHolder holder) {
        final RecordFragmentListDate item = listData.get(i);

        // 删除
        class DelItemById {
            DelItemById() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("删除");
                builder.setMessage("你确定要删除这条信息?");
                // 点击对话框以外的区域是否让对话框消失
                builder.setCancelable(true);
                // 设置正面按钮
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delAjax(); // 避免嵌套过深
                    }
                });
                // 设置反面按钮
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { /* 不需要处理 */ }
                });
                AlertDialog dialog = builder.create();
                // 显示对话框
                dialog.show();
            }

            private void delAjax() {

                JSONObject submitData = new JSONObject();
                submitData.put("androidid", item.getAndroidid());

                Observer<Consequent> observer = new Observer<Consequent>() {
                    @Override
                    public void onSubscribe(Disposable d) { /* 不需要处理 */}

                    @Override
                    public void onNext(Consequent value) {
                        final Consequent consequent = new Consequent();

                        if (value.getResult() == 1) {
                            Observable.create(new ObservableOnSubscribe<Consequent>() {
                                @Override
                                public void subscribe(ObservableEmitter<Consequent> thisEmitter) {
                                    thisEmitter.onNext(consequent.setResult(1931)); // 1932 表示编辑 跳转到 编辑记录页面
                                    thisEmitter.onComplete();
                                }
                            }).subscribe(holder.observer);

                        } else {
                            /* 报错信息暂不处理 */
                        }

                    }

                    @Override
                    public void onError(Throwable e) { /* 报错信息暂不处理 */ }

                    @Override
                    public void onComplete() { /* 不需要处理 */ }
                };

                RxPost httpRxGet = new RxPost(context, "/android/recordevent/del", submitData.toJSONString());
                httpRxGet.observable().subscribe(observer);

            }
        }

        holder.recordDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                new DelItemById();
            }
        });

        holder.eventDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                new DelItemById();
            }
        });
    }

    /**
     * 绑定 编辑
     */
    private void initEdit(int i, final ViewHolder holder) {
        final RecordFragmentListDate item = listData.get(i);

        class JumpToRecord {
            JumpToRecord() {
                Log.d("跳转", "JumpToRecord");
                final Consequent consequent = new Consequent();

                consequent.setData(JSON.parseObject(JSON.toJSONString(item))).setResult(1932); // 1932 表示编辑 跳转到 编辑记录页面

                Observable.create(new ObservableOnSubscribe<Consequent>() {
                    @Override
                    public void subscribe(ObservableEmitter<Consequent> thisEmitter) {
                        thisEmitter.onNext(consequent);
                        thisEmitter.onComplete();
                    }
                }).subscribe(holder.observer);
            }
        }
        class JumpToEvent {
            JumpToEvent() {
                Log.d("跳转", "JumpToEvent");
                final Consequent consequent = new Consequent();

                consequent.setData(JSON.parseObject(JSON.toJSONString(item))).setResult(1933); // 1933 表示编辑 跳转到 编辑事件页面

                Observable.create(new ObservableOnSubscribe<Consequent>() {
                    @Override
                    public void subscribe(ObservableEmitter<Consequent> thisEmitter) {
                        thisEmitter.onNext(consequent);
                        thisEmitter.onComplete();
                    }
                }).subscribe(holder.observer);
            }
        }

        if (item.getType().equals("record")) {
            holder.recordImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisView) {
                    new JumpToRecord();
                }
            });
            holder.recordTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisView) {
                    new JumpToRecord();
                }
            });
            holder.recordThink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisView) {
                    new JumpToRecord();
                }
            });
            holder.recordContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisView) {
                    new JumpToRecord();
                }
            });


        } else {
            holder.eventImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisView) {
                    new JumpToEvent();
                }
            });
            holder.eventCause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisView) {
                    new JumpToEvent();
                }
            });
            holder.eventHandle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisView) {
                    new JumpToEvent();
                }
            });
            holder.eventResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisView) {
                    new JumpToEvent();
                }
            });
            holder.eventConclusion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisView) {
                    new JumpToEvent();
                }
            });

        }

    }
}

