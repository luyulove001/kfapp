package com.xxl.kfapp.model.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/8.
 */

public class BarberCountInfoVo implements Serializable{
    private String total;
    private String endtime;
    private String integrate;
    private String shopid;
    private String endsts;
    private String begintime;
    private String fromsts;
    private String shopname;
    private String realname;
    private String shopno;
    private String daytotal;
    private String signendtime;
    private String signfromtime;

    public String getSignendtime() {
        return signendtime;
    }

    public void setSignendtime(String signendtime) {
        this.signendtime = signendtime;
    }

    public String getSignfromtime() {
        return signfromtime;
    }

    public void setSignfromtime(String signfromtime) {
        this.signfromtime = signfromtime;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getIntegrate() {
        return integrate;
    }

    public void setIntegrate(String integrate) {
        this.integrate = integrate;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getEndsts() {
        return endsts;
    }

    public void setEndsts(String endsts) {
        this.endsts = endsts;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getFromsts() {
        return fromsts;
    }

    public void setFromsts(String fromsts) {
        this.fromsts = fromsts;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getShopno() {
        return shopno;
    }

    public void setShopno(String shopno) {
        this.shopno = shopno;
    }

    public String getDaytotal() {
        return daytotal;
    }

    public void setDaytotal(String daytotal) {
        this.daytotal = daytotal;
    }

}
