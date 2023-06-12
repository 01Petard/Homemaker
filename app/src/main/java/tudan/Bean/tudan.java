package tudan.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by fengliang
 * on 2017/7/13.
 */

public class tudan extends BmobObject {
    private String name;
    private String nickname;
   public tudan(String name,String nickname){
        this.name=name;
       this.nickname=nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
