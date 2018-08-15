package com.softgrid.shortvideo.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tianfeng on 2018/6/5.
 */

public class Building {

    private String id;
    private int type;
    private int isResale;
    private int isAuth;
    private String title;
    private String name;
    private int floor;
    private String rooms;
    private String number;                   //房号 2-1-1004
    private int decorate;                    //装修等级
    private int orientation;                 //朝向
    private String thumbnail;                //缩略图
    private String original;                 //原始图片
    private ArrayList<WebImage> images;      //图片
    private int region;                      //所在大区
    private int pd;                          //所在区
    private String address;
    private float longitude;                 //经度
    private float latitude;                  //纬度
    private long openTime;                   //开盘时间
    private String area;
    private float unitPrice;
    private float totalPrice;
    private User intermediary;
    private User owner;
    private ArrayList<Tag> featuresTags;
    private String featureStr;
    private String desc;
    private int status;
    private int followCount;
    private long createAt;
    private long updateAt;

    private boolean isValid = true;

    public Building(){

    }

    public Building(boolean test){
        if (test){

            isValid = new Random().nextInt(2) > 0;
            type = 1;
            featuresTags = new ArrayList<>();
            featuresTags.add(new Tag(true));
            featuresTags.add(new Tag(true));
            featuresTags.add(new Tag(true));

            images = new ArrayList<>();
            images.add(new WebImage(true));
            images.add(new WebImage(true));
            images.add(new WebImage(true));
            images.add(new WebImage(true));
            images.add(new WebImage(true));

            intermediary = new User(true);
            owner = new User(true);

            number = "20-1";
            region = new Random().nextInt(3) + 1;
            pd = new Random().nextInt(27) + 1;
            address = "成都市高新区大源";
            floor = new Random().nextInt(34) + 1;
            thumbnail = "http://fdfs.xmcdn.com/group23/M01/15/2B/wKgJL1gYYjzxW7fQAAWaMAvliP8961_mobile_meduim.jpg";
            original = "http://fdfs.xmcdn.com/group23/M01/15/2B/wKgJL1gYYjzxW7fQAAWaMAvliP8961_mobile_meduim.jpg";
            decorate = new Random().nextInt(4) + 1;
            orientation = new Random().nextInt(7) + 1;
            followCount = new Random().nextInt(999);
            desc = "朗诗熙华府开盘,优惠高达20万元,全程0元一对一定制服务,全国1000+合作楼盘,20万套房源任您挑选,朗诗熙华府开盘,7个购房者中4个用「居理新房」";
            openTime = System.currentTimeMillis();
            createAt = System.currentTimeMillis();
            isResale = new Random().nextInt(3);
            if (isResale > 0){
                isAuth = new Random().nextInt(1);
                title = "朗诗西华府二期房源，正对中庭，豪华装修，拎包入住！房东急卖，低于市场价20万！！";
                name = "朗诗西华府";
                rooms = "5";
                area = "200";
                unitPrice = 14000;
                totalPrice = 2800000;
            }
            else {
                status = new Random().nextInt(3) + 1;
                title = "朗诗西华府二期";
                name = "朗诗西华府";
                rooms = "3-5";
                area = "87-200";
                unitPrice = 14000;
                totalPrice = 2800000;
            }

        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public ArrayList<WebImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<WebImage> images) {
        this.images = images;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getIntermediary() {
        return intermediary;
    }

    public void setIntermediary(User intermediary) {
        this.intermediary = intermediary;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ArrayList<Tag> getFeaturesTags() {
        return featuresTags;
    }

    public void setFeaturesTags(ArrayList<Tag> featuresTags) {
        this.featuresTags = featuresTags;
    }

    public String getFeatureStr() {
        return featureStr;
    }

    public void setFeatureStr(String featureStr) {
        this.featureStr = featureStr;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
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

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
