package cn.com.mvvmdemo.news.model;

/**
 * Created by JokerWan on 2018/11/22.
 * Function:
 */

public class ItemNews {

    private String title;
    private String time;
    private String desc;
    private String imgUrl;
    private long totalResultsCount;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTotalResultsCount() {
        return totalResultsCount;
    }

    public void setTotalResultsCount(long totalResultsCount) {
        this.totalResultsCount = totalResultsCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
