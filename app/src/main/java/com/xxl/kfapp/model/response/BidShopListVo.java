package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class BidShopListVo implements Serializable {

    private List<BidShopVo> shoplst;

    public List<BidShopVo> getShoplst() {
        return shoplst;
    }

    public void setShoplst(List<BidShopVo> shoplst) {
        this.shoplst = shoplst;
    }

    public class BidShopVo implements Serializable {
        private String endtime;
        private String shopid;
        private String starttime;
        private String pcnt;
        private String address;
        private String viewcount;
        private String salests;
        private String shopname;
        private String listpic;

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getPcnt() {
            return pcnt;
        }

        public void setPcnt(String pcnt) {
            this.pcnt = pcnt;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getViewcount() {
            return viewcount;
        }

        public void setViewcount(String viewcount) {
            this.viewcount = viewcount;
        }

        public String getSalests() {
            return salests;
        }

        public void setSalests(String salests) {
            this.salests = salests;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getListpic() {
            return listpic;
        }

        public void setListpic(String listpic) {
            this.listpic = listpic;
        }
    }
}
