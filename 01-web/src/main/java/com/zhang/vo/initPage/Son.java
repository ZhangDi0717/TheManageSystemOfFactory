package com.zhang.vo.initPage;

import com.alibaba.fastjson.annotation.JSONField;

/*
                  "title": "申请",
 *               "href": "page/apply/applying.html",
 *               "icon": "fa fa-tachometer",
 *               "target": "_self"
 */
public class Son {
    @JSONField(name = "title")
    private String Title;
    @JSONField(name = "href")
    private String Href;
    @JSONField(name = "icon")
    private String Icon;
    @JSONField(name = "target")
    private String Target;

    public Son() {
    }

    public Son(String title, String href, String icon, String target) {
        Title = title;
        Href = href;
        Icon = icon;
        Target = target;
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

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getTarget() {
        return Target;
    }

    public void setTarget(String target) {
        Target = target;
    }
}
