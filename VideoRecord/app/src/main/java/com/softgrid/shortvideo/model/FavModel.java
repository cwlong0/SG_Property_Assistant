package com.softgrid.shortvideo.model;

/**
 * Created by tianfeng on 2017/9/16.
 */

public class FavModel {

    public static final int FAV_BUILDING = 1;

    private long id;                   //数据库主键
    private String favId;             //收藏内容ID
    private int type;                 //收藏内容
    private int favUpdated;           //是否有更新
    private String favUpdatedTime;    //最后更新时间
    private String favCreatedTime;    //收藏时间
    private String favContent;        //收藏内容
    private Object favContentModel;   //收藏内容Model


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFavId() {
        return favId;
    }

    public void setFavId(String favId) {
        this.favId = favId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFavUpdated() {
        return favUpdated;
    }

    public void setFavUpdated(int favUpdated) {
        this.favUpdated = favUpdated;
    }

    public String getFavUpdatedTime() {
        return favUpdatedTime;
    }

    public void setFavUpdatedTime(String favUpdatedTime) {
        this.favUpdatedTime = favUpdatedTime;
    }

    public String getFavCreatedTime() {
        return favCreatedTime;
    }

    public void setFavCreatedTime(String favCreatedTime) {
        this.favCreatedTime = favCreatedTime;
    }

    public String getFavContent() {
        return favContent;
    }

    public void setFavContent(String favContent) {
        this.favContent = favContent;
    }

    public Object getFavContentModel() {
        return favContentModel;
    }

    public void setFavContentModel(Object favContentModel) {
        this.favContentModel = favContentModel;
    }
}
