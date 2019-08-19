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

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.io.File;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


public class RecordEventActivity extends AppCompatActivity {
    private Context mContext;
    private String previewRecordImageImageSrc; // null 表示未上传图片
    ImageView previewRecordImage;

    /**
     * 页面状态
     * record 记录页面 event 事件页面
     */
    String pageType = "record";

    QMUISpanTouchFixTextView recordTabView;
    QMUISpanTouchFixTextView eventTabView;

    // 生命周期的 onCreate 周期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordevent);

        mContext = this;

        // 取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            pageType = intent.getStringExtra("type");
        }

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
        recordTabView = findViewById(R.id.record_event_tab_left);
        eventTabView = findViewById(R.id.record_event_tab_right);
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
        FancyButton eventSelectTab = findViewById(R.id.record_event_select_tab);

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
                 * 这个是旋转图片的
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
            Log.d("标签 选择", result);
        }
    }

    // 初始化 确认
    private void initConfirmSubmit() {
        FancyButton recordConfirm = findViewById(R.id.record_event_record_confirm);

        final EditText eventEditThought = findViewById(R.id.record_event_edit_thought);
        eventEditThought.setText(Html.fromHtml("")); // Html 转为 EditText

        recordConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View thisView) {
                /**
                 * 将 EditText 转为 Html
                 */
                String htmlString = Html.toHtml(eventEditThought.getText());
            }
        });

    }
}
