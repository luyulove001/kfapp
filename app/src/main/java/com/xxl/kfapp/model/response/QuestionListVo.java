package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

public class QuestionListVo implements Serializable {
    private List<Question> qList;

    public List<Question> getqList() {
        return qList;
    }

    public void setqList(List<Question> qList) {
        this.qList = qList;
    }

    public class Answer {
        private String content;
        private String answer;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        @Override
        public String toString() {
            return "Answer{" +
                    "content='" + content + '\'' +
                    ", answer='" + answer + '\'' +
                    '}';
        }
    }

    public class Question {
        private String findBy;
        private String qid;
        private String question;
        private List<Answer> alist;

        public String getFindBy() {
            return findBy;
        }

        public void setFindBy(String findBy) {
            this.findBy = findBy;
        }

        public String getQid() {
            return qid;
        }

        public void setQid(String qid) {
            this.qid = qid;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public List<Answer> getAlist() {
            return alist;
        }

        public void setAlist(List<Answer> alist) {
            this.alist = alist;
        }

        @Override
        public String toString() {
            return "QuestionListVo{" +
                    "findBy='" + findBy + '\'' +
                    ", qid='" + qid + '\'' +
                    ", question='" + question + '\'' +
                    ", alist=" + alist +
                    '}';
        }
    }
}
