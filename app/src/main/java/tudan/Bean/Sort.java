package tudan.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by fengliang
 * on 2017/7/6.
 */

public class Sort extends BmobObject {
    private String sortid;
    private String sortname;
    private String sortpic;

    public BmobFile getImagefile() {
        return imagefile;
    }

    public void setImagefile(BmobFile imagefile) {
        this.imagefile = imagefile;
    }

    private BmobFile imagefile;

    public String getSortid() {
        return sortid;
    }

    public void setSortid(String sortid) {
        this.sortid = sortid;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public String getSortpic() {
        return sortpic;
    }

    public void setSortpic(String sortpic) {
        this.sortpic = sortpic;
    }
}
