package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/10.
 */

public class SignListVo implements Serializable {
    private List<SignVo> signlst;

    public List<SignVo> getSignlst() {
        return signlst;
    }

    public void setSignlst(List<SignVo> signlst) {
        this.signlst = signlst;
    }

    public class SignVo {
        private String endtime;
        private String endsts;
        private String fromsts;
        private String signdate;
        private String fromtime;

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getEndsts() {
            return endsts;
        }

        public void setEndsts(String endsts) {
            this.endsts = endsts;
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

        public String getFromtime() {
            return fromtime;
        }

        public void setFromtime(String fromtime) {
            this.fromtime = fromtime;
        }
    }
}
