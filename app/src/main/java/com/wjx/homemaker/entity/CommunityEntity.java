package com.wjx.homemaker.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/7/19.
 */

public class CommunityEntity {

    public List<XX> list;

    public int errcode;
    public String errmsg ;

    public CommunityEntity(List<XX> list, int errcode, String errmsg) {
        this.list = list;
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public List<XX> getList() {
        return list;
    }

    public void setList(List<XX> list) {
        this.list = list;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode0(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public static class XX implements Serializable {

        public String pid;
        public String createDate;
        public String content;
        public UserData userData;
        public List<AttachMents> attachments;
//        public AttachMents attachments;

//        public XX(String pid, String createDate, String content, UserData userData, AttachMents attachments) {
//            this.pid = pid;
//            this.createDate = createDate;
//            this.content = content;
//            this.userData = userData;
//            this.attachments = attachments;
//        }

//        public AttachMents getAttachments() {
//            return attachments;
//        }
//
//        public void setAttachments(AttachMents attachments) {
//            this.attachments = attachments;
//        }


        public XX(String pid, String createDate, String content, UserData userData, List<AttachMents> attachments) {
            this.pid = pid;
            this.createDate = createDate;
            this.content = content;
            this.userData = userData;
            this.attachments = attachments;
        }


        public List<AttachMents> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<AttachMents> attachments) {
            this.attachments = attachments;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public UserData getUserData() {
            return userData;
        }

        public void setUserData(UserData userData) {
            this.userData = userData;
        }

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

        public static class AttachMents implements Serializable {

            public String aid;
            public String path;

            public AttachMents(String aid, String path) {
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

    }
}
