package com.softgrid.shortvideo.model;

import java.util.Random;

/**
 * Created by tianfeng on 2018/6/29.
 */

public class Tag {

    private String id;
    private String title;
    private String desc;

    public Tag(boolean test){
        if (test){
            int random = new Random().nextInt(100);
            if (random < 30){
                title = "学区房";
                desc = "周边四所小学，五所中学，两所大学！";
            }
            else if (random > 60){
                title = "地铁房";
                desc = "紧靠地铁三号线，5号线！";
            }
            else {
                title = "配套成熟";
                desc = "紧靠地铁三号线，5号线！紧靠伊藤！";
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
