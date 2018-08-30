package com.du.weibo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author duzhentong
 * @Date 2018/8/29
 * @Time 14:10
 */

public class ViewObject {
    private Map<String, Object> objs = new HashMap<>();

    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
