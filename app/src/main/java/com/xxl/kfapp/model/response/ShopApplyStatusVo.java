package com.xxl.kfapp.model.response;

import java.io.Serializable;

public class ShopApplyStatusVo implements Serializable {
    private String shopid;
    private String checksts;
    private String brandmoney;
    private String fixedreason;
    private String applyid;
    private String applysts;
    private String customreason;
    private String prepayreason;
    private String devicereason;
    private String prepaychecksts;
    private String devicechecksts;

    public String getPrepayreason() {
        return prepayreason;
    }

    public void setPrepayreason(String prepayreason) {
        this.prepayreason = prepayreason;
    }

    public String getDevicereason() {
        return devicereason;
    }

    public void setDevicereason(String devicereason) {
        this.devicereason = devicereason;
    }

    public String getPrepaychecksts() {
        return prepaychecksts;
    }

    public void setPrepaychecksts(String prepaychecksts) {
        this.prepaychecksts = prepaychecksts;
    }

    public String getDevicechecksts() {
        return devicechecksts;
    }

    public void setDevicechecksts(String devicechecksts) {
        this.devicechecksts = devicechecksts;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getChecksts() {
        return checksts;
    }

    public void setChecksts(String checksts) {
        this.checksts = checksts;
    }

    public String getBrandmoney() {
        return brandmoney;
    }

    public void setBrandmoney(String brandmoney) {
        this.brandmoney = brandmoney;
    }

    public String getFixedreason() {
        return fixedreason;
    }

    public void setFixedreason(String fixedreason) {
        this.fixedreason = fixedreason;
    }

    public String getApplyid() {
        return applyid;
    }

    public void setApplyid(String applyid) {
        this.applyid = applyid;
    }

    public String getApplysts() {
        return applysts;
    }

    public void setApplysts(String applysts) {
        this.applysts = applysts;
    }

    public String getCustomreason() {
        return customreason;
    }

    public void setCustomreason(String customreason) {
        this.customreason = customreason;
    }

    @Override
    public String toString() {
        return "ShopApplyStatusVo{" +
                "shopid='" + shopid + '\'' +
                ", checksts='" + checksts + '\'' +
                ", brandmoney='" + brandmoney + '\'' +
                ", fixedreason='" + fixedreason + '\'' +
                ", applyid='" + applyid + '\'' +
                ", applysts='" + applysts + '\'' +
                ", customreason='" + customreason + '\'' +
                '}';
    }
}
