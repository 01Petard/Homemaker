package tudan.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by fengliang
 * on 2017/7/5.
 */

public class Goods extends BmobObject {
    private String goodsId;
    private String sortname;
    private String goodsname;
    private BmobFile goodspic;
    private String goodsCount;
    private String goodsPrice;

    public Goods(String goodsId,String sortname,String goodsname,String goodsCount,String goodsPrice ,BmobFile goodspic){
        this.goodsId=goodsId;
        this.sortname=sortname;
        this.goodsname=goodsname;
        this.goodspic=goodspic;
        this.goodsCount=goodsCount;
        this.goodsPrice=goodsPrice;


    }


    public BmobFile getGoodspic() {
        return goodspic;
    }

    public void setGoodspic(BmobFile goodspic) {
        this.goodspic = goodspic;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }



    public String getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }


}
