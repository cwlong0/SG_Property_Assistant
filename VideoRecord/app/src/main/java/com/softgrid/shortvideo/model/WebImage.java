package com.softgrid.shortvideo.model;

/**
 * Created by tianfeng on 2018/6/29.
 */

public class WebImage {

    private String url;
    private int width;
    private int height;

    public WebImage(boolean test){
        if (test){
            url = "http://fdfs.xmcdn.com/group36/M07/27/2D/wKgJTVpCItDA9iSGAAFoEwpEI48032_ios_x_large.png";
            width = 120;
            height = 85;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
