package com.xxl.kfapp.model.response;

import java.io.Serializable;


public class SelectAddrstsInfoVo implements Serializable {
    private String nowprice;
    private String bond;
    private String prepayway;
    private String prepaycertsts;
    private String shopid;
    private String bidamount;
    private String bidmoneysts;
    private String prepaysts;
    private String salests;
    private String addrange;
    private String bidsts;

    private String endpaysts;
    private String prepayreason;
    private String prepaychecksts;
    private String startprice;

    public String getEndpaysts() {
        return endpaysts;
    }

    public void setEndpaysts(String endpaysts) {
        this.endpaysts = endpaysts;
    }

    public String getPrepayreason() {
        return prepayreason;
    }

    public void setPrepayreason(String prepayreason) {
        this.prepayreason = prepayreason;
    }

    public String getPrepaychecksts() {
        return prepaychecksts;
    }

    public void setPrepaychecksts(String prepaychecksts) {
        this.prepaychecksts = prepaychecksts;
    }

    public String getStartprice() {
        return startprice;
    }

    public void setStartprice(String startprice) {
        this.startprice = startprice;
    }

    public String getNowprice() {
        return nowprice;
    }

    public void setNowprice(String nowprice) {
        this.nowprice = nowprice;
    }

    public String getBond() {
        return bond;
    }

    public void setBond(String bond) {
        this.bond = bond;
    }

    public String getPrepayway() {
        return prepayway;
    }

    public void setPrepayway(String prepayway) {
        this.prepayway = prepayway;
    }

    public String getPrepaycertsts() {
        return prepaycertsts;
    }

    public void setPrepaycertsts(String prepaycertsts) {
        this.prepaycertsts = prepaycertsts;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getBidamount() {
        return bidamount;
    }

    public void setBidamount(String bidamount) {
        this.bidamount = bidamount;
    }

    public String getBidmoneysts() {
        return bidmoneysts;
    }

    public void setBidmoneysts(String bidmoneysts) {
        this.bidmoneysts = bidmoneysts;
    }

    public String getPrepaysts() {
        return prepaysts;
    }

    public void setPrepaysts(String prepaysts) {
        this.prepaysts = prepaysts;
    }

    public String getSalests() {
        return salests;
    }

    public void setSalests(String salests) {
        this.salests = salests;
    }

    public String getAddrange() {
        return addrange;
    }

    public void setAddrange(String addrange) {
        this.addrange = addrange;
    }

    public String getBidsts() {
        return bidsts;
    }

    public void setBidsts(String bidsts) {
        this.bidsts = bidsts;
    }

    @Override
    public String toString() {
        return "SelectAddrstsInfoVo{" +
                "nowprice='" + nowprice + '\'' +
                ", bond='" + bond + '\'' +
                ", prepayway='" + prepayway + '\'' +
                ", prepaycertsts='" + prepaycertsts + '\'' +
                ", shopid='" + shopid + '\'' +
                ", bidamount='" + bidamount + '\'' +
                ", bidmoneysts='" + bidmoneysts + '\'' +
                ", prepaysts='" + prepaysts + '\'' +
                ", salests='" + salests + '\'' +
                ", addrange='" + addrange + '\'' +
                ", bidsts='" + bidsts + '\'' +
                '}';
    }
}
