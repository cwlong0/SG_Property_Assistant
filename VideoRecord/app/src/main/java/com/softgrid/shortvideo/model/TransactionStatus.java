package com.softgrid.shortvideo.model;

/**
 * Created by tianfeng on 2018/6/5.
 */

public class TransactionStatus {

    private String id;
    private int code;
    private String desc;
    private int isUploadDoc;
    private int isChecked;
    private int isReceiveContract;
    private int isSendOffer;
    private int isAccepted;
    private int haveLawyer;
    private int isSendback;
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

    public int getIsUploadDoc() {
        return isUploadDoc;
    }

    public void setIsUploadDoc(int isUploadDoc) {
        this.isUploadDoc = isUploadDoc;
    }

    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    public int getIsReceiveContract() {
        return isReceiveContract;
    }

    public void setIsReceiveContract(int isReceiveContract) {
        this.isReceiveContract = isReceiveContract;
    }

    public int getIsSendOffer() {
        return isSendOffer;
    }

    public void setIsSendOffer(int isSendOffer) {
        this.isSendOffer = isSendOffer;
    }

    public int getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(int isAccepted) {
        this.isAccepted = isAccepted;
    }

    public int getHaveLawyer() {
        return haveLawyer;
    }

    public void setHaveLawyer(int haveLawyer) {
        this.haveLawyer = haveLawyer;
    }

    public int getIsSendback() {
        return isSendback;
    }

    public void setIsSendback(int isSendback) {
        this.isSendback = isSendback;
    }

    public int getIsPayTaxes() {
        return isPayTaxes;
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
