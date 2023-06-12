package com.wjx.homemaker.entity;


import java.io.Serializable;

public class UserEntity extends BaseEntity {

    public UserData userData;

    public static class UserData implements Serializable {

        public String username;
        public String userType;
        public String phone;
        public String uid;
        public String avatar;

        public String sex;
        public String hobby;
        public String track;
        public String sign;
        public String birthday;

        public String nickname;

        public UserData(String username, String userType, String phone, String uid, String avatar, String sex, String hobby, String track, String sign, String birthday, String nickname) {
            this.username = username;
            this.userType = userType;
            this.phone = phone;
            this.uid = uid;
            this.avatar = avatar;
            this.sex = sex;
            this.hobby = hobby;
            this.track = track;
            this.sign = sign;
            this.birthday = birthday;
            this.nickname = nickname;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
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

}
