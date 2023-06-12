package com.wjx.homemaker.utils;


import com.google.gson.Gson;

public class GsonUtil {
    public static <T> T fromJson(String json, Class<T> clazz) {

        Gson gson = new Gson();
        return gson.fromJson(json, clazz);

    }

}
