package pers.jssd.mybatispluspro.utils;

import lombok.Data;

/**
 * @author jssd
 */
@Data
public class ResponseUtils {

    private int code;

    private String msg;

    private Object data;

    public static ResponseUtils success(String msg) {
        ResponseUtils responseUtils = new ResponseUtils();
        responseUtils.code = 200;
        responseUtils.msg = msg;
        return responseUtils;
    }


    public static ResponseUtils success(Object object) {
        ResponseUtils responseUtils = new ResponseUtils();
        responseUtils.code = 200;
        responseUtils.data = object;
        return responseUtils;
    }


}
