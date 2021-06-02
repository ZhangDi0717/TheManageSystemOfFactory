package com.zhang.vo.initPage;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 *        "title": "常规管理",
 *       "icon": "fa fa-address-book",
 *       "href": "",
 *       "target": "_self",
 *       "child":[...]
 */
public class MenuInfo {
    @JSONField(name = "title")
    private String Title;
    @JSONField(name = "icon")
    private String Icon;
    @JSONField(name = "href")
    private String Href;
    @JSONField(name = "target")
    private String Target;
    @JSONField(name = "child")
    private List<Child> child;

    public MenuInfo() {
    }

    public MenuInfo(String title, String icon, String href, String target, List<Child> child) {
        Title = title;
        Icon = icon;
        Href = href;
        Target = target;
        this.child = child;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getHref() {
        return Href;
    }

    public void setHref(String href) {
        Href = href;
    }

    public String getTarget() {
        return Target;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }
}
