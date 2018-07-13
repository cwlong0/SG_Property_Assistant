package com.softgrid.shortvideo.model;

/**
 * Created by tianfeng on 2018/6/5.
 */

public class TransactionStatus {

    private String id;
    private int code;
    private String desc;
    private int haveLawyer;
    private int isPayTaxes;
    private int isPayEnd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setIsPayTaxes(int isPayTaxes) {
        this.isPayTaxes = isPayTaxes;
    }

    public int getIsPayEnd() {
        return isPayEnd;
    }

    public void setIsPayEnd(int isPayEnd) {
        this.isPayEnd = isPayEnd;
    }
}
