package tudan.Tools;

import android.util.SparseArray;

import java.io.Serializable;
import java.util.HashMap;

import tudan.Item.GoodsItem;

/**
 * Created by fengliang
 * on 2017/7/6.
 */

public class SerializableSparseArray<E> extends SparseArray<E> implements Serializable {
    private SparseArray<GoodsItem> goodsitem;

    public SparseArray<GoodsItem> getGoodsitem() {
        return goodsitem;
    }

    public void setGoodsitem(SparseArray<GoodsItem> goodsitem) {
        this.goodsitem = goodsitem;
    }

    private HashMap<String,String> map;

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
}
