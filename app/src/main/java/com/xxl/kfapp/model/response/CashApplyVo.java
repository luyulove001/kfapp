package com.xxl.kfapp.model.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/17.
 */

public class CashApplyVo implements Serializable {
    private String total;
    private String balance;
    private String casefee;
    private String shopid;
    private String shopmodel;
    private String mincashamount;
    private String shopname;
    private String maxcashamount;

    public String getShopmodel() {
        return shopmodel;
    }

    public void setShopmodel(String shopmodel) {
        this.shopmodel = shopmodel;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCasefee() {
        return casefee;
    }

    public void setCasefee(String casefee) {
        this.casefee = casefee;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getMincashamount() {
        return mincashamount;
    }

    public void setMincashamount(String mincashamount) {
        this.mincashamount = mincashamount;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getMaxcashamount() {
        return maxcashamount;
    }

    public void setMaxcashamount(String maxcashamount) {
        this.maxcashamount = maxcashamount;
    }
}
