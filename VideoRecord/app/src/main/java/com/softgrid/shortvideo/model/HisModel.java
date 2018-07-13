package com.softgrid.shortvideo.model;

/**
 * Created by tianfeng on 2017/9/16.
 */

public class HisModel {

    public static final int HIS_BUILDING = 1;

    private long id;                    //数据库主键
    private String hisCreatedTime;     //创建时间
    private int hisType;               //历史类型
    private String hisId;                 //内容id
    private int hisUpdated;            //是否有更新
    private String hisUpdatedTime;     //最后更新时间
    private int hisPlayedTime;         //历史中子内容的播放了的时间
    private String hisContent;         //历史内容
    private String hisSubContent;      //历史内容的子内容

    private Object hisContentModel;      //历史内容Model
    private Object hisSubContentModel;   //历史内容的子内容Model

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHisCreatedTime() {
        return hisCreatedTime;
    }

    public void setHisCreatedTime(String hisCreatedTime) {
        this.hisCreatedTime = hisCreatedTime;
    }

    public int getHisType() {
        return hisType;
    }

    public void setHisType(int hisType) {
        this.hisType = hisType;
    }

    public String getHisId() {
        return hisId;
    }

    public void setHisId(String hisId) {
        this.hisId = hisId;
    }

    public int getHisUpdated() {
        return hisUpdated;
    }

    public void setHisUpdated(int hisUpdated) {
        this.hisUpdated = hisUpdated;
    }

    public String getHisUpdatedTime() {
        return hisUpdatedTime;
    }

    public void setHisUpdatedTime(String hisUpdatedTime) {
        this.hisUpdatedTime = hisUpdatedTime;
    }

    public int getHisPlayedTime() {
        return hisPlayedTime;
    }

    public void setHisPlayedTime(int hisPlayedTime) {
        this.hisPlayedTime = hisPlayedTime;
    }

    public String getHisContent() {
        return hisContent;
    }

    public void setHisContent(String hisContent) {
        this.hisContent = hisContent;
    }

    public String getHisSubContent() {
        return hisSubContent;
    }

    public void setHisSubContent(String hisSubContent) {
        this.hisSubContent = hisSubContent;
    }

    public Object getHisContentModel() {
        return hisContentModel;
    }

    public void setHisContentModel(Object hisContentModel) {
        this.hisContentModel = hisContentModel;
    }

    public Object getHisSubContentModel() {
        return hisSubContentModel;
    }

    public void setHisSubContentModel(Object hisSubContentModel) {
        this.hisSubContentModel = hisSubContentModel;
    }
}
