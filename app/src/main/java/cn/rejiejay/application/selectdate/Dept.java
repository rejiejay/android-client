package cn.rejiejay.application.selectdate;

/**
 * Created by HQOCSHheqing on 2016/8/2.
 * https://github.com/heqinghqocsh/TreeView
 *
 * @description 部门类（继承Node），此处的泛型Integer是因为ID和parentID都为int
 * ，如果为String传入泛型String即可
 */
public class Dept extends Node<Integer> {

    private int id; // 日期唯一标识
    private int parentId; // 父亲节点ID
    private String name; // 日期名称
    private long minTimestamp; // 最小 时间戳
    private long maxTimestamp; // 最大 时间戳

    public Dept() {
    }

    public Dept(int id, int parentId, String name, long minTimestamp, long maxTimestamp) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.minTimestamp = minTimestamp;
        this.maxTimestamp = maxTimestamp;
    }

    /**
     * 此处返回节点ID
     *
     * @return
     */
    @Override
    public Integer get_id() {
        return id;
    }

    /**
     * 此处返回父亲节点ID
     *
     * @return
     */
    @Override
    public Integer get_parentId() {
        return parentId;
    }

    @Override
    public String get_label() {
        return name;
    }

    @Override
    public boolean parent(Node dest) {
        if (id == ((Integer) dest.get_parentId()).intValue()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean child(Node dest) {
        if (parentId == ((Integer) dest.get_id()).intValue()) {
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMinTimestamp() {
        return minTimestamp;
    }

    public void setMinTimestamp(long minTimestamp) {
        this.minTimestamp = minTimestamp;
    }

    public long getMaxTimestamp() {
        return maxTimestamp;
    }

    public void setMaxTimestamp(long maxTimestamp) {
        this.maxTimestamp = maxTimestamp;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", minTimestamp=" + minTimestamp +
                ", maxTimestamp=" + maxTimestamp +
                '}';
    }
}
