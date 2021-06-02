package com.zhang.vo.initPage;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *        "title": "后台管理系统",
 *  *     "image": "images/logo.png",
 *  *     "href": ""
 */
public class LogoInfo {
    @JSONField(name = "title")
    private String Title;
    @JSONField(name = "image")
    private String Image;
    @JSONField(name = "href")
    private String Href;

    public LogoInfo() {
    }

    public LogoInfo(String title, String image, String href) {
        Title = title;
        Image = image;
        Href = href;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getHref() {
        return Href;
    }

    public void setHref(String href) {
        Href = href;
    }
}
