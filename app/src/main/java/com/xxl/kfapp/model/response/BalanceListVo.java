package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class BalanceListVo implements Serializable {
    private List<BalanceVo> balanceList;

    public List<BalanceVo> getBalanceList() {
        return balanceList;
    }

    public void setBalanceList(List<BalanceVo> balanceList) {
        this.balanceList = balanceList;
    }

    public class BalanceVo {
        private String balance;
        private String shopid;
        private String shopname;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }
    }
}
