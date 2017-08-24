package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class IncomeListVo implements Serializable {
    private List<IncomeVo> rows;

    public List<IncomeVo> getRows() {
        return rows;
    }

    public void setRows(List<IncomeVo> rows) {
        this.rows = rows;
    }

    public class IncomeVo {
        private String amount;
        private String headpic;
        private String nickname;
        private String orderdate;
        private String paytype;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getHeadpic() {
            return headpic;
        }

        public void setHeadpic(String headpic) {
            this.headpic = headpic;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getOrderdate() {
            return orderdate;
        }

        public void setOrderdate(String orderdate) {
            this.orderdate = orderdate;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }
    }
}
