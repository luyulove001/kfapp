package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

public class AmountVo implements Serializable {
    private String zfbbl;
    private String total;
    private String wxbl;
    private List<Count> countlst;

    public String getZfbbl() {
        return zfbbl;
    }

    public void setZfbbl(String zfbbl) {
        this.zfbbl = zfbbl;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getWxbl() {
        return wxbl;
    }

    public void setWxbl(String wxbl) {
        this.wxbl = wxbl;
    }

    public List<Count> getCountlst() {
        return countlst;
    }

    public void setCountlst(List<Count> countlst) {
        this.countlst = countlst;
    }

    public class Count {
        private String total;
        private String wxtotal;
        private String shopid;
        private String zfbtotal;
        private String shopname;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getWxtotal() {
            return wxtotal;
        }

        public void setWxtotal(String wxtotal) {
            this.wxtotal = wxtotal;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getZfbtotal() {
            return zfbtotal;
        }

        public void setZfbtotal(String zfbtotal) {
            this.zfbtotal = zfbtotal;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }
    }
}
