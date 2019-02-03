package com.softgrid.shortvideo.model;

import java.util.List;

/**
 * Created by tianfeng on 2018/6/6.
 */

public class SearchCondition {

    private int isResale;              //是否是二手房
    private int isAuth;                //是否已经通过认证（二手房）
    private int type;                  //房屋类型（1-公寓）
    private int sort;                  //排序
    private String rooms;              //几个房间（1-4）
    private int decorate;              //装修等级
    private int orientation;           //朝向
    private int region;                //所在大区
    private int pd;                    //所在区
    private float longitude;           //经度
    private float latitude;            //纬度
    private float range;               //配合经纬度使用，以经纬度为中心的范围
    private String area;               //面积范围
    private String unitPrice;          //单价范围
    private String totalPrice;         //总价范围
    private List<Tag> featureTags;     //特色标签


    public int getIsResale() {
        return isResale;
    }

    public void setIsResale(int isResale) {
        this.isResale = isResale;
    }

    public int getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(int isAuth) {
        this.isAuth = isAuth;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public int getDecorate() {
        return decorate;
    }

    public void setDecorate(int decorate) {
        this.decorate = decorate;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getPd() {
        return pd;
    }

    public void setPd(int pd) {
        this.pd = pd;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Tag> getFeatureTags() {
        return featureTags;
    }

    public void setFeatureTags(List<Tag> featureTags) {
        this.featureTags = featureTags;
    }
}
