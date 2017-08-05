package com.xxl.kfapp.model.response;

import java.io.Serializable;

public class TextVo implements Serializable {
    private String txt1;
    private String txt2;

    public String getTxt1() {
        return txt1;
    }

    public void setTxt1(String txt1) {
        this.txt1 = txt1;
    }

    public String getTxt2() {
        return txt2;
    }

    public void setTxt2(String txt2) {
        this.txt2 = txt2;
    }

    @Override
    public String toString() {
        return "TextVo{" +
                "txt1='" + txt1 + '\'' +
                ", txt2='" + txt2 + '\'' +
                '}';
    }
}
