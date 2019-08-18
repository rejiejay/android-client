package cn.rejiejay.application.main;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;

import cn.rejiejay.application.R;

/**
 * 选择第二个构造方法，使得在XML中可以使用
 */
public class ActivityTabButton extends FrameLayout {

    /**
     * 这两个就是组件里面的东西
     */
    public TextView tabNameView;
    public ImageView mImageView;

    public ActivityTabButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**
         * inflate 大概作用是与自己写的XML文件互相匹配(绑定
         * 意思就是说当作一个JAVA组件来用（类似于JS组件
         */
        inflate(context, R.layout.main_tab_button, this);

        /**
         * 在构造函数里面去到值
         */
        tabNameView = findViewById(R.id.textView);
        mImageView = findViewById(R.id.imageView);
    }

    /**
     * 这个组件对外提供一个方法(当然内部也可以调用
     * 就是设置文字和下边的点点是否选中
     *
     * @param isSelected 是否选中
     */
    public void setSelected(boolean isSelected) {
        /**
         * 选中的状态下
         */
        if (isSelected) {
            // 文字就设置为粗体
            tabNameView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            mImageView.setVisibility(View.VISIBLE);

            /**
             * 下面是渐变动画(但是动画生效，具体原因未知
             * 图片透明度为不透明，也就是由0到1
             */
            // AlphaAnimation animation = new AlphaAnimation(0f, 1.0f);
            // mImageView.startAnimation(animation);
            // animation.setFillAfter(true); // 动画结束后保持状态
            // animation.setDuration(500); // 动画持续时间，单位为毫秒
        } else {
            // 文字就设置为不加粗体
            tabNameView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            mImageView.setVisibility(View.INVISIBLE);

            /**
             * 为选中的情况下是不显示，也就是1到0
             */
            // AlphaAnimation animation = new AlphaAnimation(1.0f, 0f);
            // mImageView.startAnimation(animation);
            // animation.setFillAfter(true); // 动画结束后保持状态
            // animation.setDuration(500); // 动画持续时间，单位为毫秒
        }
    }

    /**
     * 因为文本内容是需要从外部初始化进来的
     */
    public void setTextContentAndAlpha(boolean isSelected, String textContent) {
        tabNameView.setText(textContent);
        setSelected(isSelected);
    }
}


































































