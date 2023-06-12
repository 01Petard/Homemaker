package com.wjx.homemaker.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/7/20.
 */

public class UserListEntity extends BaseEntity {

    public List<UserData> list;
    public Include include;

    public UserListEntity(int errcode, String errmsg, List<UserData> list, Include include) {
        super(errcode, errmsg);
        this.list = list;
        this.include = include;
    }

    public List<UserData> getList() {
        return list;
    }

    public void setList(List<UserData> list) {
        this.list = list;
    }

    public Include getInclude() {
        return include;
    }

    public void setInclude(Include include) {
        this.include = include;
    }

    public static class UserData implements Serializable {

        public String username;
        public String userType;
        public String phone;
        public String uid;
        public String avatar;

        public UserData(String username, String userType, String phone, String uid, String avatar) {
            this.username = username;
            this.userType = userType;
            this.phone = phone;
            this.uid = uid;
            this.avatar = avatar;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public static class Include implements Serializable {

        public String score;

        public Include(String score) {
            this.score = score;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }

}
