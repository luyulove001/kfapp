package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/12.
 */

public class ApplyListVo implements Serializable {
    private List<ApplyVo> applylst;

    public List<ApplyVo> getApplylst() {
        return applylst;
    }

    public void setApplylst(List<ApplyVo> applylst) {
        this.applylst = applylst;
    }

    public class ApplyVo {
        private String shopid;
        private String applystsname;
        private String applyid;
        private String applysts;
        private String applytime;

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getApplystsname() {
            return applystsname;
        }

        public void setApplystsname(String applystsname) {
            this.applystsname = applystsname;
        }

        public String getApplyid() {
            return applyid;
        }

        public void setApplyid(String applyid) {
            this.applyid = applyid;
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
