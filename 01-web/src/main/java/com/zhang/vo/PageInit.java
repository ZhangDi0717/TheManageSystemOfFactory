package com.zhang.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhang.vo.initPage.HomeInfo;
import com.zhang.vo.initPage.LogoInfo;
import com.zhang.vo.initPage.MenuInfo;

import java.util.List;

/**
 * {
 *   "homeInfo": {
 *     "title": "主页",
 *     "href": "page/welcome-1.html?t=1"
 *   },
 *   "logoInfo": {
 *     "title": "后台管理系统",
 *     "image": "images/logo.png",
 *     "href": ""
 *   },
 *   "menuInfo": [
 *     {
 *       "title": "常规管理",
 *       "icon": "fa fa-address-book",
 *       "href": "",
 *       "target": "_self",
 *       "child": [
 *         {
 *           "title": "申请表",
 *           "href": "",
 *           "icon": "fa fa-home",
 *           "target": "_self",
 *           "child": [
 *             {
 *               "title": "申请",
 *               "href": "page/apply/applying.html",
 *               "icon": "fa fa-tachometer",
 *               "target": "_self"
 *             },
 *             {
 *               "title": "主页二",
 *               "href": "page/welcome-2.html",
 *               "icon": "fa fa-tachometer",
 *               "target": "_self"
 *             },
 *             {
 *               "title": "主页三",
 *               "href": "page/welcome-3.html",
 *               "icon": "fa fa-tachometer",
 *               "target": "_self"
 *             }
 *           ]
 *         },
 */

public class PageInit {

    @JSONField(name = "homeInfo")
    private HomeInfo homeInfo;
    @JSONField(name = "logoInfo")
    private LogoInfo logoInfo;
    @JSONField(name = "menuInfo")
    private List<MenuInfo> menuInfo;

    public PageInit() {
    }

    public PageInit(HomeInfo homeInfo, LogoInfo logoInfo, List<MenuInfo> menuInfo) {
        this.homeInfo = homeInfo;
        this.logoInfo = logoInfo;
        this.menuInfo = menuInfo;
    }

    public HomeInfo getHomeInfo() {
        return homeInfo;
    }

    public void setHomeInfo(HomeInfo homeInfo) {
        this.homeInfo = homeInfo;
    }

    public LogoInfo getLogoInfo() {
        return logoInfo;
    }

    public void setLogoInfo(LogoInfo logoInfo) {
        this.logoInfo = logoInfo;
    }

    public List<MenuInfo> getMenuInfo() {
        return menuInfo;
    }

    public void setMenuInfo(List<MenuInfo> menuInfo) {
        this.menuInfo = menuInfo;
    }
}
