package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

public class ShopGoodListVo implements Serializable {
    private List<GoodsInfo> onelst;

    private List<GoodsInfo> morelst;

    public List<GoodsInfo> getOnelst() {
        return onelst;
    }

    public List<GoodsInfo> getMorelst() {
        return morelst;
    }

    public void setOnelst(List<GoodsInfo> onelst) {
        this.onelst = onelst;
    }

    public void setMorelst(List<GoodsInfo> morelst) {
        this.morelst = morelst;
    }

    public class GoodsInfo {
        private String goodsname;
        private String csname;
        private int price;
        private String memo;
        private String csid;
        private String pic;
        private int num;
        private String ydflag;
        private String gid;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getYdflag() {
            return ydflag;
        }

        public void setYdflag(String ydflag) {
            this.ydflag = ydflag;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getGoodsname() {
            return goodsname;
        }

        public void setGoodsname(String goodsname) {
            this.goodsname = goodsname;
        }

        public String getCsname() {
            return csname;
        }

        public void setCsname(String csname) {
            this.csname = csname;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getCsid() {
            return csid;
        }

        public void setCsid(String csid) {
            this.csid = csid;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        @Override
        public String toString() {
            return "GoodsInfo{" +
                    "goodsname='" + goodsname + '\'' +
                    ", csname='" + csname + '\'' +
                    ", price='" + price + '\'' +
                    ", memo='" + memo + '\'' +
                    ", csid='" + csid + '\'' +
                    ", pic='" + pic + '\'' +
                    '}';
        }
    }
}
