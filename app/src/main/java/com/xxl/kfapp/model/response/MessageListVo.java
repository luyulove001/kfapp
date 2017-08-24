package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class MessageListVo implements Serializable {
    private List<MessageVo> rows;

    public List<MessageVo> getRows() {
        return rows;
    }

    public void setRows(List<MessageVo> rows) {
        this.rows = rows;
    }

    public class MessageVo {
        private String readsts;
        private String title;
        private String msgid;
        private String msgtype;
        private String sendtime;
        private String sendmsg;

        public String getReadsts() {
            return readsts;
        }

        public void setReadsts(String readsts) {
            this.readsts = readsts;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMsgid() {
            return msgid;
        }

        public void setMsgid(String msgid) {
            this.msgid = msgid;
        }

        public String getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(String msgtype) {
            this.msgtype = msgtype;
        }

        public String getSendtime() {
            return sendtime;
        }

        public void setSendtime(String sendtime) {
            this.sendtime = sendtime;
        }

        public String getSendmsg() {
            return sendmsg;
        }

        public void setSendmsg(String sendmsg) {
            this.sendmsg = sendmsg;
        }
    }
}
