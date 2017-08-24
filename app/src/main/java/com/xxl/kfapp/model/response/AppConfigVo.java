package com.xxl.kfapp.model.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/17.
 */

public class AppConfigVo implements Serializable {
    private String shopprotocol;//开店阅读协议
    private String bondnotice;//保证金须知
    private String barberprotocol;//快发师阅读协议
    private String transcompanyname;//收款账户名
    private String addgood;//选址优势
    private String barbercheckdays;//快发师审核期限
    private String cashcheckdays;//提现审核期限
    private String shopcheckdays;//开店审核期限
    private String barbergood; //快发师优势
    private String cashdesc;//提现说明
    private String transcheckdays;//转账审核期限
    private String qcpromise;//快发承诺
    private String contactus;//联系我们
    private String brandgoods;//品牌优势
    private String transbankinfo;//收款账号
    private String mincashamount;//最小提现额度
    private String ID;
    private String regprotocol;//注册协议
    private String cashrate;//提现费率
    private String maxcashamount;//最大提现额度

    public String getShopprotocol() {
        return shopprotocol;
    }

    public void setShopprotocol(String shopprotocol) {
        this.shopprotocol = shopprotocol;
    }

    public String getBondnotice() {
        return bondnotice;
    }

    public void setBondnotice(String bondnotice) {
        this.bondnotice = bondnotice;
    }

    public String getBarberprotocol() {
        return barberprotocol;
    }

    public void setBarberprotocol(String barberprotocol) {
        this.barberprotocol = barberprotocol;
    }

    public String getTranscompanyname() {
        return transcompanyname;
    }

    public void setTranscompanyname(String transcompanyname) {
        this.transcompanyname = transcompanyname;
    }

    public String getAddgood() {
        return addgood;
    }

    public void setAddgood(String addgood) {
        this.addgood = addgood;
    }

    public String getBarbercheckdays() {
        return barbercheckdays;
    }

    public void setBarbercheckdays(String barbercheckdays) {
        this.barbercheckdays = barbercheckdays;
    }

    public String getCashcheckdays() {
        return cashcheckdays;
    }

    public void setCashcheckdays(String cashcheckdays) {
        this.cashcheckdays = cashcheckdays;
    }

    public String getShopcheckdays() {
        return shopcheckdays;
    }

    public void setShopcheckdays(String shopcheckdays) {
        this.shopcheckdays = shopcheckdays;
    }

    public String getBarbergood() {
        return barbergood;
    }

    public void setBarbergood(String barbergood) {
        this.barbergood = barbergood;
    }

    public String getCashdesc() {
        return cashdesc;
    }

    public void setCashdesc(String cashdesc) {
        this.cashdesc = cashdesc;
    }

    public String getTranscheckdays() {
        return transcheckdays;
    }

    public void setTranscheckdays(String transcheckdays) {
        this.transcheckdays = transcheckdays;
    }

    public String getQcpromise() {
        return qcpromise;
    }

    public void setQcpromise(String qcpromise) {
        this.qcpromise = qcpromise;
    }

    public String getContactus() {
        return contactus;
    }

    public void setContactus(String contactus) {
        this.contactus = contactus;
    }

    public String getBrandgoods() {
        return brandgoods;
    }

    public void setBrandgoods(String brandgoods) {
        this.brandgoods = brandgoods;
    }

    public String getTransbankinfo() {
        return transbankinfo;
    }

    public void setTransbankinfo(String transbankinfo) {
        this.transbankinfo = transbankinfo;
    }

    public String getMincashamount() {
        return mincashamount;
    }

    public void setMincashamount(String mincashamount) {
        this.mincashamount = mincashamount;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRegprotocol() {
        return regprotocol;
    }

    public void setRegprotocol(String regprotocol) {
        this.regprotocol = regprotocol;
    }

    public String getCashrate() {
        return cashrate;
    }

    public void setCashrate(String cashrate) {
        this.cashrate = cashrate;
    }

    public String getMaxcashamount() {
        return maxcashamount;
    }

    public void setMaxcashamount(String maxcashamount) {
        this.maxcashamount = maxcashamount;
    }
}
