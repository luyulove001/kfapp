package com.xxl.kfapp.model.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/12.
 */

public class BarberInfoVo implements Serializable {
    private String worklife;//工作年限
    private String education;//教育程度
    private String linktel;//紧急联系人电话
    private String cardid;//身份证号
    private String cardpos;//身份证正面
    private String cardneg;//身份证反面
    private String addprovincecode;
    private String addprovincename;
    private String addcitycode;
    private String addcityname;
    private String addareacode;
    private String addareaname;
    private String address;
    private String certdisc;//获得的荣誉、成就
    private String linkman;//紧急联系人
    private String realname;//真实姓名

    public String getAddareaname() {
        return addareaname;
    }

    public void setAddareaname(String addareaname) {
        this.addareaname = addareaname;
    }

    public String getWorklife() {
        return worklife;
    }

    public void setWorklife(String worklife) {
        this.worklife = worklife;
    }

    public String getAddprovincecode() {
        return addprovincecode;
    }

    public void setAddprovincecode(String addprovincecode) {
        this.addprovincecode = addprovincecode;
    }

    public String getAddareacode() {
        return addareacode;
    }

    public void setAddareacode(String addareacode) {
        this.addareacode = addareacode;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getLinktel() {
        return linktel;
    }

    public void setLinktel(String linktel) {
        this.linktel = linktel;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getAddprovincename() {
        return addprovincename;
    }

    public void setAddprovincename(String addprovincename) {
        this.addprovincename = addprovincename;
    }

    public String getCardpos() {
        return cardpos;
    }

    public void setCardpos(String cardpos) {
        this.cardpos = cardpos;
    }

    public String getAddcitycode() {
        return addcitycode;
    }

    public void setAddcitycode(String addcitycode) {
        this.addcitycode = addcitycode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardneg() {
        return cardneg;
    }

    public void setCardneg(String cardneg) {
        this.cardneg = cardneg;
    }

    public String getCertdisc() {
        return certdisc;
    }

    public void setCertdisc(String certdisc) {
        this.certdisc = certdisc;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAddcityname() {
        return addcityname;
    }

    public void setAddcityname(String addcityname) {
        this.addcityname = addcityname;
    }
}
