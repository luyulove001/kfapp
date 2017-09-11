package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

public class TicketListVo implements Serializable {
    private List<TicketVo> rows;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TicketVo> getRows() {
        return rows;
    }

    public void setRows(List<TicketVo> rows) {
        this.rows = rows;
    }

    public class TicketVo {
        private String buytime;
        private String price;
        private String ticketno;
        private String checktime;
        private String realname;
        private String shopname;

        public String getBuytime() {
            return buytime;
        }

        public void setBuytime(String buytime) {
            this.buytime = buytime;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTicketno() {
            return ticketno;
        }

        public void setTicketno(String ticketno) {
            this.ticketno = ticketno;
        }

        public String getChecktime() {
            return checktime;
        }

        public void setChecktime(String checktime) {
            this.checktime = checktime;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }
    }
}
