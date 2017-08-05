package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;


public class CheckInVo implements Serializable {
    private List<CheckVo> rows;

    public List<CheckVo> getRows() {
        return rows;
    }

    public void setRows(List<CheckVo> rows) {
        this.rows = rows;
    }

    public class CheckVo {
        private String endsts;
        private String nickname;
        private String fromsts;
        private String signdate;

        public String getEndsts() {
            return endsts;
        }

        public void setEndsts(String endsts) {
            this.endsts = endsts;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getFromsts() {
            return fromsts;
        }

        public void setFromsts(String fromsts) {
            this.fromsts = fromsts;
        }

        public String getSigndate() {
            return signdate;
        }

        public void setSigndate(String signdate) {
            this.signdate = signdate;
        }
    }
}
