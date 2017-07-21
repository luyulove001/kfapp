package com.xxl.kfapp.model.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/18.
 */

public class ShopApplyInfoVo implements Serializable {
    private String addprovincename;
    private String addprovincecode;
    private String addcityname;
    private String addcitycode;
    private String addareaname;
    private String addareacode;
    private String applyid;

    public String getAddprovincename() {
        return addprovincename;
    }

    public void setAddprovincename(String addprovincename) {
        this.addprovincename = addprovincename;
    }

    public String getAddprovincecode() {
        return addprovincecode;
    }

    public void setAddprovincecode(String addprovincecode) {
        this.addprovincecode = addprovincecode;
    }

    public String getAddcityname() {
        return addcityname;
    }

    public void setAddcityname(String addcityname) {
        this.addcityname = addcityname;
    }

    public String getAddcitycode() {
        return addcitycode;
    }

    public void setAddcitycode(String addcitycode) {
        this.addcitycode = addcitycode;
    }

    public String getAddareaname() {
        return addareaname;
    }

    public void setAddareaname(String addareaname) {
        this.addareaname = addareaname;
    }

    public String getAddareacode() {
        return addareacode;
    }

    public void setAddareacode(String addareacode) {
        this.addareacode = addareacode;
    }

    public String getApplyid() {
        return applyid;
    }

    public void setApplyid(String applyid) {
        this.applyid = applyid;
    }

    @Override
    public String toString() {
        return "ShopApplyInfoVo{" +
                "addprovincename='" + addprovincename + '\'' +
                ", addprovincecode='" + addprovincecode + '\'' +
                ", addcityname='" + addcityname + '\'' +
                ", addcitycode='" + addcitycode + '\'' +
                ", addareaname='" + addareaname + '\'' +
                ", addareacode='" + addareacode + '\'' +
                ", applyid='" + applyid + '\'' +
                '}';
    }
}
