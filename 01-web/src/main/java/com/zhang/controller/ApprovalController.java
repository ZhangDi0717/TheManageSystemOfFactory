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
public class ApprovalController {


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
     * 请求路径：approval/manage
     * 模块功能：管理申请单的审批流程
     *
     * 功能路径：
     *      approvalTableIdUrl               数据表格的请求地址            approval/table
     *      approvalSearchDir                根据条件搜索指定的申请单       approval/search
     *      approvalApprovingDir             审批按钮                    approval/approving
     *      approvalApprovingBtnDir          审批通过                    approval/approvingBtn
     *      approvalApprovingErrorBtnDir     审批驳回                    approval/approvingErrorBtn
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "/approval/manage")
    private ModelAndView approvalManage(HttpServletRequest httpServletRequest){
        ModelAndView mv = new ModelAndView();

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
        mv.addObject("approvalTableIdUrl","approval/table");

        //查询按钮
        mv.addObject("approvalSearchDir","approval/search");

        //审批按钮
        mv.addObject("approvalApprovingDir","approval/approving");

        mv.addObject("approvalApprovingBtnDir","approval/approvingBtn");
        mv.addObject("approvalApprovingErrorBtnDir","approval/approvingErrorBtn");

        mv.setViewName("static/page/admin/approval");
        return mv;
    }



    // approvalTableIdUrl         数据表格的请求地址           approval/table
    @RequestMapping(value = "approval/table",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject approvalTable(){
        System.out.println("进入------approvalTable");

        JSONObject jsonObject = new JSONObject();

        List<Requisition> requisitionList = requisitionimpl.findByStateBetween(1,3);

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


    //approvalSearchDir      根据条件搜索指定的申请单       approval/search
    @RequestMapping(value = "approval/search",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject approvalSearch(@RequestParam(value = "applyId", required = false,defaultValue = "") String applyId,
                                     @RequestParam(value = "appliedId", required = false, defaultValue = "") String appliedId,
                                     @RequestParam(value = "deliverymanId", required = false, defaultValue = "") String deliverymanId,
                                     @RequestParam(value = "applyState",required = false,defaultValue = "") String applyState,
                                     @RequestParam(value = "dateStart",required = false,defaultValue = "") String dateStart,
                                     @RequestParam(value = "dateEnd",required = false,defaultValue = "") String dateEnd,
                                     @RequestParam(value = "datelineStart",required = false,defaultValue = "") String datelineStart,
                                     @RequestParam(value = "datelineEnd",required = false,defaultValue = "") String datelineEnd) {
        System.out.println("进入------approvalSearch");
        int flag = 1;
        JSONObject jsonObject = new JSONObject();
        AdminTableResult adminTableResult = null;
        List<AdminTable> adminTableList = new ArrayList<AdminTable>();

        try {

            //通过申请单状态
            List<Requisition> requisitionLists = requisitionimpl.findByStateBetween(1,3);
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



    //approvalApprovingDir      审批按钮                    approval/approving
    //审批
    @RequestMapping(value ="approval/approving")
    private ModelAndView approvalApproving(String id,String v){
        System.out.println("进入------approvalApproving");
        ModelAndView mv = new ModelAndView();

        Requisition requisition = requisitionimpl.getOne(Integer.parseInt(id));



        //申请单
        mv.addObject("requisition",requisition);

        //分配单
        Distribution distribution = requisition.getDistribution();
        mv.addObject("distribution",distribution);

        //主料
        List<Material> mainIngredientList = materialimpl.findAllByState(1);
        mv.addObject("mainIngredientList",mainIngredientList);

        //辅料
        List<Material> ingredientList = materialimpl.findAllByState(0);
        mv.addObject("ingredientList",ingredientList);

        //所选主料
        List<MainIngredient> selectMainIngredientList = distribution.getMainingredient();
        mv.addObject("selectMainIngredientList",selectMainIngredientList);

        //所选辅料
        List<Ingredient> selectIngredientList = distribution.getIngredient();
        mv.addObject("selectIngredientList",selectIngredientList);

        //配送员
        Position position = positionimpl.getOne(Long.parseLong("3"));
        List<Employee> employeeList = employeeimpl.findByPosition(position);
        mv.addObject("employeeList",employeeList);

        if (requisition.getState() == 1){
            requisition.setState(2);
            requisitionimpl.save(requisition);
        }



        mv.setViewName("static/page/admin/approving");
        return mv;
    }

    //approvalApprovingBtnDir        审批通过                    approval/approvingBtn
    @RequestMapping(value = "approval/approvingBtn",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject approvalApprovingBtn(@RequestBody Map param){
        System.out.println("go into approvalApprovingBtn");
        JSONObject jsonObject = null;

        try {
            Distribution distribution = distributionimpl.getOne(Integer.parseInt(param.get("distributionId").toString()));
            //更新配送员
            Employee employee = employeeimpl.getOne(Long.parseLong(param.get("distributionMan").toString()));
            distribution.setEmployee(employee);//更新配送员
            //更新主料表
            List<MainIngredient> newMainIngredientList = new ArrayList<MainIngredient>();
            List<MainIngredient> mainIngredientList = distribution.getMainingredient();

            MainIngredient mainIngredient0 = mainIngredientList.get(0);
            mainIngredient0.setMaterial(materialimpl.getOne(Integer.parseInt(param.get("zhuliao0").toString())));//种类
            mainIngredient0.setDosage(Double.parseDouble(param.get("zhuliao0number").toString()));
            newMainIngredientList.add(mainIngredient0);

            MainIngredient mainIngredient1 = mainIngredientList.get(1);
            mainIngredient1.setMaterial(materialimpl.getOne(Integer.parseInt(param.get("zhuliao1").toString())));//种类
            mainIngredient1.setDosage(Double.parseDouble(param.get("zhuliao1number").toString()));
            newMainIngredientList.add(mainIngredient1);

            MainIngredient mainIngredient2 = mainIngredientList.get(2);
            mainIngredient2.setMaterial(materialimpl.getOne(Integer.parseInt(param.get("zhuliao2").toString())));//种类
            mainIngredient2.setDosage(Double.parseDouble(param.get("zhuliao2number").toString()));
            newMainIngredientList.add(mainIngredient2);

            distribution.setMainingredient(newMainIngredientList);//更新主料表

            //更新辅料表
            List<Ingredient> newIngredientList = new ArrayList<Ingredient>();
            List<Ingredient> ingredientList = distribution.getIngredient();

            Ingredient ingredient0 = ingredientList.get(0);
            ingredient0.setMaterial(materialimpl.getOne(Integer.parseInt(param.get("fuliao0").toString())));
            ingredient0.setDosage(Double.parseDouble(param.get("fuliao0number").toString()));
            newIngredientList.add(ingredient0);

            Ingredient ingredient1 = ingredientList.get(1);
            ingredient1.setMaterial(materialimpl.getOne(Integer.parseInt(param.get("fuliao1").toString())));
            ingredient1.setDosage(Double.parseDouble(param.get("fuliao1number").toString()));
            newIngredientList.add(ingredient1);

            Ingredient ingredient2 = ingredientList.get(2);
            ingredient2.setMaterial(materialimpl.getOne(Integer.parseInt(param.get("fuliao2").toString())));
            ingredient2.setDosage(Double.parseDouble(param.get("fuliao2number").toString()));
            newIngredientList.add(ingredient2);

            distribution.setIngredient(newIngredientList);//更新辅料表

            distributionimpl.save(distribution);

            //更新申请单信息---申请单状态和审批意见
            Requisition requisition = requisitionimpl.findByDistribution(distribution);
            requisition.setMassage(param.get("distributionMsg").toString());
            requisition.setState(4);
            requisitionimpl.save(requisition);

            Result result = new Result(0, "审批完成");

            jsonObject = (JSONObject) JSONObject.toJSON(result);
        } catch (Exception e){
            System.out.println("applyChange processing failed");
            System.out.println(e.toString());
            Result result = new Result(404, "审批失败");
            jsonObject = (JSONObject) JSONObject.toJSON(result);
        }

        return jsonObject;
    }


    //approvalApprovingErrorBtnDir   审批驳回                    approval/approvingErrorBtn
    @RequestMapping(value = "approval/approvingErrorBtn",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject approvalApprovingErrorBtn(@RequestBody Map param){
        System.out.println("go into applyChange");
        JSONObject jsonObject = null;

        try {
            Distribution distribution = distributionimpl.getOne(Integer.parseInt(param.get("distributionId").toString()));
            Requisition requisition = requisitionimpl.findByDistribution(distribution);
            requisition.setState(3);
            requisition.setMassage(param.get("distributionMsg").toString());

            requisitionimpl.save(requisition);

            Result result = new Result(0, "审批完成");

            jsonObject = (JSONObject) JSONObject.toJSON(result);
        } catch (Exception e){
            System.out.println("applyChange processing failed");
            Result result = new Result(404, "审批失败");
            jsonObject = (JSONObject) JSONObject.toJSON(result);
            System.out.println(e.toString());
        }

        return jsonObject;
    }




}
