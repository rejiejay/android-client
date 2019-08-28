package cn.rejiejay.application.selectdate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.util.LinkedList;
import java.util.List;

import cn.rejiejay.application.R;
import cn.rejiejay.application.utils.Consequent;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;

/**
 * Created by HQOCSHheqing on 2016/8/3.
 * https://github.com/heqinghqocsh/TreeView
 *
 * @description 适配器类，就是 listView 最常见的适配器写法
 */
public class NodeTreeAdapter extends BaseAdapter {
    private Context context;

    /**
     * nodeLinkedList 就是数据源
     * <p>
     * 大家经常用ArrayList，但是这里为什么要使用LinkedList
     * 后面大家会发现因为这个list会随着用户展开、收缩某一项而频繁的进行增加、删除元素操作，
     * 因为ArrayList是数组实现的，频繁的增删性能低下，而LinkedList是链表实现的，对于频繁的增删
     * 操作性能要比ArrayList好。
     */
    private LinkedList<Node> nodeLinkedList;
    private LayoutInflater inflater;
    private int retract; // 缩进值

    // RxAndroid 进行页面通信
    private Observer<Consequent> observer;

    public NodeTreeAdapter(Context context, ListView listView, LinkedList<Node> linkedList, Observer<Consequent> observer) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        nodeLinkedList = linkedList;
        this.observer = observer;

        /**
         * 绑定点击事件
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expandOrCollapse(position); // 展开或收缩用户点击的条目
            }
        });

        // 缩进值，大家可以将它配置在资源文件中，从而实现适配
        retract = (int) (context.getResources().getDisplayMetrics().density * 10 + 0.5f);
    }

    /**
     * 展开或收缩用户点击的条目
     *
     * @param position
     */
    private void expandOrCollapse(int position) {
        Node node = nodeLinkedList.get(position);

        /*
         * 判断节点是否为空 isLeaf 也是判空的作用
         */
        if (node != null && !node.isLeaf()) {
            boolean old = node.isExpand();
            /*
             * 判断是否展开
             * 展开与否 与 icon箭头 方向有关
             */
            if (old) {
                /*
                 * 表示 展开的节点 要收起来
                 */
                List<Node> nodeList = node.get_childrenList();
                int size = nodeList.size();
                Node tmp = null;
                for (int i = 0; i < size; i++) {
                    tmp = nodeList.get(i);
                    if (tmp.isExpand()) {
                        collapse(tmp, position + 1);
                    }
                    nodeLinkedList.remove(position + 1);
                }
            } else {
                /*
                 * 表示 不展开的节点 要展开
                 */
                nodeLinkedList.addAll(position + 1, node.get_childrenList());
            }

            // 切换一下状态
            node.setIsExpand(!old);

            // 重新执行一次 getView
            notifyDataSetChanged();
        }
    }

    /**
     * 通过递归 实现 节点收起
     * <p>
     * 思路:
     * 当用户展开某一条时，就将该条对应的所有子节点加入到nodeLinkedList，
     * 同时控制缩进，当用户收缩某一条时，就将该条所对应的子节点全部删除。
     * <p>
     * 而当用户跨级缩进时，就需要递归缩进其所有的孩子节点，这样才能保持整个nodeLinkedList的正确性
     * 同时这种实现方式避免了每次对所有数据进行处理然后插入到一个list，最后显示出来，当数据量一大，就会卡顿， 所以这种只改变局部数据的方式性能大大提高。
     */
    private void collapse(Node node, int position) {
        node.setIsExpand(false);
        List<Node> nodes = node.get_childrenList();
        int size = nodes.size();
        Node tmp = null;
        for (int i = 0; i < size; i++) {
            tmp = nodes.get(i);
            if (tmp.isExpand()) {
                // 这里进行递归
                collapse(tmp, position + 1);
            }
            nodeLinkedList.remove(position + 1);
        }
    }

    @Override
    public int getCount() {
        return nodeLinkedList.size();
    }

    @Override
    public Object getItem(int position) {
        return nodeLinkedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.selecttdate_item, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.id_tree_node_icon);
            holder.label = convertView.findViewById(R.id.id_tree_node_label);
            holder.confirm = convertView.findViewById(R.id.id_confirm);

            /*
             * 什么是setTag
             *
             * Tag从本质上来讲是就是相关联的view的额外的信息。
             * 经常用来存储一些view的数据，这样做非常方便而不用存入另外的单独结构。
             */
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Node node = nodeLinkedList.get(position);
        /*
         * get_label 其实是一个 String 字符串
         * 设置文字
         */
        holder.label.setText(node.get_label());

        /*
         * 这里是设置icon显示
         */
        if (node.get_icon() == -1) {
            holder.imageView.setVisibility(View.INVISIBLE);
        } else {
            holder.imageView.setImageResource(node.get_icon());
        }

        /*
         * 绑定点击事件
         *
         * 但是不知道为啥setTag
         */
        holder.confirm.setTag(position);
        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag();
                final Dept item = (Dept) nodeLinkedList.get(index);

                Observable.create(new ObservableOnSubscribe<Consequent>() {
                    @Override
                    public void subscribe(ObservableEmitter<Consequent> thisEmitter) {
                        Consequent consequent = new Consequent();
                        JSONObject selectedData = new JSONObject();
                        selectedData.put("name", item.getName());
                        selectedData.put("minTimestamp", item.getMinTimestamp());
                        selectedData.put("maxTimestamp", item.getMaxTimestamp());
                        consequent.setSuccess(selectedData);
                        thisEmitter.onNext(consequent);
                        thisEmitter.onComplete();
                    }
                }).subscribe(observer);

            }
        });

        /*
         * 这里是设置缩进
         */
        convertView.setPadding(node.get_level() * retract, 5, 5, 5);

        return convertView;
    }

    /**
     * 三个控件
     */
    static class ViewHolder {
        public ImageView imageView;
        public TextView label;
        public ImageView confirm;
    }

}