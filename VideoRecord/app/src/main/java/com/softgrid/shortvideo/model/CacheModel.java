package com.softgrid.shortvideo.model;

/**
 * Created by tianfeng on 2017/9/16.
 */

public class CacheModel {

    private int id;                   //数据库主键
    private String  act;              //缓存接口
    private String  params;           //参数
    private String  content;          //内容

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
