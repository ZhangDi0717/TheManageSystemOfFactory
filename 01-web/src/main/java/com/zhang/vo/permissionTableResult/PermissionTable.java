package com.zhang.vo.permissionTableResult;


/*

 {
      "authorityId": 2,
      "authorityName": "申请申请单",
      "isMenu": 0,
      "parentId": -1
    },

 */

import com.alibaba.fastjson.annotation.JSONField;

public class PermissionTable {

    //权限Id
    @JSONField(name = "authorityId")
    private Integer authorityId;

    @JSONField(name = "authorityName")
    private String authorityName;

    @JSONField(name = "url")
    private String url;

    @JSONField(name = "isMenu")
    private Integer isMenu;

    @JSONField(name = "parentId")
    private Integer parentId;

    public PermissionTable() {
    }

    public PermissionTable(Integer authorityId, String authorityName, String url, Integer isMenu, Integer parentId) {
        this.authorityId = authorityId;
        this.authorityName = authorityName;
        this.url = url;
        this.isMenu = isMenu;
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public Integer getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(Integer isMenu) {
        this.isMenu = isMenu;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
