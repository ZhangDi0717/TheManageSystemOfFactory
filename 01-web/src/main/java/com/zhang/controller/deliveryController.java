package com.zhang.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhang.dao.*;
import com.zhang.entity.*;
import com.zhang.vo.Result;
import com.zhang.vo.adminTableResult.AdminTable;
import com.zhang.vo.adminTableResult.AdminTableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class deliveryController {
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


    /**
     * 请求地址 ：delivery/manage
     * 功能说明 ：进行订单原料的分配
     *      deliveryTableIdUrl              数据表格的请求地址           delivery/table
     *      deliverySearchDir               搜索表格                   delivery/search
     *      deliveryDetailDir               查看配送表                 delivery/detail
     *      deliveryFinishBtnDir            配送完成                   delivery/finish
     *
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "delivery/manage")
    private ModelAndView deliveryManage(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView();
        System.out.println("go into deliveryManage");

        mv.addObject("approvalSearchDir","approval/search");
        //响应申请/生产员工
        List<Employee> appliedList =  employeeimpl.findByPosition(positionimpl.getOne(Long.parseLong("2")));
        mv.addObject("appliedList",appliedList);
        //响应配送员工
        List<Employee> deliverymanList =  employeeimpl.findByPosition(positionimpl.getOne(Long.parseLong("4")));
        mv.addObject("deliverymanList",deliverymanList);
        //响应申请单Id
        List<Requisition> applyList = requisitionimpl.findByStateBetween(1,3);
        mv.addObject("applyList",applyList);

        //数据表格
        mv.addObject("deliveryTableIdUrl","delivery/table");

        //查询按钮
        mv.addObject("deliverySearchDir","delivery/search");

        //分配单详情按钮
        mv.addObject("deliveryDetailDir","delivery/detail");

        //分配单完成
        mv.addObject("deliveryFinishBtnDir","delivery/finish");



        mv.setViewName("static/page/admin/delivery");
        return mv;
    }

    // deliveryTableIdUrl         数据表格的请求地址           delivery/table
    @RequestMapping(value = "delivery/table",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deliveryTable(){
        System.out.println("进入------deliveryTable");

        JSONObject jsonObject = new JSONObject();

        List<Requisition> requisitionList = requisitionimpl.findByState(4);

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


    //deliverySearchDir          搜索表格                   delivery/search
    @RequestMapping(value = "delivery/search",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deliverySearch(@RequestParam(value = "applyId", required = false,defaultValue = "") String applyId,
                                     @RequestParam(value = "appliedId", required = false, defaultValue = "") String appliedId,
                                     @RequestParam(value = "deliverymanId", required = false, defaultValue = "") String deliverymanId,
                                     @RequestParam(value = "applyState",required = false,defaultValue = "") String applyState,
                                     @RequestParam(value = "dateStart",required = false,defaultValue = "") String dateStart,
                                     @RequestParam(value = "dateEnd",required = false,defaultValue = "") String dateEnd,
                                     @RequestParam(value = "datelineStart",required = false,defaultValue = "") String datelineStart,
                                     @RequestParam(value = "datelineEnd",required = false,defaultValue = "") String datelineEnd)
    {
        System.out.println("进入------deliverySearch");
        int flag = 1;
        JSONObject jsonObject = new JSONObject();
        AdminTableResult adminTableResult = null;
        List<AdminTable> adminTableList = new ArrayList<AdminTable>();

        try {

            //通过申请单状态
            List<Requisition> requisitionLists = requisitionimpl.findByState(4);
            List<AdminTable> tableLists = new ArrayList<AdminTable>();
            for (Requisition requisition: requisitionLists) {
                tableLists.add(new AdminTable(
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
                adminTableList.removeAll(tableLists); // bingList为 [1, 2]
                adminTableList.addAll(tableLists);  //添加[3,4,5,6]
                flag = 0;
            }else {//交集
                adminTableList.retainAll(tableLists);
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
            System.out.println("deliverySearch 处理失败");
            System.out.println(e.toString());
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



    //deliveryDetailDir           查看配送表                  delivery/detail
    @RequestMapping(value ="delivery/detail")
    private ModelAndView deliveryDetail(String id,String v){
        System.out.println("进入------deliveryDetail");
        ModelAndView mv = new ModelAndView();

        Requisition requisition = requisitionimpl.getOne(Integer.parseInt(id));

        //申请单
        mv.addObject("requisition",requisition);

        //分配单
        Distribution distribution = requisition.getDistribution();
        mv.addObject("distribution",distribution);


        //所选主料
        List<MainIngredient> selectMainIngredientList = distribution.getMainingredient();
        mv.addObject("selectMainIngredientList",selectMainIngredientList);

        //所选辅料
        List<Ingredient> selectIngredientList = distribution.getIngredient();
        mv.addObject("selectIngredientList",selectIngredientList);



        mv.setViewName("static/page/admin/delivering");
        return mv;
    }

    //deliveryFinishBtnDir       配送完成                   delivery/finish
    @RequestMapping(value = "delivery/finish",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deliveryFinishBtn(@RequestBody Map param){
        System.out.println("go into deliveryFinishBtn");
        JSONObject jsonObject = null;

        try {
            Distribution distribution = distributionimpl.getOne(Integer.parseInt(param.get("distributionId").toString()));

            //更新申请单信息---申请单状态和审批意见
            Requisition requisition = requisitionimpl.findByDistribution(distribution);
            requisition.setState(5);
            requisitionimpl.save(requisition);

            Result result = new Result(0, "配送完成");

            jsonObject = (JSONObject) JSONObject.toJSON(result);
        } catch (Exception e){
            System.out.println("deliveryFinishBtn processing failed");
            System.out.println(e.toString());
            Result result = new Result(404, "配送失败");
            jsonObject = (JSONObject) JSONObject.toJSON(result);
        }

        return jsonObject;
    }

}
