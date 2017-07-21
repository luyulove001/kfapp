package com.xxl.kfapp.model.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/15.
 */

public class ShopApplyStatusVo implements Serializable {
    private String shopid;
    private String checksts;
    private String brandmoney;
    private String fixedreason;
    private String applyid;
    private String applysts;
    private String customreason;

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
