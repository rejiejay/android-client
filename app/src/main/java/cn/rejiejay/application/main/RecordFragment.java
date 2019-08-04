package cn.rejiejay.application.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.rejiejay.application.R;

public class RecordFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * 绑定 layout
         */
        return inflater.inflate(R.layout.activity_main_fragment_record, container, false);
    }

    /**
     * 这里加载图片会报错
     */
    // @Override
    // public void onCreate(@Nullable Bundle savedInstanceState) {
    //     super.onCreate(savedInstanceState);
    // }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String url = "https://rejiejay-1251940173.cos.ap-guangzhou.myqcloud.com/myweb/mobile-list/articles-1.png";
        ImageView myImage = view.findViewById(R.id.record_Item_img);
        ImageView myImage2 = view.findViewById(R.id.record_Item_img2);
        Glide.with(this)
                .load(url)
                .into(myImage);
        Glide.with(this)
                .load(url)
                .into(myImage2);
    }

}

