package com.zhang.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhang.vo.PageInit;
import com.zhang.vo.initPage.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理员服务
 */
@RestController
public class AdminController {


    //响应主页
    @RequestMapping("adminpage")
    private ModelAndView homePage(String username){
        System.out.println("进入-------homePage");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("templates/applypage");
        mv.addObject("username",username);
        mv.addObject("initDir","admin/init");
        return mv;
    }

    //初始化目录
    @RequestMapping(value = "admin/init",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject adminInit(){
        System.out.println("进入--------adminInit");

        PageInit pageInit = new PageInit();
        //  1
        HomeInfo homeInfo = new HomeInfo("首页","page/apply/applied.html");
        pageInit.setHomeInfo(homeInfo);
        //  2
        LogoInfo logoInfo = new LogoInfo("后台管理系统","images/logo.png","");
        pageInit.setLogoInfo(logoInfo);
        //  3
        List<MenuInfo> menuInfos = new ArrayList<MenuInfo>();

        List<Son> sons1 = new ArrayList<Son>();
        List<Son> sons2 = new ArrayList<Son>();
        List<Son> sons3 = new ArrayList<Son>();
        List<Son> sons4 = new ArrayList<Son>();
        List<Son> sons5 = new ArrayList<Son>();

        /****  添加三级菜单 START ****/

//      Son son1 = new Son("申请材料","applying","fa fa-tachometer","_self");
//      sons1.add(son1);

        //申请单功能演示
        Son son1_2 = new Son("查看申请","page/apply/applied.html","fa fa-tachometer","_self");
        sons1.add(son1_2);

        //个人信息功能演示
        Son son2_1 = new Son("个人信息","page/user-setting.html","fa fa-tachometer","_self");
        sons2.add(son2_1);

        //修改密码
        Son son2_2 = new Son("修改密码","page/user-password.html","fa fa-tachometer","_self");
        sons2.add(son2_2);

        //申请单管理---查看所有的申清单----所有状态----不能修改
        Son son3_1 = new Son("申请单管理","page/admin/applied.html","fa fa-tachometer","_self");
        sons3.add(son3_1);

        //人员管理
        Son son4_1 = new Son("人员管理","page/admin/employee.html","fa fa-tachometer","_self");
        sons4.add(son4_1);

        //权限管理
        Son son5_1 = new Son("权限管理","page/admin/permission.html","fa fa-tachometer","_self");
        sons5.add(son5_1);

        /****  添加三级菜单 EDN ****/


        List<Child> children1 = new ArrayList<Child>();
        List<Child> children2 = new ArrayList<Child>();


        /****  添加二级菜单 START ****/

        Child child1_1 = new Child("申请单","","fa fa-file-text-o","_self",sons1);
        children1.add(child1_1);

        Child child1_2 = new Child("个人设置","","fa fa-user","_self",sons2);
        children1.add(child1_2);

        Child child2_1 = new Child("申请单管理","","fa fa-file-text-o","_self",sons3);
        children2.add(child2_1);

        Child child2_2 = new Child("人员管理","","fa fa-file-text-o","_self",sons4);
        children2.add(child2_2);

        Child child2_3 = new Child("权限管理","","fa fa-file-text-o","_self",sons5);
        children2.add(child2_3);

        /****  添加二级菜单 END ****/

        MenuInfo menuInfo1 = new MenuInfo("功能模块管理","fa fa-address-book","","_self",children1);
        MenuInfo menuInfo2 = new MenuInfo("职工模块管理","fa fa-address-book","","_self",children2);


        menuInfos.add(menuInfo1);
        menuInfos.add(menuInfo2);


        pageInit.setMenuInfo(menuInfos);

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(pageInit);

        return jsonObject;
    }

}


/*
{
    "menuInfo":[
    {
        "icon":"fa fa-address-book",
        "href":"",
        "title":"功能模块管理",
        "child":[
            {
                "icon":"fa fa-file-text-o",
                "href":"","title":"申请单",
                "child":[
                  {
                    "icon":"fa fa-tachometer",
                    "href":"page/apply/applied.html",
                    "title":"查看申请",
                    "target":"_self"
                  }
                ],
                "target":"_self"
             },

             {
                "icon":"fa fa-user",
                "href":"",
                "title":"个人设置",
                "child":[
                    {
                        "icon":"fa fa-tachometer",
                        "href":"page/user-setting.html",
                        "title":"个人信息",
                        "target":"_self"
                     },
                     {
                        "icon":"fa fa-tachometer",
                        "href":"page/user-password.html",
                        "title":"修改密码",
                        "target":"_self"
                      }
                  ],
                  "target":"_self"
                }
              ],
              "target":"_self"
              },
              {"icon":"fa fa-address-book","href":"","title":"职工模块管理","child":[{"icon":"fa fa-file-text-o","href":"","title":"申请单管理","child":[{"icon":"fa fa-tachometer","href":"page/admin/applied.html","title":"修改密码","target":"_self"}],"target":"_self"},{"icon":"fa fa-file-text-o","href":"","title":"人员管理","child":[{"icon":"fa fa-tachometer","href":"page/admin/employee.html","title":"人员管理","target":"_self"}],"target":"_self"},{"icon":"fa fa-file-text-o","href":"","title":"权限管理","child":[{"icon":"fa fa-tachometer","href":"page/admin/permission.html","title":"权限管理","target":"_self"}],"target":"_self"}],"target":"_self"}],"homeInfo":{"href":"page/apply/applied.html","title":"首页"},"logoInfo":{"image":"images/logo.png","href":"","title":"后台管理系统"}}
 */