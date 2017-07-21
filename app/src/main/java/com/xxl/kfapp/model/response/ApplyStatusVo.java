package com.xxl.kfapp.model.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/13.
 */

public class ApplyStatusVo implements Serializable {
    private String applysts;
    private String testcnt;
    private String checksts;
    private String fixedreason;
    private String teststs;
    private String customreason;

    public String getCustomreason() {
        return customreason;
    }

    public void setCustomreason(String customreason) {
        this.customreason = customreason;
    }

    public String getApplysts() {
        return applysts;
    }

    public void setApplysts(String applysts) {
        this.applysts = applysts;
    }

    public String getTestcnt() {
        return testcnt;
    }

    public void setTestcnt(String testcnt) {
        this.testcnt = testcnt;
    }

    public String getChecksts() {
        return checksts;
    }

    public void setChecksts(String checksts) {
        this.checksts = checksts;
    }

    public String getFixedreason() {
        return fixedreason;
    }

    public void setFixedreason(String fixedreason) {
        this.fixedreason = fixedreason;
    }

    public String getTeststs() {
        return teststs;
    }

    public void setTeststs(String teststs) {
        this.teststs = teststs;
    }

    @Override
    public String toString() {
        return "ApplyStatusVo{" +
                "applysts='" + applysts + '\'' +
                ", testcnt='" + testcnt + '\'' +
                ", checksts='" + checksts + '\'' +
                ", fixedreason='" + fixedreason + '\'' +
                ", teststs='" + teststs + '\'' +
                '}';
    }
}
