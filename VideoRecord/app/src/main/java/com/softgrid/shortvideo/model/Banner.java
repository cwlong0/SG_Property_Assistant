package com.softgrid.shortvideo.model;

/**
 * Created by tianfeng on 2018/7/10.
 */

public class Banner {

    private String bannerId;
    private int bannerType;
    private String bannerUrl;
    private Object bannerModel;

    public Banner(boolean test) {
        if (test) {
            bannerType = 1;
            bannerUrl = "http://fdfs.xmcdn.com/group36/M07/27/2D/wKgJTVpCItDA9iSGAAFoEwpEI48032_ios_x_large.png";
            bannerModel = new Building(true);
        }
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public Object getBannerModel() {
        return bannerModel;
    }

    public void setBannerModel(Object bannerModel) {
        this.bannerModel = bannerModel;
    }
}
