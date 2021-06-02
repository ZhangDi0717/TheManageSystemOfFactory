package com.zhang.vo.initPage;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *      "title": "首页",
 *     "href": "page/welcome-1.html?t=1"
 */
public class HomeInfo {
    @JSONField(name = "title")
    private String Title;
    @JSONField(name = "href")
    private String Href;

    public HomeInfo() {
    }

    public HomeInfo(String title, String href) {
        Title = title;
        Href = href;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getHref() {
        return Href;
    }

    public void setHref(String href) {
        Href = href;
    }
}
