package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class CashRecordVo implements Serializable {
    private List<CashVo> rows;

    public List<CashVo> getRows() {
        return rows;
    }

    public void setRows(List<CashVo> rows) {
        this.rows = rows;
    }

    public class CashVo {
        private String amount;
        private String applysts;
        private String applytime;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getApplysts() {
            return applysts;
        }

        public void setApplysts(String applysts) {
            this.applysts = applysts;
        }

        public String getApplytime() {
            return applytime;
        }

        public void setApplytime(String applytime) {
            this.applytime = applytime;
        }
    }
}
