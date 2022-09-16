package cn.calendo.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 前后端协议联调 结果集result(R)
 * @param <T>
 */

@Data
public class R<T> implements Serializable {

    /**
     * 状态码：1成功，0失败
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 动态数据
     */
    private Map map = new HashMap();

    /**
     * 响应成功状态
     *
     * @param object
     * @param <T>
     * @return r
     */
    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    /**
     * 响应失败状态
     *
     * @param msg
     * @param <T>
     * @return r
     */
    public static <T> R<T> error(String msg) {
        R<T> r = new R<T>();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    /**
     * 增加动态数据
     * @param key
     * @param value
     * @return R
     */
    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
