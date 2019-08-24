package cn.rejiejay.application.main;

public class RecordFragmentListDate {

    // 公共部分
    private int androidid; // 唯一标识
    private String type; // event record
    private String imageidentity = ""; // 图片标识
    private String imageurl = ""; // 图片地址
    private long timestamp; // 时间
    private int fullyear; // 时间
    private int month; // 时间
    private int week; // 时间
    private String tag; // 标签

    // record
    private String recordtitle = ""; // 标题
    private String recordmaterial = ""; // 素材
    private String recordcontent = ""; // 内容

    // event
    private String eventcause = ""; // 起因
    private String eventprocess = ""; // 过程
    private String eventresult = ""; // 结果
    private String eventconclusion = ""; // 结论

    public int getAndroidid() {
        return androidid;
    }

    public void setAndroidid(int androidid) {
        this.androidid = androidid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageidentity() {
        return imageidentity;
    }

    public void setImageidentity(String imageidentity) {
        this.imageidentity = imageidentity;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getFullyear() {
        return fullyear;
    }

    public void setFullyear(int fullyear) {
        this.fullyear = fullyear;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRecordtitle() {
        return recordtitle;
    }

    public void setRecordtitle(String recordtitle) {
        this.recordtitle = recordtitle;
    }

    public String getRecordmaterial() {
        return recordmaterial;
    }

    public void setRecordmaterial(String recordmaterial) {
        this.recordmaterial = recordmaterial;
    }

    public String getRecordcontent() {
        return recordcontent;
    }

    public void setRecordcontent(String recordcontent) {
        this.recordcontent = recordcontent;
    }

    public String getEventcause() {
        return eventcause;
    }

    public void setEventcause(String eventcause) {
        this.eventcause = eventcause;
    }

    public String getEventprocess() {
        return eventprocess;
    }

    public void setEventprocess(String eventprocess) {
        this.eventprocess = eventprocess;
    }

    public String getEventresult() {
        return eventresult;
    }

    public void setEventresult(String eventresult) {
        this.eventresult = eventresult;
    }

    public String getEventconclusion() {
        return eventconclusion;
    }

    public void setEventconclusion(String eventconclusion) {
        this.eventconclusion = eventconclusion;
    }

    @Override
    public String toString() {
        return "RecordFragmentListDate{" +
                "androidid=" + androidid +
                ", type='" + type + '\'' +
                ", imageidentity='" + imageidentity + '\'' +
                ", timestamp=" + timestamp +
                ", fullyear=" + fullyear +
                ", month=" + month +
                ", week=" + week +
                ", recordtitle='" + recordtitle + '\'' +
                ", recordmaterial='" + recordmaterial + '\'' +
                ", recordcontent='" + recordcontent + '\'' +
                ", eventcause='" + eventcause + '\'' +
                ", eventprocess='" + eventprocess + '\'' +
                ", eventresult='" + eventresult + '\'' +
                ", eventconclusion='" + eventconclusion + '\'' +
                '}';
    }
}

