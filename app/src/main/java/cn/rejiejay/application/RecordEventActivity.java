package cn.rejiejay.application;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


public class RecordEventActivity extends AppCompatActivity {
    private Context mContext;
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

        initUploadView(); // 初始化上传图片
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

        if (pageType.equals("record")) {
            // 记录
            recordTabView.setTextColor(Color.rgb(255, 255, 255)); // 记录的文字是白色
            recordTabView.setBackgroundColor(Color.rgb(33, 150, 243)); // 背景是蓝色
            eventTabView.setTextColor(Color.rgb(144, 147, 153));
            eventTabView.setBackgroundColor(Color.rgb(255, 255, 255));

        } else {
            // 事件
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
     * 初始化上传图片
     * <p>
     * https://github.com/LuckSiege/PictureSelector
     */
    private void initUploadView() {
        FancyButton imageBtn = findViewById(R.id.record_event_image_btn);

        imageBtn.setOnClickListener(new View.OnClickListener() {
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
    }

    /**
     * 图片上传回调函数
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
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

                        if (media.getPath() != null) {

                            /**
                             * 缓存清除
                             * 包括裁剪和压缩后的缓存，要在上传成功后调用
                             *
                             */
                            // PictureFileUtils.deleteCacheDirFile(MainActivity.this);
                            // Log.d("media", media.getPath());
                        }
                    }

                    break;
            }
        }
    }
}
