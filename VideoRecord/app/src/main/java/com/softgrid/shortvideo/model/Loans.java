package com.softgrid.shortvideo.model;

import java.util.Random;

/**
 * Created by tianfeng on 2018/6/5.
 */

public class Loans {


    private String id;
    private int code;
    private String desc;
    private float total;
    private int year;
    private Building building;
    private Transaction transaction;
    private User banker;
    private long requestTime;
    private long createAt;
    private long updateAt;

    public Loans(){}

    public Loans(boolean test){

        code = new Random().nextInt(5) + 1;
        desc = "贷款描述，由贷款的code决定具体描述文本。";
        total = 2000000;
        year = 30;
        building = new Building(true);
        transaction = new Transaction();
        banker = new User(true);
        requestTime = System.currentTimeMillis();
        createAt = System.currentTimeMillis();
        updateAt = System.currentTimeMillis();
    }

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

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
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

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
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
