package cn.rejiejay.application.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.rejiejay.application.R;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * 下面这段话就是设置fragment
         * 第二个参数是设置到哪里去
         */
        return inflater.inflate(R.layout.activity_main_fragment_home, container, false);
    }
}
