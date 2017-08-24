package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

public class OrderListVo implements Serializable {
    private List<Order> orderlst;

    public List<Order> getOrderlst() {
        return orderlst;
    }

    public void setOrderlst(List<Order> orderlst) {
        this.orderlst = orderlst;
    }

    public class Order {
        private String amount;
        private String orderdate;
        private String subject;
        private String paytype;
        private String refundsts;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getOrderdate() {
            return orderdate;
        }

        public void setOrderdate(String orderdate) {
            this.orderdate = orderdate;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public String getRefundsts() {
            return refundsts;
        }

        public void setRefundsts(String refundsts) {
            this.refundsts = refundsts;
        }
    }
}
