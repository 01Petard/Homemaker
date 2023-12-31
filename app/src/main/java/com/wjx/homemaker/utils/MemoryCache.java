package com.wjx.homemaker.utils;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by admin on 2017/7/19.
 */

public class MemoryCache {
    /** 最大的缓存数 */
    private static final int MAX_CACHE_CAPACITY = 30;
    /**
     * 缓存map，当缓存数量超过规定大小，会清除最早放入缓存的
     */
    private HashMap<String, SoftReference<Bitmap>> mCacheMap =
            new LinkedHashMap<String, SoftReference<Bitmap>>() {
                private static final long serialVersionUID = 1L;
                //重写LinkedHashMap的方法，当map数量超过规定大小（30），则会清除最早放入的元素
                protected boolean removeEldestEntry(Entry<String,SoftReference<Bitmap>> eldest) {
                    return size() > MAX_CACHE_CAPACITY;
                };
            };

    /**
     * 从缓存里取出图片
     *
     * @param id
     * @return 如果缓存有，并且该图片没被释放，则返回该图片，否则返回null
     */
    public Bitmap get(String id){
        if(!mCacheMap.containsKey(id))
            return null;
        SoftReference<Bitmap> ref = mCacheMap.get(id);
        return ref.get();
    }

    /**
     * 将图片加入缓存
     *
     * @param id
     * @param bitmap
     */
    public void put(String id, Bitmap bitmap){
        mCacheMap.put(id, new SoftReference<Bitmap>(bitmap));
    }

    /**
     * 清除所有缓存
     */
    public void clear() {
        try {
            for(Map.Entry<String, SoftReference<Bitmap>> entry : mCacheMap.entrySet()) {
                SoftReference<Bitmap> sr = entry.getValue();
                if(null != sr) {
                    Bitmap bmp = sr.get();
                    if(null != bmp)
                        bmp.recycle();
                }
            }
            mCacheMap.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
