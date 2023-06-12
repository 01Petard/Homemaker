package com.wjx.homemaker.utils;



public class Config {

    public static final String BASEHOST = "http://de.by.cx:8080/kuaidi/";

    public static final String REGISTER = BASEHOST+"/mobile/UserServlet?method=register";
    public static final String LOGIN = BASEHOST+"/mobile/UserServlet?method=login";
    public static final String SCORE = BASEHOST + "/mobile/UserServlet?method=getRelationScore";
    public static final String ADDFD = BASEHOST + "/mobile/UserServlet?method=addFriend";
    public static final String GETFD = BASEHOST + "/mobile/UserServlet?method=listFriend&uid=";
    public static final String UPDATA = BASEHOST + "/mobile/UserServlet?method=update";

    public static final String UPNEW = BASEHOST + "/mobile/AttachmentServlet?method=upload";

    public static final String PUTNEWS = BASEHOST + "/mobile/PostServlet?method=submit";

    public static final String PUTUSER = BASEHOST + "/mobile/VerifiedInfoServlet?method=submit";

    public static final String GETNEWS = BASEHOST + "/mobile/PostServlet?method=getAll";
}
