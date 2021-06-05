package com.zhang.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhang.dao.DistributionImpl;
import com.zhang.dao.EmployeeImpl;
import com.zhang.dao.PositionImpl;
import com.zhang.dao.RequisitionImpl;
import com.zhang.entity.Distribution;
import com.zhang.entity.Employee;
import com.zhang.entity.Position;
import com.zhang.entity.Requisition;
import com.zhang.vo.PageInit;
import com.zhang.vo.adminTableResult.AdminTable;
import com.zhang.vo.adminTableResult.AdminTableResult;
import com.zhang.vo.applyTableResult.ApplyTable;
import com.zhang.vo.applyTableResult.ApplyTableResult;
import com.zhang.vo.initPage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Action;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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


    //响应主页
    @RequestMapping("admin/homepage")
    private ModelAndView homePage(HttpServletRequest httpServletRequest){
        System.out.println("进入-------homePage");
        //获取用户名
        String username = (String) httpServletRequest.getSession().getAttribute("username");
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
        HomeInfo homeInfo = new HomeInfo("首页","");
        pageInit.setHomeInfo(homeInfo);
        //  2
        LogoInfo logoInfo = new LogoInfo("后台管理系统","../images/logo.png","");
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
        Son son3_1 = new Son("申请单管理","/admin/apply/manage","fa fa-tachometer","_self");
        sons3.add(son3_1);

        //人员管理
        Son son4_1 = new Son("人员管理","/admin/employee/manage","fa fa-tachometer","_self");
        sons4.add(son4_1);

        //权限管理
        Son son5_1 = new Son("权限管理","/admin/permission/manage","fa fa-tachometer","_self");
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


    //申请单管理页面相应-----各种状态的表单查看----全能修改-----

    /**
     * 模块说明：
     *          1、查看各种状态的申请单
     *          2、申请单所有数据都可以修改
     *
     * 本函数功能说明：
     *          返回数据表格
     *
     * 页面响应数据:
     *      adminTableIdUrl             数据表格的请求地址           apply/table
     *      adminSearchByDatelineDir    根据生产截止日期搜索申请单     apply/searchdate
     *      adminSearchByIdDir          根据申请单Id搜索
     *      adminSearchDir              根据条件搜索指定的申请单       apply/search
     *
     *
     * @return 相应页面
     */
    @RequestMapping(value = "admin/apply/manage")
    private ModelAndView applyManage(){
        System.out.println("进入-----applyManage");
        ModelAndView mv = new ModelAndView();

        mv.addObject("adminTableIdUrl","apply/table");

        mv.addObject("adminSearchByDateDir","apply/searchdate");

        mv.addObject("adminSearchDir","apply/search");
        //响应申请/生产员工
        List<Employee> appliedList =  employeeimpl.findByPosition(positionimpl.getOne(Long.parseLong("2")));
        mv.addObject("appliedList",appliedList);
        //响应配送员工
        List<Employee> deliverymanList =  employeeimpl.findByPosition(positionimpl.getOne(Long.parseLong("4")));
        mv.addObject("deliverymanList",deliverymanList);
        //响应申请单Id
        List<Requisition> applyList = requisitionimpl.findAll();
        mv.addObject("applyList",applyList);



        mv.setViewName("static/page/admin/apply");

        return mv;
    }

    // adminTableIdUrl         数据表格的请求地址           admin/apply/table
    @RequestMapping(value = "admin/apply/table",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject applyTable(){
        System.out.println("进入------applyTable");

        JSONObject jsonObject = new JSONObject();

        List<Requisition> requisitionList = requisitionimpl.findAll();

        AdminTableResult adminTableResult = null;
        if( requisitionList.size()==0 ){
            adminTableResult = new AdminTableResult(404,"暂无申请单信息",0,null);

        }else {//有申请单信息
            List<AdminTable> adminTableList = new ArrayList<AdminTable>();
            for (Requisition requisition :requisitionList){
                AdminTable adminTable = new AdminTable(
                        requisition.getId(),
                        requisition.getDateline(),
                        requisition.getDate(),
                        requisition.getState(),
                        requisition.getState(),
                        requisition.getEmployee().getId());
                adminTableList.add(adminTable);
            }
            adminTableResult = new AdminTableResult(0,"申请单请求成功",requisitionList.size(),adminTableList);

        }

        jsonObject = (JSONObject) JSONObject.toJSON(adminTableResult);

        return jsonObject;
    }

    //adminSearchDir              根据条件搜索指定的申请单       apply/search
    @RequestMapping(value = "admin/apply/search",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject search(@RequestParam(value = "applyId", required = false,defaultValue = "") String applyId,
                             @RequestParam(value = "appliedId", required = false, defaultValue = "") String appliedId,
                             @RequestParam(value = "deliverymanId", required = false, defaultValue = "") String deliverymanId,
                             @RequestParam(value = "applyState", required = false, defaultValue = "") String applyState,
                             @RequestParam(value = "dateStart",required = false,defaultValue = "") String dateStart,
                             @RequestParam(value = "dateEnd",required = false,defaultValue = "") String dateEnd,
                             @RequestParam(value = "datelineStart",required = false,defaultValue = "") String datelineStart,
                             @RequestParam(value = "datelineEnd",required = false,defaultValue = "") String datelineEnd){
        System.out.println("进入------search");
        int flag = 1;
        JSONObject jsonObject = new JSONObject();
        AdminTableResult adminTableResult = null;
        List<AdminTable> adminTableList = new ArrayList<AdminTable>();

        try {
            //单独查询
            if( !applyId.isEmpty()){//订单Id
                Requisition requisition = requisitionimpl.getOne(Integer.parseInt(applyId));
                List<AdminTable> TableList = new ArrayList<AdminTable>();
                TableList.add(new AdminTable(
                        requisition.getId(),
                        requisition.getDateline(),
                        requisition.getDate(),
                        requisition.getState(),
                        requisition.getState(),
                        requisition.getEmployee().getId())
                );
                if( flag==1 ){//并集(先做差集再做添加所有）
                    adminTableList.removeAll(TableList); // bingList为 [1, 2]
                    adminTableList.addAll(TableList);  //添加[3,4,5,6]
                    flag = 0;
                }else {//交集
                    adminTableList.retainAll(TableList);
                }
            }

            if( !appliedId.isEmpty()) {//申请人
                List<Requisition> requisitionList = requisitionimpl.findByEmployee(employeeimpl.getOne(Long.parseLong(appliedId)));
                List<AdminTable> TableList = new ArrayList<AdminTable>();
                //封装表格
                for (Requisition requisition :requisitionList) {
                    TableList.add(new AdminTable(
                            requisition.getId(),
                            requisition.getDateline(),
                            requisition.getDate(),
                            requisition.getState(),
                            requisition.getState(),
                            requisition.getEmployee().getId()
                    ));
                }

                //进行交并集
                if( flag==1 ){//并集(先做差集再做添加所有）
                    adminTableList.removeAll(TableList); // bingList为 [1, 2]
                    adminTableList.addAll(TableList);  //添加[3,4,5,6]
                    flag = 0;
                }else {//交集
                    adminTableList.retainAll(TableList);
                }

            }

            if( !deliverymanId.isEmpty()){//配送员
                List<Distribution> distributionList = distributionimpl.findByEmployee(employeeimpl.getOne(Long.parseLong(deliverymanId)));
                List<AdminTable> tableList = new ArrayList<AdminTable>();
                for (Distribution distribution: distributionList) {
                    Requisition requisition = requisitionimpl.findByDistribution(distribution);
                    tableList.add(new AdminTable(
                            requisition.getId(),
                            requisition.getDateline(),
                            requisition.getDate(),
                            requisition.getState(),
                            requisition.getState(),
                            requisition.getEmployee().getId()
                    ));
                }
                //进行交并集
                if( flag==1 ){//并集(先做差集再做添加所有）
                    adminTableList.removeAll(tableList); // bingList为 [1, 2]
                    adminTableList.addAll(tableList);  //添加[3,4,5,6]
                    flag = 0;
                }else {//交集
                    adminTableList.retainAll(tableList);
                }

            }

            if( !applyState.isEmpty() ){//通过申请单状态
                List<Requisition> requisitionList = requisitionimpl.findByState(Integer.parseInt(applyState));
                List<AdminTable> tableList = new ArrayList<AdminTable>();
                for (Requisition requisition: requisitionList) {
                    tableList.add(new AdminTable(
                            requisition.getId(),
                            requisition.getDateline(),
                            requisition.getDate(),
                            requisition.getState(),
                            requisition.getState(),
                            requisition.getEmployee().getId()
                    ));
                }
                //进行交并集
                if( flag==1 ){//并集(先做差集再做添加所有）
                    adminTableList.removeAll(tableList); // bingList为 [1, 2]
                    adminTableList.addAll(tableList);  //添加[3,4,5,6]
                    flag = 0;
                }else {//交集
                    adminTableList.retainAll(tableList);
                }

            }

            if( !dateStart.isEmpty()){//申请日期
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                List<Requisition> requisitionList = requisitionimpl.findByDateBetween(sdf.parse(dateStart), sdf.parse(dateEnd));
                List<AdminTable> tableList = new ArrayList<AdminTable>();
                for (Requisition requisition: requisitionList) {
                    tableList.add(new AdminTable(
                            requisition.getId(),
                            requisition.getDateline(),
                            requisition.getDate(),
                            requisition.getState(),
                            requisition.getState(),
                            requisition.getEmployee().getId()
                    ));
                }
                //进行交并集
                if( flag==1 ){//并集(先做差集再做添加所有）
                    adminTableList.removeAll(tableList); // bingList为 [1, 2]
                    adminTableList.addAll(tableList);  //添加[3,4,5,6]
                    flag = 0;
                }else {//交集
                    adminTableList.retainAll(tableList);
                }
            }

            if( !datelineStart.isEmpty()){//生产日期
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                List<Requisition> requisitionList = requisitionimpl.findByDatelineBetween(sdf.parse(dateStart), sdf.parse(dateEnd));
                List<AdminTable> tableList = new ArrayList<AdminTable>();
                for (Requisition requisition: requisitionList) {
                    tableList.add(new AdminTable(
                            requisition.getId(),
                            requisition.getDateline(),
                            requisition.getDate(),
                            requisition.getState(),
                            requisition.getState(),
                            requisition.getEmployee().getId()
                    ));
                }
                //进行交并集
                if( flag==1 ){//并集(先做差集再做添加所有）
                    adminTableList.removeAll(tableList); // bingList为 [1, 2]
                    adminTableList.addAll(tableList);  //添加[3,4,5,6]
                    flag = 0;
                }else {//交集
                    adminTableList.retainAll(tableList);
                }
            }

            adminTableResult = new AdminTableResult(
                    0,
                    "查询成功",
                    adminTableList.size(),
                    adminTableList
            );

        }catch (Exception e){
            System.out.println("search 处理失败");
            adminTableResult = new AdminTableResult(
                    404,
                    "查询失败",
                    0,
                    null
            );
        }


        jsonObject = (JSONObject) JSONObject.toJSON(adminTableResult);

        return jsonObject;

    }

    // adminSearchByDatelineDir    根据生产截止日期搜索申请单     apply/searchdate
    @RequestMapping(value = "admin/apply/searchdate",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject searchByDateTime(@RequestParam(value = "begin",required = false,defaultValue = "") String beginDate,
                                   @RequestParam(value = "end",required = false,defaultValue = "") String endDate,
                                   @RequestParam(value = "state",required = false,defaultValue = "") String state){

        System.out.println("进入---searchByDateTime");
        JSONObject jsonObject = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            AdminTableResult adminTableResult = new AdminTableResult();
            List<Requisition> requisitionList = null;

            if( Integer.parseInt(state)==1 ){//查询截止日期范围
                requisitionList = requisitionimpl.findByDatelineBetween(sdf.parse(beginDate),sdf.parse(endDate));
            }else {
                requisitionList = requisitionimpl.findByDateBetween(sdf.parse(beginDate),sdf.parse(endDate));
            }

            adminTableResult.setCode(0);
            adminTableResult.setMsg("查询成功");
            adminTableResult.setCount(requisitionList.size());

            //封装响应数据
            List<AdminTable> adminTableList = new ArrayList<AdminTable>();
            for (Requisition requisition :requisitionList) {
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
        }catch (Exception e){
            jsonObject = (JSONObject) JSONObject.toJSON(new AdminTableResult(500,"请求响应失败",0,null));
        }
        return jsonObject;
    }


    /**
     *
     * @return
     */
    @RequestMapping(value = "admin/employee/manage")
    private ModelAndView employeeManage(){
        System.out.println("进入-----employeeManage");
        ModelAndView mv = new ModelAndView();

        mv.setViewName("static/page/admin/employee");

        return mv;
    }


    /**
     *
     * @return
     */
    @RequestMapping(value = "admin/permission/manage")
    private ModelAndView permissionManage(){
        System.out.println("进入-----permissionManage");
        ModelAndView mv = new ModelAndView();

        mv.setViewName("static/page/admin/permission");


        return mv;
    }


    //个人管理

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


/*
"date": "2021-05-27T16:54:52.576+00:00",
"dateline": "2021-05-28T16:00:00.000+00:00",
"applyEmployeeId": 2,
"id": 10,
"state": 1,
"state1": 1
 */