package com.shileiyu.okgo;

import com.google.gson.Gson;

/**
 * @author shilei.yu
 * @since on 2018/3/15.
 */

public class JsonUtil {
    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
