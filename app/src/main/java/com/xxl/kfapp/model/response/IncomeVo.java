package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class IncomeVo implements Serializable {
    private List<Income> rows;

    public List<Income> getRows() {
        return rows;
    }

    public void setRows(List<Income> rows) {
        this.rows = rows;
    }

    public class Income {
        private String balance;
        private String incometype;
        private String income;
        private String incomedate;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getIncometype() {
            return incometype;
        }

        public void setIncometype(String incometype) {
            this.incometype = incometype;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getIncomedate() {
            return incomedate;
        }

        public void setIncomedate(String incomedate) {
            this.incomedate = incomedate;
        }
    }
}
