package cn.rejiejay.application.tabselect;

/**
 * 数据Model
 * 自定义的Adapter依然遵循MVC设计模式
 */
public class Tab {
    // 构造函数
    public Tab(String name, String image) {
        this.name = name;
        this.image = image;
    }

    private String name; // 标签 名称
    private String image; // 标签 图片

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
