package com.zhang.vo;

/**
 * 服务器响应结果信息类
 */
public class LoginResult {
    //code=0 成功；
    private int code;

    //消息内容
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
