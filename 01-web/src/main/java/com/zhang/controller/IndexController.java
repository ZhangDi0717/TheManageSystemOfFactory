package com.zhang.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhang.dao.EmployeeImpl;
import com.zhang.dao.EmployeeInformationImpl;
import com.zhang.dao.PositionImpl;
import com.zhang.entity.Employee;
import com.zhang.entity.EmployeeInformation;
import com.zhang.entity.Position;
import com.zhang.vo.PageInit;
import com.zhang.vo.Result;
import com.zhang.vo.initPage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

//首页响应服务
@Controller
public class IndexController {

    @Autowired
    private EmployeeImpl employeeimpl;

    @Autowired
    private EmployeeInformationImpl employeeInformation;

    @Autowired
    private PositionImpl positionimpl;

    //登陆界面
    @RequestMapping("/mylogin")
    private ModelAndView myLogin(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("templates/myLogin");
        return mv;
    }


    //首页
    @RequestMapping("/index")
    private ModelAndView index(HttpServletRequest httpServletRequest){
        System.out.println("go into index");
        ModelAndView mv = new ModelAndView();

        //方便测试开发
        if(httpServletRequest.getSession().getAttribute("username")==null)
            httpServletRequest.getSession().setAttribute("username","admin");

        //获取用户名
        String username = (String) httpServletRequest.getSession().getAttribute("username");

        mv.setViewName("templates/index");
        mv.addObject("username",username);

        Employee employee = employeeimpl.findByUsername(username);

        Set<Position> positionSet = employee.getPosition();

        int positionId = 0;

        for (Position position : positionSet) {
            positionId = position.getId().intValue();
        }

        String initDir = "";
        switch (positionId){
            case 1:initDir="adminInit";
                break;
            case 2:initDir="applyInit";
                break;
            case 3:initDir="approvalInit";
                break;
            case 4:initDir="deliveryInit";
                break;
            default:
                break;
        }
        mv.addObject("initDir",initDir);
        return mv;
    }



    //初始化目录
    @RequestMapping(value = "adminInit",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject adminInit(){
        System.out.println("进入--------adminInit");

        PageInit pageInit = new PageInit();
        //  1
        HomeInfo homeInfo = new HomeInfo("首页","/apply/manage");
        pageInit.setHomeInfo(homeInfo);
        //  2
        LogoInfo logoInfo = new LogoInfo("后台管理系统","../images/logo.png","");
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
        Son son2_1 = new Son("个人信息","../employee-setting","fa fa-tachometer","_self");
        sons2.add(son2_1);

        //修改密码
        Son son2_2 = new Son("修改密码","../employee-password","fa fa-tachometer","_self");
        sons2.add(son2_2);

        //申请单管理---查看所有的申清单----所有状态----不能修改
        Son son3_1 = new Son("申请单申请","/apply/manage","fa fa-tachometer","_self");
        sons3.add(son3_1);

        Son son3_2 = new Son("申请单审批","/approval/manage","fa fa-tachometer","_self");
        sons3.add(son3_2);

        Son son3_3 = new Son("申请单配送","/delivery/manage","fa fa-tachometer","_self");
        sons3.add(son3_3);

        //人员管理
        Son son4_1 = new Son("人员管理","/employee/manage","fa fa-tachometer","_self");
        sons4.add(son4_1);

        //权限管理
        Son son5_1 = new Son("权限管理","/permission/manage","fa fa-tachometer","_self");
        sons5.add(son5_1);

        //生产参数检测
        Son son6_1 = new Son("生产管理","/production/manage","fa fa-tachometer","_self");

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

        Child child2_1 = new Child("申请单管理","","fa fa-file-text-o","_self",sons3);
        children2.add(child2_1);

        Child child2_2 = new Child("人员管理","","fa fa-file-text-o","_self",sons4);
        children2.add(child2_2);

        Child child2_3 = new Child("生产检测","","fa fa-file-text-o","_self",sons6);
        children2.add(child2_3);


        Child child2_4 = new Child("权限管理","","fa fa-file-text-o","_self",sons5);
        children2.add(child2_4);

        Child child2_5 = new Child("个人管理","","fa fa-file-text-o","_self",sons2);
        children2.add(child2_5);

        /****  添加二级菜单 END ****/

//        MenuInfo menuInfo1 = new MenuInfo("功能模块管理","fa fa-address-book","","_self",children1);
        MenuInfo menuInfo2 = new MenuInfo("管理模块","fa fa-address-book","","_self",children2);


//        menuInfos.add(menuInfo1);
        menuInfos.add(menuInfo2);


        pageInit.setMenuInfo(menuInfos);

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(pageInit);

        return jsonObject;
    }


    /**
     *  employeeInformationSaveDir         保存个人信息                  employeeSetting
     * @param httpServletRequest
     * @return
     */
    //个人信息页面响应
    @RequestMapping(value = "/employee-setting")
    private ModelAndView employeeSetting(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView();

        if(httpServletRequest.getSession().getAttribute("username")==null)
            httpServletRequest.getSession().setAttribute("username","admin");


        String username = httpServletRequest.getSession().getAttribute("username").toString();
        Employee employee = employeeimpl.findByUsername(username);

        EmployeeInformation employeeinformation = employee.getEmployeeinformation();
        mv.addObject("employeeinformation",employeeinformation);

        List<Position> positionList = positionimpl.findAll();
        mv.addObject("positionList",positionList);

        Set<Position> positionSet = employee.getPosition();
        mv.addObject("positionSet",positionSet);

        mv.addObject("employeeInformationSaveDir","../employeeSetting");


        mv.setViewName("static/page/employee-setting");
        return mv;
    }

    //employeeInformationSaveDir         保存个人信息                  employeeSetting
    @RequestMapping(value = "employeeSetting",method = RequestMethod.POST)
    @ResponseBody
    private JSONObject employeeSetting(@RequestBody Map param,HttpServletRequest httpServletRequest){
        System.out.println("go into employeeSetting");

        JSONObject jsonObject = null;

        try {
            String username = httpServletRequest.getSession().getAttribute("username").toString();
            Employee employee = employeeimpl.findByUsername(username);

            //更新个人详细信息
            EmployeeInformation employeeinformation = employee.getEmployeeinformation();
            employeeinformation.setAddress(param.get("address").toString());
            employeeinformation.setEmail(param.get("email").toString());
            employeeinformation.setName(param.get("name").toString());
            employeeinformation.setPhone(param.get("phone").toString());
            employeeinformation.setSex(Integer.parseInt(param.get("sex").toString()));
            employee.setEmployeeinformation(employeeinformation);

            Position position = positionimpl.getOne(Long.parseLong(param.get("position").toString()));
            Set<Position> positionSet = new HashSet<Position>();
            positionSet.add(position);
            employee.setPosition(positionSet);

            employeeimpl.save(employee);
            jsonObject = (JSONObject) JSONObject.toJSON(new Result(0,"修改成功"));
        }catch (Exception e){
            System.out.println("employeeSetting processing field");
            System.out.println(e.toString());
            jsonObject = (JSONObject) JSONObject.toJSON(new Result(404,"修改失败"));
        }


        return jsonObject;
    }

    //修改密码页面响应
    @RequestMapping(value = "employee-password")
    private ModelAndView employeePassword(HttpServletRequest httpServletRequest){
        System.out.println("go into employeePassword");
        ModelAndView mv = new ModelAndView();

        String username = httpServletRequest.getSession().getAttribute("username").toString();
        Employee employee = employeeimpl.findByUsername(username);
        mv.addObject("employee",employee);

        mv.addObject("employeePasswordSaveDir","../employeePassword");

        mv.setViewName("static/page/employee-password");
        return mv;
    }

    //employeePasswordSaveDir         修改密码页面请求处理                  employeePassword
    @RequestMapping(value = "/employeePassword",method = RequestMethod.POST)
    @ResponseBody
    private JSONObject employeePassword(@RequestBody Map param,HttpServletRequest httpServletRequest){
        System.out.println("go into employeePassword");
        String username = httpServletRequest.getSession().getAttribute("username").toString();
        Result result = null;

        try{
            Employee employee = employeeimpl.findByUsername(username);
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            //判断密码是否正确
            boolean f = encoder.matches(param.get("old_password").toString(),employee.getPassword());
            if( f==false ){//密码不正确
                result = new Result(200,"密码不正确");
            }else {//密码正确
                employee.setPassword(encoder.encode(param.get("again_password").toString()));
                employeeimpl.save(employee);
                result = new Result(0,"密码修改成功");
            }
        }catch (Exception e){
            System.out.println("employeePassword processing failed");
            System.out.println(e.toString());
            result = new Result(404,"处理失败");

        }
        return (JSONObject) JSONObject.toJSON(result);
    }


}
