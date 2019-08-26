package cn.rejiejay.application.selecttab;

/**
 * 数据Model
 * 自定义的Adapter依然遵循MVC设计模式
 */
public class Tab {

    // 构造函数
    public Tab(int id, String name, Boolean isDelete) {
        this.id = id;
        this.name = name;
        this.isDelete = isDelete;
    }

    private int id; // 标签 唯一标识

    private String name; // 标签 名称

    private Boolean isDelete; // 是否删除

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    @Override
    public String toString() {
        return "Tab{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isDelete=" + isDelete +
                '}';
    }
}
