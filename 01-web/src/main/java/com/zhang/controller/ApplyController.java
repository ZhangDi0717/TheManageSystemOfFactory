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

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ApplyController {

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
     * 请求路径 ： apply/manage
     * 模块说明：
     *          1、查看各种状态的申请单
     *          2、申请单所有数据都可以修改
     *
     * 本函数功能说明：
     *          返回数据表格
     *
     * 页面响应数据:
     *      applyTableIdUrl             数据表格的请求地址            apply/table
     *      applySearchDir              根据条件搜索指定的申请单       apply/search
     *      applyAddDir                 添加申请单                  apply/add
     *      applySaveDir                监听保存                    apply/save
     *      applySubmitDir              监听提交                    apply/submit
     *      applyDeleteDir              监听删除（多个删）            apply/delete
     *
     *      applyDeleteOneDir           监听删除单个                 apply/deleteOne
     *      applyEitDir                 监听编辑                    apply/edit
     *      applyDetailDir              监听详情                    apply/detail
     *      applyChangeDir              监听修改                    apply/change
     *
     *
     * @return 相应页面
     */
    @RequestMapping(value = "apply/manage")
    private ModelAndView applyManage(){
        System.out.println("进入-----applyManage");
        ModelAndView mv = new ModelAndView();


        //响应申请/生产员工
        List<Employee> appliedList =  employeeimpl.findByPosition(positionimpl.getOne(Long.parseLong("2")));
        mv.addObject("appliedList",appliedList);
        //响应配送员工
        List<Employee> deliverymanList =  employeeimpl.findByPosition(positionimpl.getOne(Long.parseLong("4")));
        mv.addObject("deliverymanList",deliverymanList);
        //响应申请单Id
        List<Requisition> applyList = requisitionimpl.findAll();
        mv.addObject("applyList",applyList);


        mv.addObject("applyTableIdUrl","apply/table");

        mv.addObject("applySearchDir","apply/search");

        //添加申请单按钮
        mv.addObject("applyAddDir","apply/add");

        //添加申请单页面的保存按钮
        mv.addObject("applySaveDir"," apply/save");

        //添加申请单页面的提交按钮
        mv.addObject("applySubmitDir","apply/submit");

        //多个删除按钮
        mv.addObject("applyDeleteDir","apply/delete");

        //单个删除按钮
        mv.addObject("applyDeleteOneDir","apply/deleteOne");

        //编辑按钮
        mv.addObject("applyEitDir","apply/edit");

        //详情按钮
        mv.addObject("applyDetailDir","apply/detail");

        //修改按钮
        mv.addObject("applyChangeDir","apply/change");


        mv.setViewName("static/page/admin/apply");

        return mv;
    }


    // applyTableIdUrl         数据表格的请求地址           apply/table
    @RequestMapping(value = "apply/table",method = RequestMethod.POST)
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


    //applySearchDir              根据条件搜索指定的申请单       apply/search
    @RequestMapping(value = "apply/search",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject applySearch(@RequestParam(value = "applyId", required = false,defaultValue = "") String applyId,
                             @RequestParam(value = "appliedId", required = false, defaultValue = "") String appliedId,
                             @RequestParam(value = "deliverymanId", required = false, defaultValue = "") String deliverymanId,
                             @RequestParam(value = "applyState", required = false, defaultValue = "") String applyState,
                             @RequestParam(value = "dateStart",required = false,defaultValue = "") String dateStart,
                             @RequestParam(value = "dateEnd",required = false,defaultValue = "") String dateEnd,
                             @RequestParam(value = "datelineStart",required = false,defaultValue = "") String datelineStart,
                             @RequestParam(value = "datelineEnd",required = false,defaultValue = "") String datelineEnd)
    {
        System.out.println("进入------applySearch");
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


    //applyAddDir            添加申请单                  apply/add
    @RequestMapping(value = "apply/add")
    private ModelAndView applyAdd(){
        System.out.println("进入------applyAdd");
        ModelAndView mv = new ModelAndView();

        mv.setViewName("static/page/admin/applying");

        //响应原料
        List<Material> mainIngredients = materialimpl.findAllByState(1);
        mv.addObject("mainIngredients",mainIngredients);
        List<Material> ingredients = materialimpl.findAllByState(0);
        mv.addObject("ingredients",ingredients);

        //标记添加
        mv.addObject("state",0);

        //响应用户名
        mv.addObject("username","admin");
        return mv;
    }


    //applySave              监听保存                    apply/save
    @RequestMapping(value = "apply/save",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject applySave(@RequestBody Map param) {
        System.out.println("进入---applySave");

        //返回结果
        Result result = new Result();
        try{

            if(param.get("requisitionid").toString().isEmpty()){//未保存申清单
                /**
                 * 创建配送表
                 */
                Distribution distribution = new Distribution();

                Position positionTemp = positionimpl.getOne(Long.parseLong("4"));//4:代表配送人员
                List<Employee> employeeTempList = employeeimpl.findByPosition(positionTemp);
                Random random = new Random();
                int anInt = random.nextInt(employeeTempList.size());
                distribution.setEmployee(employeeTempList.get(anInt));//保存订单不分配配送人员

                /**
                 * 创建申请表
                 */
                Requisition requisition = new Requisition();
                Employee employee = employeeimpl.findByUsername(param.get("username").toString());
                requisition.setEmployee(employee);

                requisition.setDate(new Date());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                requisition.setDateline(format.parse(param.get("dateline").toString()));
                requisition.setState(0);
                requisition.setDistribution(distribution);

                distribution.setRequisition(requisition);
                Requisition save = requisitionimpl.save(requisition);

                /**
                 *
                 * 创建主料信息
                 */
                //原料0
                String material0Id = (String) param.get("zhuliao0");
                String mainIngredient0number = (String) param.get("zhuliao0number");
                MainIngredient mainIngredient0 = new MainIngredient(
                        Double.parseDouble(mainIngredient0number),
                        distribution,
                        materialimpl.getOne(Integer.parseInt(material0Id)));
                //保存到数据库
                mainIngredientimpl.save(mainIngredient0);
                //原料1
                String material1Id = (String) param.get("zhuliao1");
                String mainIngredient1number = (String) param.get("zhuliao1number");
                MainIngredient mainIngredient1 = new MainIngredient(
                        Double.parseDouble(mainIngredient1number),
                        distribution,
                        materialimpl.getOne(Integer.parseInt(material1Id)));
                //保存到数据库
                mainIngredientimpl.save(mainIngredient1);
                //原料2
                //原料1
                String material2Id = (String) param.get("zhuliao2");
                String mainIngredient2number = (String) param.get("zhuliao2number");
                MainIngredient mainIngredient2 = new MainIngredient(
                        Double.parseDouble(mainIngredient2number),
                        distribution,
                        materialimpl.getOne(Integer.parseInt(material2Id)));
                //保存到数据库
                mainIngredientimpl.save(mainIngredient2);
                /**
                 * 创建辅料信息
                 */
                //辅料0
                String ingredient0id = (String) param.get("fuliao0");
                String ingredient0number = (String) param.get("fuliao0number");
                Ingredient ingredient0 = new Ingredient(
                        Double.parseDouble(ingredient0number),
                        distribution,
                        materialimpl.getOne(Integer.parseInt(ingredient0id))
                );
                //保存到数据库
                ingredientimpl.save(ingredient0);
                //辅料1
                String ingredient1id = (String) param.get("fuliao1");
                String ingredient1number = (String) param.get("fuliao1number");
                Ingredient ingredient1 = new Ingredient(
                        Double.parseDouble(ingredient1number),
                        distribution,
                        materialimpl.getOne(Integer.parseInt(ingredient1id))
                );
                //保存到数据库
                ingredientimpl.save(ingredient1);
                //辅料2
                String ingredient2id = (String) param.get("fuliao2");
                String ingredient2number = (String) param.get("fuliao2number");
                Ingredient ingredient2 = new Ingredient(
                        Double.parseDouble(ingredient2number),
                        distribution,
                        materialimpl.getOne(Integer.parseInt(ingredient2id))
                );
                //保存到数据库
                ingredientimpl.save(ingredient2);

                /**
                 * 创建返回信息
                 */
                result.setCode(0);
                result.setMsg(save.getId().toString());
            }else {
                /*
                    提交申请单
                    1、更新配送表（主辅料表）
                    2、更新申请单状态
                 */
                //获取申请单
                Requisition requisition = requisitionimpl.getOne(Integer.parseInt(param.get("requisitionid").toString()));
//            requisition.setState(1);
                SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
                Date dateline = format.parse(param.get("dateline").toString());
                requisition.setDateline(format.parse(param.get("dateline").toString()));
                //获取配送单
                Distribution distribution = requisition.getDistribution();
                //获取辅料
                List<Ingredient> ingredients = distribution.getIngredient();
                //更新辅料
                //0
                Ingredient ingredient0 = ingredients.get(0);
                ingredient0.setDosage(Double.parseDouble((String) param.get("fuliao0number")));
                ingredient0.setMaterial(materialimpl.getOne(Integer.parseInt((String) param.get("fuliao0"))));
                //1
                Ingredient ingredient1 = ingredients.get(1);
                ingredient1.setDosage(Double.parseDouble((String) param.get("fuliao1number")));
                ingredient1.setMaterial(materialimpl.getOne(Integer.parseInt((String) param.get("fuliao1"))));
                //2
                Ingredient ingredient2 = ingredients.get(2);
                ingredient2.setDosage(Double.parseDouble((String) param.get("fuliao2number")));
                ingredient2.setMaterial(materialimpl.getOne(Integer.parseInt((String) param.get("fuliao2"))));
                //保存辅料分配单
                ingredientimpl.save(ingredient0);
                ingredientimpl.save(ingredient1);
                ingredientimpl.save(ingredient2);

                //获取主料
                List<MainIngredient> mainIngredients = distribution.getMainingredient();
                //更新辅料
                //0
                MainIngredient mainIngredient0 = mainIngredients.get(0);
                mainIngredient0.setDosage(Double.parseDouble((String) param.get("zhuliao0number")));
                mainIngredient0.setMaterial(materialimpl.getOne(Integer.parseInt((String) param.get("zhuliao0"))));
                //1
                MainIngredient mainIngredient1 = mainIngredients.get(1);
                mainIngredient1.setDosage(Double.parseDouble((String) param.get("zhuliao1number")));
                mainIngredient1.setMaterial(materialimpl.getOne(Integer.parseInt((String) param.get("zhuliao1"))));
                //2
                MainIngredient mainIngredient2 = mainIngredients.get(2);
                mainIngredient2.setDosage(Double.parseDouble((String) param.get("zhuliao2number")));
                mainIngredient2.setMaterial(materialimpl.getOne(Integer.parseInt((String) param.get("zhuliao2"))));
                //保存辅料分配单
                mainIngredientimpl.save(mainIngredient0);
                mainIngredientimpl.save(mainIngredient1);
                mainIngredientimpl.save(mainIngredient2);

                //
                requisitionimpl.save(requisition);
                /**
                 * 创建返回信息
                 */
                result.setCode(0);
                result.setMsg(requisition.getId().toString());
            }



        }catch (Exception o){//失败
            /**
             * 创建返回信息
             */
            System.out.println("处理失败------applySave");
            result.setCode(400);
            result.setMsg("保存失败");
        }

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);

        return jsonObject;
    }

    //applySubmitDir         监听提交                    apply/submit
    @RequestMapping(value = "apply/submit",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject applySubmit(@RequestBody Map param) {
        System.out.println("进入---applySubmit");
        //返回结果
        Result result = new Result();
        try{

            if(param.get("requisitionid").toString().isEmpty()){//未保存申清单
                /**
                 * 创建配送表
                 */
                Distribution distribution = new Distribution();
                Position positionTemp = positionimpl.getOne(Long.parseLong("4"));//4:代表配送人员
                List<Employee> employeeTempList = employeeimpl.findByPosition(positionTemp);
                Random random = new Random();
                int anInt = random.nextInt(employeeTempList.size());
                distribution.setEmployee(employeeTempList.get(anInt));//保存订单不分配配送人员


                /**
                 * 创建申请表
                 */
                Requisition requisition = new Requisition();
                Employee employee = employeeimpl.findByUsername(param.get("username").toString());
                requisition.setEmployee(employee);
                SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
                requisition.setDateline(format.parse(param.get("dateline").toString()));
                requisition.setDate(new Date());
                requisition.setState(1);
                requisition.setDistribution(distribution);

                distribution.setRequisition(requisition);
                Requisition save = requisitionimpl.save(requisition);

                /**
                 *
                 * 创建主料信息
                 */
                //原料0
                String material0Id = (String) param.get("zhuliao0");
                String mainIngredient0number = (String) param.get("zhuliao0number");
                MainIngredient mainIngredient0 = new MainIngredient(
                        Double.parseDouble(mainIngredient0number),
                        distribution,
                        materialimpl.getOne(Integer.parseInt(material0Id)));
                //保存到数据库
                mainIngredientimpl.save(mainIngredient0);
                //原料1
                String material1Id = (String) param.get("zhuliao1");
                String mainIngredient1number = (String) param.get("zhuliao1number");
                MainIngredient mainIngredient1 = new MainIngredient(
                        Double.parseDouble(mainIngredient1number),
                        distribution,
                        materialimpl.getOne(Integer.parseInt(material1Id)));
                //保存到数据库
                mainIngredientimpl.save(mainIngredient1);
                //原料2
                //原料1
                String material2Id = (String) param.get("zhuliao2");
                String mainIngredient2number = (String) param.get("zhuliao2number");
                MainIngredient mainIngredient2 = new MainIngredient(
                        Double.parseDouble(mainIngredient2number),
                        distribution,
                        materialimpl.getOne(Integer.parseInt(material2Id)));
                //保存到数据库
                mainIngredientimpl.save(mainIngredient2);
                /**
                 * 创建辅料信息
                 */
                //辅料0
                String ingredient0id = (String) param.get("fuliao0");
                String ingredient0number = (String) param.get("fuliao0number");
                Ingredient ingredient0 = new Ingredient(
                        Double.parseDouble(ingredient0number),
                        distribution,
                        materialimpl.getOne(Integer.parseInt(ingredient0id))
                );
                //保存到数据库
                ingredientimpl.save(ingredient0);
                //辅料1
                String ingredient1id = (String) param.get("fuliao1");
                String ingredient1number = (String) param.get("fuliao1number");
                Ingredient ingredient1 = new Ingredient(
                        Double.parseDouble(ingredient1number),
                        distribution,
                        materialimpl.getOne(Integer.parseInt(ingredient1id))
                );
                //保存到数据库
                ingredientimpl.save(ingredient1);
                //辅料2
                String ingredient2id = (String) param.get("fuliao2");
                String ingredient2number = (String) param.get("fuliao2number");
                Ingredient ingredient2 = new Ingredient(
                        Double.parseDouble(ingredient2number),
                        distribution,
                        materialimpl.getOne(Integer.parseInt(ingredient2id))
                );
                //保存到数据库
                ingredientimpl.save(ingredient2);

                /**
                 * 创建返回信息
                 */
                result.setCode(0);
                result.setMsg(save.getId().toString());
            }else {
                /*
                    提交申请单
                    1、更新配送表（主辅料表）
                    2、更新申请单状态
                 */
                //获取申请单
                Requisition requisition = requisitionimpl.getOne(Integer.parseInt(param.get("requisitionid").toString()));
                requisition.setState(1);
                SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
                requisition.setDateline(format.parse(param.get("dateline").toString()));
                //获取配送单
                Distribution distribution = requisition.getDistribution();
                //获取辅料
                List<Ingredient> ingredients = distribution.getIngredient();
                //更新辅料
                //0
                Ingredient ingredient0 = ingredients.get(0);
                ingredient0.setDosage(Double.parseDouble((String) param.get("fuliao0number")));
                ingredient0.setMaterial(materialimpl.getOne(Integer.parseInt((String) param.get("fuliao0"))));
                //1
                Ingredient ingredient1 = ingredients.get(1);
                ingredient1.setDosage(Double.parseDouble((String) param.get("fuliao1number")));
                ingredient1.setMaterial(materialimpl.getOne(Integer.parseInt((String) param.get("fuliao1"))));
                //2
                Ingredient ingredient2 = ingredients.get(2);
                ingredient2.setDosage(Double.parseDouble((String) param.get("fuliao2number")));
                ingredient2.setMaterial(materialimpl.getOne(Integer.parseInt((String) param.get("fuliao2"))));
                //保存辅料分配单
                ingredientimpl.save(ingredient0);
                ingredientimpl.save(ingredient1);
                ingredientimpl.save(ingredient2);

                //获取主料
                List<MainIngredient> mainIngredients = distribution.getMainingredient();
                //更新辅料
                //0
                MainIngredient mainIngredient0 = mainIngredients.get(0);
                mainIngredient0.setDosage(Double.parseDouble((String) param.get("zhuliao0number")));
                mainIngredient0.setMaterial(materialimpl.getOne(Integer.parseInt((String) param.get("zhuliao0"))));
                //1
                MainIngredient mainIngredient1 = mainIngredients.get(1);
                mainIngredient1.setDosage(Double.parseDouble((String) param.get("zhuliao1number")));
                mainIngredient1.setMaterial(materialimpl.getOne(Integer.parseInt((String) param.get("zhuliao1"))));
                //2
                MainIngredient mainIngredient2 = mainIngredients.get(2);
                mainIngredient2.setDosage(Double.parseDouble((String) param.get("zhuliao2number")));
                mainIngredient2.setMaterial(materialimpl.getOne(Integer.parseInt((String) param.get("zhuliao2"))));
                //保存辅料分配单
                mainIngredientimpl.save(mainIngredient0);
                mainIngredientimpl.save(mainIngredient1);
                mainIngredientimpl.save(mainIngredient2);

                //
                Requisition save = requisitionimpl.save(requisition);
                /**
                 * 创建返回信息
                 */
                result.setCode(0);
                result.setMsg(save.getId().toString());
            }



        }catch (Exception o){//失败
            /**
             * 创建返回信息
             */
            System.out.println("处理失败------applySubmit");
            result.setCode(400);
            result.setMsg("保存失败");
        }

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);

        return jsonObject;
    }


    //applyDeleteDir         监听删除（多个删）            apply/delete
    @RequestMapping(value = "apply/delete",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject applyDelete(@RequestBody List<AdminTable> adminTableList) {
        System.out.println("进入---applyDelete");

        AdminTable adminTable = null;
        JSONObject jsonObject = null;
        try {
            for (int index = 0; index < adminTableList.size(); index++) {
                adminTable = adminTableList.get(index);
                requisitionimpl.deleteById(adminTable.getId());
            }
            jsonObject = (JSONObject) JSONObject.toJSON(new Result(0, "删除成功"));
        } catch (Exception e) {
            jsonObject = (JSONObject) JSONObject.toJSON(new Result(400, "删除失败"));
        }
        return jsonObject;

    }

    //applyDeleteOneDir      监听删除单个                 apply/deleteOne
    @RequestMapping(value = "apply/deleteOne",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject applyDeleteOne(@RequestBody AdminTable adminTable) {
        System.out.println("进入---applyDeleteOne");
        JSONObject jsonObject = null;
        try {
            requisitionimpl.deleteById(adminTable.getId());
            jsonObject = (JSONObject) JSONObject.toJSON(new Result(0,"删除成功"));
        }catch (Exception e){
            jsonObject = (JSONObject) JSONObject.toJSON(new Result(400,"删除失败"));
        }
        return jsonObject;

    }

    //applyEitDir            监听编辑                    apply/edit
    @RequestMapping("apply/edit")
    private ModelAndView applyEdit(String id,String v){
        System.out.println("进入------applyDetail");
        ModelAndView mv = new ModelAndView();

        Requisition requisition = requisitionimpl.getOne(Integer.parseInt(id));

        Integer requisitionId = requisition.getId();
        mv.addObject("requisitionId",requisitionId);

        Date dateline = requisition.getDateline();
        String s = dateline.toString();
        mv.addObject("dateline",dateline);

        Distribution distribution = requisition.getDistribution();
        List<Ingredient> ingredientSelects = distribution.getIngredient();
        Ingredient ingredient0 = ingredientSelects.get(0);
        Ingredient ingredient1 = ingredientSelects.get(1);
        Ingredient ingredient2 = ingredientSelects.get(2);
        mv.addObject("ingredient0id",ingredient0.getMaterial().getId());
        mv.addObject("ingredient0number",ingredient0.getDosage());
        mv.addObject("ingredient1id",ingredient1.getMaterial().getId());
        mv.addObject("ingredient1number",ingredient1.getDosage());
        mv.addObject("ingredient2id",ingredient2.getMaterial().getId());
        mv.addObject("ingredient2number",ingredient2.getDosage());


        List<MainIngredient> mainingredientSelects = distribution.getMainingredient();
        MainIngredient mainIngredient0 = mainingredientSelects.get(0);
        MainIngredient mainIngredient1 = mainingredientSelects.get(1);
        MainIngredient mainIngredient2 = mainingredientSelects.get(2);
        mv.addObject("mainIngredient0id",mainIngredient0.getMaterial().getId());
        mv.addObject("mainIngredient0number",mainIngredient0.getDosage());
        mv.addObject("mainIngredient1id",mainIngredient1.getMaterial().getId());
        mv.addObject("mainIngredient1number",mainIngredient1.getDosage());
        mv.addObject("mainIngredient2id",mainIngredient2.getMaterial().getId());
        mv.addObject("mainIngredient2number",mainIngredient2.getDosage());

        List<Material> mainIngredients = materialimpl.findAllByState(1);
//        System.out.println(mainIngredients);
        mv.addObject("mainIngredients",mainIngredients);

        List<Material> ingredients = materialimpl.findAllByState(0);
//        System.out.println(ingredients);
        mv.addObject("ingredients",ingredients);

        mv.addObject("state",1);

        mv.setViewName("static/page/admin/applying");
        return mv;
    }


    //applyDetailDir         监听详情                    apply/detail
    @RequestMapping("apply/detail")
    private ModelAndView applyDetail(String id,String v){
        System.out.println("进入------applyDetail");
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

        mv.setViewName("static/page/admin/apply-detail");
        return mv;
    }


    //applyChangeDir         监听修改                    apply/change
    @RequestMapping(value = "apply/change",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject applyChange(@RequestBody Map param){
        System.out.println("go into applyChange");
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

            Result result = new Result(0, "修改成功");

            jsonObject = (JSONObject) JSONObject.toJSON(result);
        } catch (Exception e){
            System.out.println("applyChange processing failed");
            Result result = new Result(404, "修改失败");
            jsonObject = (JSONObject) JSONObject.toJSON(result);
            System.out.println(e.toString());
        }

        return jsonObject;
    }


}
