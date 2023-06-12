package com.wjx.homemaker.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/7/21.
 */

public class FriendEntity extends BaseEntity {

    public List<Friend> list;

    public FriendEntity(int errcode, String errmsg, List<Friend> list) {
        super(errcode, errmsg);
        this.list = list;
    }

    public List<Friend> getList() {
        return list;
    }

    public void setList(List<Friend> list) {
        this.list = list;
    }

    public static class Friend implements Serializable {

        public String score;
        public String username;
        public String uid;
        public String avatar;

        public String sex;
        public String hobby;
        public String track;
        public String sign;
        public String birthday;

        public String nickname;

        public Friend(String score, String username, String uid, String avatar, String sex, String hobby, String track, String sign, String birthday, String nickname) {
            this.score = score;
            this.username = username;
            this.uid = uid;
            this.avatar = avatar;
            this.sex = sex;
            this.hobby = hobby;
            this.track = track;
            this.sign = sign;
            this.birthday = birthday;
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public String getTrack() {
            return track;
        }

        public void setTrack(String track) {
            this.track = track;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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
}
