package com.xxl.kfapp.model.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/28.
 */

public class BossCountInfoVo implements Serializable {
    private String applyflg;
    private double totaldayamount;
    private String totalcnt;
    private String shopcnt;
    private String applyid;
    private String totalbbcnt;
    private String offline;
    private String online;
    private double totalbalance;
    private String totalworkcdcnt;
    private String totaldaycnt;
    private String totalworkcnt;
    private String totalworkztcnt;
    private double totalamount;

    public String getApplyflg() {
        return applyflg;
    }

    public void setApplyflg(String applyflg) {
        this.applyflg = applyflg;
    }

    public double getTotaldayamount() {
        return totaldayamount;
    }

    public void setTotaldayamount(double totaldayamount) {
        this.totaldayamount = totaldayamount;
    }

    public String getTotalcnt() {
        return totalcnt;
    }

    public void setTotalcnt(String totalcnt) {
        this.totalcnt = totalcnt;
    }

    public String getShopcnt() {
        return shopcnt;
    }

    public void setShopcnt(String shopcnt) {
        this.shopcnt = shopcnt;
    }

    public String getApplyid() {
        return applyid;
    }

    public void setApplyid(String applyid) {
        this.applyid = applyid;
    }

    public String getTotalbbcnt() {
        return totalbbcnt;
    }

    public void setTotalbbcnt(String totalbbcnt) {
        this.totalbbcnt = totalbbcnt;
    }

    public String getOffline() {
        return offline;
    }

    public void setOffline(String offline) {
        this.offline = offline;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public double getTotalbalance() {
        return totalbalance;
    }

    public void setTotalbalance(double totalbalance) {
        this.totalbalance = totalbalance;
    }

    public String getTotalworkcdcnt() {
        return totalworkcdcnt;
    }

    public void setTotalworkcdcnt(String totalworkcdcnt) {
        this.totalworkcdcnt = totalworkcdcnt;
    }

    public String getTotaldaycnt() {
        return totaldaycnt;
    }

    public void setTotaldaycnt(String totaldaycnt) {
        this.totaldaycnt = totaldaycnt;
    }

    public String getTotalworkcnt() {
        return totalworkcnt;
    }

    public void setTotalworkcnt(String totalworkcnt) {
        this.totalworkcnt = totalworkcnt;
    }

    public String getTotalworkztcnt() {
        return totalworkztcnt;
    }

    public void setTotalworkztcnt(String totalworkztcnt) {
        this.totalworkztcnt = totalworkztcnt;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }

    @Override
    public String toString() {
        return "BossCountInfoVo{" +
                "applyflg='" + applyflg + '\'' +
                ", totaldayamount=" + totaldayamount +
                ", totalcnt='" + totalcnt + '\'' +
                ", shopcnt='" + shopcnt + '\'' +
                ", applyid='" + applyid + '\'' +
                ", totalbbcnt='" + totalbbcnt + '\'' +
                ", offline='" + offline + '\'' +
                ", online='" + online + '\'' +
                ", totalbalance=" + totalbalance +
                ", totalworkcdcnt='" + totalworkcdcnt + '\'' +
                ", totaldaycnt='" + totaldaycnt + '\'' +
                ", totalworkcnt='" + totalworkcnt + '\'' +
                ", totalworkztcnt='" + totalworkztcnt + '\'' +
                ", totalamount=" + totalamount +
                '}';
    }
}
