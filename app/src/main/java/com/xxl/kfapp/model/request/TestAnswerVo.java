package com.xxl.kfapp.model.request;

import java.io.Serializable;

public class TestAnswerVo implements Serializable{
    private String qid;
    private String answer;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
