package com.xxl.kfapp.model.response;

import java.io.Serializable;

/**
 * 作者：XNN
 * 日期：2017/6/13
 * 作用：考试题目的内容
 */

public class KSNRVo implements Serializable {
    private String info;//content
    private String id;//answer

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private boolean xz = false;
    private boolean answer = false;

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public boolean isXz() {
        return xz;
    }

    public void setXz(boolean xz) {
        this.xz = xz;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}
