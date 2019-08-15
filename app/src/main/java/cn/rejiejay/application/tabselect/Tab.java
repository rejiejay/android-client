package cn.rejiejay.application.tabselect;

/**
 * 数据Model
 * 自定义的Adapter依然遵循MVC设计模式
 */
public class Tab {

    // 构造函数
    public Tab(String name, Boolean isDelete) {
        this.name = name;
        this.isDelete = isDelete;
    }

    private String name; // 标签 名称

    private Boolean isDelete; // 是否删除

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
}
