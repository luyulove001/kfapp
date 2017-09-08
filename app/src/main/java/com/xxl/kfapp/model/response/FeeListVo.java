package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

public class FeeListVo implements Serializable {
    private List<Fee> feelst;
    private String enddate;
    private int bidmoney;
    private String companyaccount;
    private String companyname;

    public int getBidmoney() {
        return bidmoney;
    }

    public void setBidmoney(int bidmoney) {
        this.bidmoney = bidmoney;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getCompanyaccount() {
        return companyaccount;
    }

    public void setCompanyaccount(String companyaccount) {
        this.companyaccount = companyaccount;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public List<Fee> getFeelst() {
        return feelst;
    }

    public void setFeelst(List<Fee> feelst) {
        this.feelst = feelst;
    }

    public class Fee {
        private String amount;
        private String unit;
        private String costname;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getCostname() {
            return costname;
        }

        public void setCostname(String costname) {
            this.costname = costname;
        }
    }
}
