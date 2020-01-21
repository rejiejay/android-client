package cn.rejiejay.application.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import mehdi.sakout.fancybuttons.FancyButton;
import cn.rejiejay.application.component.AutoHeightListView;
import io.reactivex.Observer;
import cn.rejiejay.application.utils.Consequent;

import cn.rejiejay.application.R;

public class TaskFragment extends Fragment {
    private Context mContext;

    public FancyButton loadMore; // 加载更多

    public List<TaskFragmentListDate> listData = new ArrayList<>();

    public TaskListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();

        /*
         * 绑定 layout
         */
        return inflater.inflate(R.layout.main_task, container, false);
    }

    /**
     * 初始化
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // 初始化 绑定 组件的方法
        initPageComponent(view);

        // listView
        initListViewComponent(view);
    }

    /**
     * 初始化 绑定 组件的方法
     */
    public void initPageComponent(View view) {
        loadMore = view.findViewById(R.id.task_load_more); // 加载更多

        // 假数据记得删除
        listData.add(new TaskFragmentListDate());
        listData.add(new TaskFragmentListDate());
    }

    /**
     * 初始化 listView
     */
    public void initListViewComponent(View view) {
        AutoHeightListView listViewComponent = view.findViewById(R.id.task_list_view);

        /**
         * Observer
         * 作用: 注册 RxAndroid 进行页面通信
         */
        Observer<Consequent> observer = new Observer<Consequent>() {
            @Override
            public void onSubscribe(Disposable d) { /** 不需要处理 */}

            @Override
            public void onNext(Consequent value) {/** 暂不处理 */}

            @Override
            public void onError(Throwable e) {/** 暂不处理 */}

            @Override
            public void onComplete() { /** 不需要处理 */}
        };

        mAdapter = new TaskListAdapter(mContext, listViewComponent, listData, observer);
        listViewComponent.setAdapter(mAdapter);
    }
}

