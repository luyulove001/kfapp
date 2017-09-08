package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

public class BossShopListVo implements Serializable {

    private List<BossShopInfo> shoplst;

    public List<BossShopInfo> getShoplst() {
        return shoplst;
    }

    public void setShoplst(List<BossShopInfo> shoplst) {
        this.shoplst = shoplst;
    }

    public class BossShopInfo {
        private String shopid;
        private String starttime;
        private int onlinests;
        private String nickname;
        private String address;
        private String shopname;
        private String bbcnt;
        private String applysts;
        private String shopno;
        private String shoppic;

        public int getOnlinests() {
            return onlinests;
        }

        public void setOnlinests(int onlinests) {
            this.onlinests = onlinests;
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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getBbcnt() {
            return bbcnt;
        }

        public void setBbcnt(String bbcnt) {
            this.bbcnt = bbcnt;
        }

        public String getApplysts() {
            return applysts;
        }

        public void setApplysts(String applysts) {
            this.applysts = applysts;
        }

        public String getShopno() {
            return shopno;
        }

        public void setShopno(String shopno) {
            this.shopno = shopno;
        }

        public String getShoppic() {
            return shoppic;
        }

        public void setShoppic(String shoppic) {
            this.shoppic = shoppic;
        }
    }
}
