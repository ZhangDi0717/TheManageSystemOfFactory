package com.zhang.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhang.dao.EmployeeImpl;
import com.zhang.dao.PositionImpl;
import com.zhang.entity.Permission;
import com.zhang.entity.Position;
import com.zhang.vo.permissionTableResult.PermissionTable;
import com.zhang.vo.permissionTableResult.PermissionTableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class PermissionController {


    @Autowired
    private EmployeeImpl employeeimpl;

    @Autowired
    private PositionImpl positionimpl;


    /**
     *  权限管理
     * @return
     */
    @RequestMapping(value = "permission/manage")   //permission/manage
    private ModelAndView permissionManage(){
        System.out.println("进入-----permissionManage");
        ModelAndView mv = new ModelAndView();

        mv.setViewName("static/page/admin/permission");

        return mv;
    }


    //响应权限数据
    @RequestMapping(value = "permission/data")
    @ResponseBody
    private JSONObject permissionData(){
        System.out.println("go into permissionData");


        JSONObject jsonObject = null;

        try {
            List<Position> positionList = positionimpl.findByIdNot(Long.parseLong("1"));

            List<PermissionTable> permissionTableList = new ArrayList<PermissionTable>();

            for (Position position :positionList) {
                Set<Permission> permissionSet = position.getPermission();
                for (Permission permission : permissionSet) {
                    permissionTableList.add(new PermissionTable(
                            permission.getId().intValue(),
                            permission.getName(),
                            permission.getUrl(),
                            permission.getIsMenu(),
                            permission.getParentId()
                    ));
                }

            }
            PermissionTableResult permissionTableResult = new PermissionTableResult(0, "请求成功", permissionTableList.size(), permissionTableList);

            jsonObject = (JSONObject) JSONObject.toJSON(permissionTableResult);
        }catch (Exception e){
            System.out.println("permissionData processing failed");
            System.out.println(e.toString());
            PermissionTableResult permissionTableResult = new PermissionTableResult(404, "请求失败", 0, null);

            jsonObject = (JSONObject) JSONObject.toJSON(permissionTableResult);
        }



        return jsonObject;
    }




}
