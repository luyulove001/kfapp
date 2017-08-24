package com.xxl.kfapp.model.response;

import java.io.Serializable;
import java.util.List;

public class ShopRenameRecordVo implements Serializable {
    private List<RecordVo> rdlst;

    public List<RecordVo> getRdlst() {
        return rdlst;
    }

    public void setRdlst(List<RecordVo> rdlst) {
        this.rdlst = rdlst;
    }

    public class RecordVo {
        private String newname;
        private String czuser;
        private String czdate;
        private String oldname;

        public String getNewname() {
            return newname;
        }

        public void setNewname(String newname) {
            this.newname = newname;
        }

        public String getCzuser() {
            return czuser;
        }

        public void setCzuser(String czuser) {
            this.czuser = czuser;
        }

        public String getCzdate() {
            return czdate;
        }

        public void setCzdate(String czdate) {
            this.czdate = czdate;
        }

        public String getOldname() {
            return oldname;
        }

        public void setOldname(String oldname) {
            this.oldname = oldname;
        }
    }
}
