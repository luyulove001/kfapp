package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

public class StaffVo implements Serializable {
    private List<Staff> stafflst;
    private String staffcnt;

    public List<Staff> getStafflst() {
        return stafflst;
    }

    public void setStafflst(List<Staff> stafflst) {
        this.stafflst = stafflst;
    }

    public String getStaffcnt() {
        return staffcnt;
    }

    public void setStaffcnt(String staffcnt) {
        this.staffcnt = staffcnt;
    }

    public class Staff {
        private String headpic;
        private String shopid;
        private String phone;
        private String barberid;
        private String shopname;
        private String staffno;
        private String realname;
        private String shopno;
        private String formatphone;

        public String getHeadpic() {
            return headpic;
        }

        public void setHeadpic(String headpic) {
            this.headpic = headpic;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBarberid() {
            return barberid;
        }

        public void setBarberid(String barberid) {
            this.barberid = barberid;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getStaffno() {
            return staffno;
        }

        public void setStaffno(String staffno) {
            this.staffno = staffno;
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

        public String getFormatphone() {
            return formatphone;
        }

        public void setFormatphone(String formatphone) {
            this.formatphone = formatphone;
        }
    }
}
