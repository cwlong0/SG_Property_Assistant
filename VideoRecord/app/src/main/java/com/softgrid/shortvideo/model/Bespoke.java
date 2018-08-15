package com.softgrid.shortvideo.model;

/**
 * Created by tianfeng on 2018/6/5.
 */

public class Bespoke {

    private String id;
    private Building building;
    private User intermediary;
    private long bespokeTime;
    private long createAt;
    private long updateAt;

    private boolean isValid = true;

    public Bespoke(){

    }

    public Bespoke(boolean test){
        building = new Building(true);
        intermediary = new User(true);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public User getIntermediary() {
        return intermediary;
    }

    public void setIntermediary(User intermediary) {
        this.intermediary = intermediary;
    }

    public long getBespokeTime() {
        return bespokeTime;
    }

    public void setBespokeTime(long bespokeTime) {
        this.bespokeTime = bespokeTime;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
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
