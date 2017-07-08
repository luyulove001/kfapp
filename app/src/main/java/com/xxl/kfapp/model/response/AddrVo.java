package com.xxl.kfapp.model.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/7.
 */

public class AddrVo implements Serializable {
    private String addprovincename;
    private String phone;
    private String username;
    private String addcitycode;
    private String address;
    private String addareaname;
    private String addprovincecode;
    private String addrid;
    private String addareacode;
    private String dispaddress;
    private String addcityname;

    public String getAddprovincename() {
        return addprovincename;
    }

    public void setAddprovincename(String addprovincename) {
        this.addprovincename = addprovincename;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAddareaname() {
        return addareaname;
    }

    public void setAddareaname(String addareaname) {
        this.addareaname = addareaname;
    }

    public String getAddprovincecode() {
        return addprovincecode;
    }

    public void setAddprovincecode(String addprovincecode) {
        this.addprovincecode = addprovincecode;
    }

    public String getAddrid() {
        return addrid;
    }

    public void setAddrid(String addrid) {
        this.addrid = addrid;
    }

    public String getAddareacode() {
        return addareacode;
    }

    public void setAddareacode(String addareacode) {
        this.addareacode = addareacode;
    }

    public String getDispaddress() {
        return dispaddress;
    }

    public void setDispaddress(String dispaddress) {
        this.dispaddress = dispaddress;
    }

    public String getAddcityname() {
        return addcityname;
    }

    public void setAddcityname(String addcityname) {
        this.addcityname = addcityname;
    }

    @Override
    public String toString() {
        return "AddrVo{" +
                "addprovincename='" + addprovincename + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", addcitycode='" + addcitycode + '\'' +
                ", address='" + address + '\'' +
                ", addareaname='" + addareaname + '\'' +
                ", addprovincecode='" + addprovincecode + '\'' +
                ", addrid='" + addrid + '\'' +
                ", addareacode='" + addareacode + '\'' +
                ", dispaddress='" + dispaddress + '\'' +
                ", addcityname='" + addcityname + '\'' +
                '}';
    }
}
