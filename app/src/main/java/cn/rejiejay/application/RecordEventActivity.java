package cn.rejiejay.application;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.io.File;
import java.util.Date;
import java.util.List;

import cn.rejiejay.application.component.RxPost;
import cn.rejiejay.application.utils.Consequent;
import cn.rejiejay.application.utils.DateFormat;
import cn.rejiejay.application.utils.UIoperate;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import mehdi.sakout.fancybuttons.FancyButton;


public class RecordEventActivity extends AppCompatActivity {
    private Context mContext;
    private int androidid;
    // 图片
    private String imageidentity = ""; // 图片唯一标识
    private String previewRecordImageImageSrc = ""; // null 表示未上传图片
    private ImageView previewRecordImage;
    // 标签
    private FancyButton eventSelectTab;
    private String Tab = "";
    // 记录编辑
    private EditText recordTitleEdit;
    private String recordTitle = "";
    private EditText recordThoughtEdit;
    private String recordThought = "";
    private EditText recordContentEdit;
    private String recordContent = "";
    private FancyButton recordConfirm;
    // 事件编辑
    private EditText eventCauseEdit;
    private EditText eventProcessEdit;
    private EditText eventResultEdit;
    private EditText eventConclusionEdit;
    private FancyButton eventConfirm;

    // 页面状态
    Boolean isEdit = false; // 是否编辑状态
    String pageType = "record"; // record 记录页面 event 事件页面
    QMUISpanTouchFixTextView recordTabView;
    QMUISpanTouchFixTextView eventTabView;


    // 生命周期的 onCreate 周期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordevent);

        mContext = this;

        initComponent(); // 初始化頁面组件

        initPageType(); // 根据 pageType 改变页面状态
        initTopTab(); // 初始化 顶部

        initSelectTab(); // 初始化选择分类

        initUploadView(); // 初始化上传图片

        initConfirmSubmit(); // 初始化 确认
    }

    /**
     * 初始化頁面组件
     */
    public void initComponent() {
        // 取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();

        recordTabView = findViewById(R.id.record_event_tab_left);
        eventTabView = findViewById(R.id.record_event_tab_right);
        eventSelectTab = findViewById(R.id.record_event_select_tab);

        // 记录编辑
        recordTitleEdit = findViewById(R.id.record_event_record_edit_title);
        recordThoughtEdit = findViewById(R.id.record_event_record_edit_thought);
        recordContentEdit = findViewById(R.id.record_event_record_edit_content);
        recordTitleEdit.setText(recordTitle);
        recordThoughtEdit.setText(recordThought);
        recordContentEdit.setText(recordContent);
        recordConfirm = findViewById(R.id.record_event_record_confirm);


        // 事件编辑
        eventCauseEdit = findViewById(R.id.record_event_event_edit_cause);
        eventProcessEdit = findViewById(R.id.record_event_event_edit_process);
        eventResultEdit = findViewById(R.id.record_event_event_edit_result);
        eventConclusionEdit = findViewById(R.id.record_event_event_edit_conclusion);
        eventConfirm = findViewById(R.id.record_event_event_confirm);

        if (intent != null) { // 从Intent当中根据key取得value
            pageType = intent.getStringExtra("type");
            isEdit = intent.getBooleanExtra("isEdit", false); // 获取失败默认就是否

            String editRecordJsonStr = intent.getStringExtra("editRecordJSON");
            if (editRecordJsonStr != null && editRecordJsonStr.length() > 0) {
                JSONObject editRecordJSON = JSON.parseObject(editRecordJsonStr);

                androidid = editRecordJSON.getIntValue("androidid");
                Tab = editRecordJSON.getString("tag");

                // 图片的晚点再搞，这个有点复杂
                // imageidentity = editRecordJSON.getString("imageidentity");

                recordTitle = editRecordJSON.getString("recordtitle");
                recordThought = editRecordJSON.getString("recordmaterial");
                recordContent = editRecordJSON.getString("recordcontent");
            }
        }

        if (isEdit) {
            recordConfirm.setText("编辑");
            eventConfirm.setText("编辑");
        }
    }

    /**
     * 改变页面状态
     */
    public void initPageType() {
        LinearLayout contentLeft = findViewById(R.id.record_event_content_left);
        LinearLayout contentRight = findViewById(R.id.record_event_content_right);

        if (pageType.equals("record")) {
            // 记录
            contentLeft.setVisibility(View.VISIBLE);
            contentRight.setVisibility(View.GONE);
            recordTabView.setTextColor(Color.rgb(255, 255, 255)); // 记录的文字是白色
            recordTabView.setBackgroundColor(Color.rgb(33, 150, 243)); // 背景是蓝色
            eventTabView.setTextColor(Color.rgb(144, 147, 153));
            eventTabView.setBackgroundColor(Color.rgb(255, 255, 255));

        } else {
            // 事件
            contentLeft.setVisibility(View.GONE);
            contentRight.setVisibility(View.VISIBLE);
            recordTabView.setTextColor(Color.rgb(144, 147, 153));
            recordTabView.setBackgroundColor(Color.rgb(255, 255, 255));
            eventTabView.setTextColor(Color.rgb(255, 255, 255)); // 事件的文字是白色
            eventTabView.setBackgroundColor(Color.rgb(33, 150, 243)); // 背景是蓝色

        }
    }

    /**
     * 初始化顶部
     */
    public void initTopTab() {
        recordTabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                pageType = "record";
                initPageType();
            }
        });

        eventTabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                pageType = "event";
                initPageType();
            }
        });
    }


    /**
     * 初始化 选择标签
     */
    public void initSelectTab() {
        eventSelectTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                Intent intent = new Intent();
                intent.setClass(RecordEventActivity.this, SelectTabActivity.class);

                startActivityForResult(intent, 20132);
            }
        });
    }

    /**
     * 初始化上传图片
     * <p>
     * https://github.com/LuckSiege/PictureSelector
     */
    private void initUploadView() {
        FancyButton recordImageBtn = findViewById(R.id.record_event_record_image_btn);
        previewRecordImage = findViewById(R.id.preview_record_image);

        recordImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                PictureSelector.create(RecordEventActivity.this)
                        .openGallery(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .maxSelectNum(1) // 最大图片选择数量 int
                        .previewImage(true) // 是否可预览图片 true or false
                        .isCamera(true) // 是否显示拍照按钮 true or false
                        .compress(true) // 是否压缩 true or false
                        .minimumCompressSize(300)// 小于300kb的图片不压缩
                        .isGif(false) // 是否显示gif图片 true or false
                        .forResult(PictureConfig.CHOOSE_REQUEST); // 结果回调onActivityResult code
            }
        });

        previewRecordImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("删除");
                builder.setMessage("你确定要删除这张图片?");
                builder.setIcon(R.mipmap.ic_launcher_round);
                // 点击对话框以外的区域是否让对话框消失
                builder.setCancelable(true);
                // 设置正面按钮
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        previewRecordImageImageSrc = null;
                        previewRecordImage.setVisibility(View.GONE);
                    }
                });
                // 设置反面按钮
                builder.setNegativeButton("不是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                //对话框显示的监听事件
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                    }
                });
                //对话框消失的监听事件
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                //显示对话框
                dialog.show();
            }
        });
    }

    /**
     * 图片上传回调函数
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                /**
                 * 这个是选择图片的
                 */
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    /**
                     * 结果必须要大于零
                     */
                    if (selectList.size() > 0) {
                        LocalMedia media = selectList.get(0);
                        previewRecordImageImageSrc = media.getPath();
                        if (previewRecordImageImageSrc != null) {

                            /**
                             * 缓存清除
                             * 包括裁剪和压缩后的缓存，要在上传成功后调用
                             *
                             */
                            previewRecordImage.setImageURI(Uri.fromFile(new File(previewRecordImageImageSrc)));
                            previewRecordImage.setVisibility(View.VISIBLE);
                            // PictureFileUtils.deleteCacheDirFile(MainActivity.this);
                            // Log.d("media", media.getPath());
                        }
                    }

                    break;
            }
        }

        /**
         * 这个是标签 选择
         */
        if (resultCode == 20132) {
            String result = data.getStringExtra("tab");
            Tab = result;
            eventSelectTab.setText(result);
        }
    }

    // 初始化 确认
    private void initConfirmSubmit() {
        recordConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                recordDataSubmit();
            }
        });


        // 事件
//        FancyButton eventConfirm = findViewById(R.id.record_event_event_confirm);
//
//        final EditText eventEditThought = findViewById(R.id.record_event_edit_thought);
//        eventEditThought.setText(Html.fromHtml("")); // Html 转为 EditText
//
//        eventConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View thisView) {
//                /**
//                 * 将 EditText 转为 Html
//                 */
//                String htmlString = Html.toHtml(eventEditThought.getText());
//            }
//        });

    }

    // 新增记录
    private void recordDataSubmit() {
        JSONObject submitData = new JSONObject();
        DateFormat thisDate = new DateFormat();

        // 获取标签
        submitData.put("tag", Tab);

        // 获取时间戳
        submitData.put("timestamp", new Date().getTime());

        // 年
        submitData.put("fullyear", thisDate.getFullYear());

        // 月
        submitData.put("month", thisDate.getMonth());

        // 日
        submitData.put("week", thisDate.getWeekInMonth());

        // 图片
        if (previewRecordImageImageSrc != null && previewRecordImageImageSrc.length() > 0) {
            submitData.put("imageidentity", previewRecordImageImageSrc);
        }

        // 标题
        String recordtitle = recordTitleEdit.getText().toString();
        if (recordtitle.equals("") || recordtitle.length() == 0) {
            recordtitle = thisDate.getYYmmDDww();
        }
        submitData.put("recordtitle", recordtitle);

        // 素材
        submitData.put("recordmaterial", recordThoughtEdit.getText().toString());

        // 内容
        String recordcontentStr = recordContentEdit.getText().toString();
        if (recordcontentStr.equals("") || recordcontentStr.length() == 0) {
            UIoperate.showErrorModal(mContext, "提示", "内容不能为空");
            return;
        }
        submitData.put("recordcontent", recordcontentStr);

        Log.d("submitData", submitData.toJSONString());

        Observer<Consequent> observer = new Observer<Consequent>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Consequent value) {
                if (value.getResult() == 1) {
                    Intent intent = new Intent();
                    setResult(32201, intent);
                    finish();
                } else {
                    // 弹出重新加载（暂不实现
                }

            }

            @Override
            public void onError(Throwable e) {
                // 弹出重新加载（暂不实现
            }

            @Override
            public void onComplete() {
            }
        };

        RxPost httpRxGet = new RxPost(mContext, "/android/record/add", submitData.toJSONString());
        httpRxGet.observable().subscribe(observer);
    }
}
