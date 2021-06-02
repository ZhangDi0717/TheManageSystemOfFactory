package com.zhang.vo.initPage;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 *                  "title": "申请",
 *  *               "href": "page/apply/applying.html",
 *  *               "icon": "fa fa-tachometer",
 *  *               "target": "_self"
 */
public class Child {
    @JSONField(name = "title")
    private String Title;
    @JSONField(name = "href")
    private String Href;
    @JSONField(name = "icon")
    private String Icon;
    @JSONField(name = "target")
    private String Target;
    @JSONField(name = "child")
    private List<Son> Son;

    public Child() {
    }

    public Child(String title, String href, String icon, String target, List<com.zhang.vo.initPage.Son> son) {
        Title = title;
        Href = href;
        Icon = icon;
        Target = target;
        Son = son;
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

    public List<com.zhang.vo.initPage.Son> getSon() {
        return Son;
    }

    public void setSon(List<com.zhang.vo.initPage.Son> son) {
        Son = son;
    }
}
