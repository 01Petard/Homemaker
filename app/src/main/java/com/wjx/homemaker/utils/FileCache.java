package com.wjx.homemaker.utils;

import android.content.Context;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by admin on 2017/7/19.
 */

public class FileCache {

    /** 缓存文件目录 */
    private File mCacheDir;

    /**
     * 创建缓存文件目录，如果有SD卡，则使用SD，如果没有则使用系统自带缓存目录
     *
     * @param context
     * @param cacheDir 图片缓存的一级目录
     * @param dir
     */
    public FileCache(Context context, File cacheDir, String dir){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            mCacheDir = new File(cacheDir, dir);
        else
            mCacheDir = context.getCacheDir();// 如何获取系统内置的缓存存储路径
        if(!mCacheDir.exists())
            mCacheDir.mkdirs();
    }

    public File getFile(String url){
        File f=null;
        try {
            String filename = URLEncoder.encode(url,"utf-8");
            f = new File(mCacheDir, filename);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return f;

    }

    /**
     * 清除缓存文件
     */
    public void clear(){
        File[] files = mCacheDir.listFiles();
        for(File f:files)
            f.delete();
    }
}
