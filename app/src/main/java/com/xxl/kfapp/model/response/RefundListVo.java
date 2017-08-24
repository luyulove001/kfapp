package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

public class RefundListVo implements Serializable {
    private List<TicketVo> rows;

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
        private String applytime;
        private String nickname;
        private String shopname;
        private String applysts;

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

        public String getApplytime() {
            return applytime;
        }

        public void setApplytime(String applytime) {
            this.applytime = applytime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getApplysts() {
            return applysts;
        }

        public void setApplysts(String applysts) {
            this.applysts = applysts;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }
    }
}
