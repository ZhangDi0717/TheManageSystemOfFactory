package com.zhang.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhang.dao.*;
import com.zhang.entity.*;
import com.zhang.vo.PageInit;
import com.zhang.vo.Result;
import com.zhang.vo.adminTableResult.AdminTable;
import com.zhang.vo.adminTableResult.AdminTableResult;
import com.zhang.vo.employeeTableResult.EmployeeTable;
import com.zhang.vo.employeeTableResult.EmployeeTableResult;
import com.zhang.vo.initPage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 管理员服务
 */
@RestController
public class AdminController {


    @Autowired
    private RequisitionImpl requisitionimpl;

    @Autowired
    private EmployeeImpl employeeimpl;

    @Autowired
    private PositionImpl positionimpl;

    @Autowired
    private DistributionImpl distributionimpl;

    @Autowired
    private MaterialImpl materialimpl;

    @Autowired
    private MainIngredientImpl mainIngredientimpl;

    @Autowired
    private IngredientImpl ingredientimpl;

    @Autowired
    private EmployeeInformationImpl employeeinformationimpl;


    //响应主页
    @RequestMapping("admin/index")
    private ModelAndView adminIndex(HttpServletRequest httpServletRequest) {
        System.out.println("进入-------adminIndex");

        //方便测试开发
        if (httpServletRequest.getSession().getAttribute("username") == null)
            httpServletRequest.getSession().setAttribute("username", "admin");

        //获取用户名
        String username = (String) httpServletRequest.getSession().getAttribute("username");

        ModelAndView mv = new ModelAndView();
        mv.setViewName("templates/index");
        mv.addObject("username", username);
        mv.addObject("initDir", "admin/init");
        return mv;
    }

    //初始化目录
    @RequestMapping(value = "admin/init", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject adminInit() {
        System.out.println("进入--------adminInit");

        PageInit pageInit = new PageInit();
        //  1
        HomeInfo homeInfo = new HomeInfo("首页", "/admin/apply/manage");
        pageInit.setHomeInfo(homeInfo);
        //  2
        LogoInfo logoInfo = new LogoInfo("后台管理系统", "../images/logo.png", "");
        pageInit.setLogoInfo(logoInfo);
        //  3
        List<MenuInfo> menuInfos = new ArrayList<MenuInfo>();

//        List<Son> sons1 = new ArrayList<Son>();
        List<Son> sons2 = new ArrayList<Son>();
        List<Son> sons3 = new ArrayList<Son>();
        List<Son> sons4 = new ArrayList<Son>();
        List<Son> sons5 = new ArrayList<Son>();
        List<Son> sons6 = new ArrayList<Son>();


        /****  添加三级菜单 START ****/

//      Son son1 = new Son("申请材料","applying","fa fa-tachometer","_self");
//      sons1.add(son1);

        //申请单功能演示
//        Son son1_2 = new Son("查看申请","page/apply/applied.html","fa fa-tachometer","_self");
//        sons1.add(son1_2);
//
        //个人信息功能演示
        Son son2_1 = new Son("个人信息", "../employee-setting", "fa fa-tachometer", "_self");
        sons2.add(son2_1);

        //修改密码
        Son son2_2 = new Son("修改密码", "../employee-password", "fa fa-tachometer", "_self");
        sons2.add(son2_2);

        //申请单管理---查看所有的申清单----所有状态----不能修改
        Son son3_1 = new Son("申请单申请", "/apply/manage", "fa fa-tachometer", "_self");
        sons3.add(son3_1);

        Son son3_2 = new Son("申请单审批", "/admin/approval/manage", "fa fa-tachometer", "_self");
        sons3.add(son3_2);

        Son son3_3 = new Son("申请单配送", "/admin/delivery/manage", "fa fa-tachometer", "_self");
        sons3.add(son3_3);

        //人员管理
        Son son4_1 = new Son("人员管理", "/admin/employee/manage", "fa fa-tachometer", "_self");
        sons4.add(son4_1);

        //权限管理
        Son son5_1 = new Son("权限管理", "/admin/permission/manage", "fa fa-tachometer", "_self");
        sons5.add(son5_1);

        //生产参数检测
        Son son6_1 = new Son("生产管理", "/admin/production/manage", "fa fa-tachometer", "_self");

        sons6.add(son6_1);

        /****  添加三级菜单 EDN ****/


//        List<Child> children1 = new ArrayList<Child>();
        List<Child> children2 = new ArrayList<Child>();


        /****  添加二级菜单 START ****/

//        Child child1_1 = new Child("申请单","","fa fa-file-text-o","_self",sons1);
//        children1.add(child1_1);
//
//        Child child1_2 = new Child("个人设置","","fa fa-user","_self",sons2);
//        children1.add(child1_2);

        Child child2_1 = new Child("申请单管理", "", "fa fa-file-text-o", "_self", sons3);
        children2.add(child2_1);

        Child child2_2 = new Child("人员管理", "", "fa fa-file-text-o", "_self", sons4);
        children2.add(child2_2);

        Child child2_3 = new Child("生产检测", "", "fa fa-file-text-o", "_self", sons6);
        children2.add(child2_3);


        Child child2_4 = new Child("权限管理", "", "fa fa-file-text-o", "_self", sons5);
        children2.add(child2_4);

        Child child2_5 = new Child("个人管理", "", "fa fa-file-text-o", "_self", sons2);
        children2.add(child2_5);

        /****  添加二级菜单 END ****/

//        MenuInfo menuInfo1 = new MenuInfo("功能模块管理","fa fa-address-book","","_self",children1);
        MenuInfo menuInfo2 = new MenuInfo("管理模块", "fa fa-address-book", "", "_self", children2);


//        menuInfos.add(menuInfo1);
        menuInfos.add(menuInfo2);


        pageInit.setMenuInfo(menuInfos);

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(pageInit);

        return jsonObject;
    }


    //申请单管理页面相应-----各种状态的表单查看----全能修改-----


    // adminSearchByDatelineDir    根据生产截止日期搜索申请单     apply/searchdate
    @RequestMapping(value = "admin/apply/searchdate", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject searchByDateTime(@RequestParam(value = "begin", required = false, defaultValue = "") String beginDate,
                                       @RequestParam(value = "end", required = false, defaultValue = "") String endDate,
                                       @RequestParam(value = "state", required = false, defaultValue = "") String state) {

        System.out.println("进入---searchByDateTime");
        JSONObject jsonObject = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            AdminTableResult adminTableResult = new AdminTableResult();
            List<Requisition> requisitionList = null;

            if (Integer.parseInt(state) == 1) {//查询截止日期范围
                requisitionList = requisitionimpl.findByDatelineBetween(sdf.parse(beginDate), sdf.parse(endDate));
            } else {
                requisitionList = requisitionimpl.findByDateBetween(sdf.parse(beginDate), sdf.parse(endDate));
            }

            adminTableResult.setCode(0);
            adminTableResult.setMsg("查询成功");
            adminTableResult.setCount(requisitionList.size());

            //封装响应数据
            List<AdminTable> adminTableList = new ArrayList<AdminTable>();
            for (Requisition requisition : requisitionList) {
                adminTableList.add(new AdminTable(
                        requisition.getId(),
                        requisition.getDateline(),
                        requisition.getDate(),
                        requisition.getState(),
                        requisition.getState(),
                        requisition.getEmployee().getId()
                ));
            }

            adminTableResult.setData(adminTableList);
            jsonObject = (JSONObject) JSONObject.toJSON(adminTableResult);
        } catch (Exception e) {
            jsonObject = (JSONObject) JSONObject.toJSON(new AdminTableResult(500, "请求响应失败", 0, null));
        }
        return jsonObject;
    }


}