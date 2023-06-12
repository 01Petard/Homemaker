package com.wjx.homemaker.entity;

/**
 * Created by admin on 2017/7/18.
 */

public class ImageEntity {

    public String aid;
    public String path;

    public ImageEntity(String aid, String path) {
        this.aid = aid;
        this.path = path;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
