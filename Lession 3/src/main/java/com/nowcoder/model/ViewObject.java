package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rainday on 16/6/30.
 */
//为啥定义这个model?:因为方便在templates中的读取。比如：一个用户的所有信息可能来自不同的表，所以将一个用户的所有信息整合成一个viewobject就可以方面在模板中的写入
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
