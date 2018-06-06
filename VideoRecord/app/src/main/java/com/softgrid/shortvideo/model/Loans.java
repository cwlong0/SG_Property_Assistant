package com.softgrid.shortvideo.model;

/**
 * Created by tianfeng on 2018/6/5.
 */

public class Loans {


    private String id;
    private int code;
    private String desc;
    private float total;
    private int year;
    private Transaction transaction;
    private User banker;
    private int haveBanker;
    private int isUploadDoc;
    private int isGet;
    private long createAt;
    private long updateAt;

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

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public User getBanker() {
        return banker;
    }

    public void setBanker(User banker) {
        this.banker = banker;
    }

    public int getHaveBanker() {
        return haveBanker;
    }

    public void setHaveBanker(int haveBanker) {
        this.haveBanker = haveBanker;
    }

    public int getIsUploadDoc() {
        return isUploadDoc;
    }

    public void setIsUploadDoc(int isUploadDoc) {
        this.isUploadDoc = isUploadDoc;
    }

    public int getIsGet() {
        return isGet;
    }

    public void setIsGet(int isGet) {
        this.isGet = isGet;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }
}
