package com.wjx.homemaker.entity;

import java.io.Serializable;

/**
 * Created by admin on 2017/7/12.
 */

public class BaseEntity implements Serializable {
    /* 服务器500错误 */
    public int errcode = 0;
    public String errmsg = "ok";

    public BaseEntity() {
    }

    public BaseEntity(int errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
