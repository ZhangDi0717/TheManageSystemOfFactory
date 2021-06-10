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
        HomeInfo homeInfo = new HomeInfo("首页","/admin/apply/manage");
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

        /****  添加三级菜单 START ****/

//      Son son1 = new Son("申请材料","applying","fa fa-tachometer","_self");
//      sons1.add(son1);

        //申请单功能演示
//        Son son1_2 = new Son("查看申请","page/apply/applied.html","fa fa-tachometer","_self");
//        sons1.add(son1_2);
//
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

        Child child2_3 = new Child("权限管理","","fa fa-file-text-o","_self",sons5);
        children2.add(child2_3);

        Child child2_4 = new Child("个人管理","","fa fa-file-text-o","_self",sons2);
        children2.add(child2_4);

        /****  添加二级菜单 END ****/

//        MenuInfo menuInfo1 = new MenuInfo("功能模块管理","fa fa-address-book","","_self",children1);
        MenuInfo menuInfo2 = new MenuInfo("管理模块","fa fa-address-book","","_self",children2);


//        menuInfos.add(menuInfo1);
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
     *      adminApplyAddDir            添加申请单                  apply/add
     *      adminApplySaveDir           监听保存                    apply/save
     *      adminApplySubmitDir         监听提交                    apply/submit
     *      adminApplyDeleteDir         监听删除（多个删）            apply/delete
     *
     *      adminApplyDeleteOneDir      监听删除单个                 apply/deleteOne
     *      adminApplyEitDir            监听编辑                    apply/edit
     *      adminApplyDetailDir         监听详情                    apply/detail
     *      adminApplyChangeDir         监听修改                    apply/change
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

        //添加申请单按钮
        mv.addObject("adminApplyAddDir","apply/add");

        //添加申请单页面的保存按钮
        mv.addObject("adminApplySaveDir"," apply/save");

        //添加申请单页面的提交按钮
        mv.addObject("adminApplySubmitDir","apply/submit");

        //多个删除按钮
        mv.addObject("adminApplyDeleteDir","apply/delete");

        //单个删除按钮
        mv.addObject("adminApplyDeleteOneDir","apply/deleteOne");

        //编辑按钮
        mv.addObject("adminApplyEitDir","apply/edit");

        //详情按钮
        mv.addObject("adminApplyDetailDir","apply/detail");

        //修改按钮
        mv.addObject("adminApplyChangeDir","apply/change");


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
                             @RequestParam(value = "datelineEnd",required = false,defaultValue = "") String datelineEnd)
    {
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


    //adminApplyAddDir            添加申请单                  apply/add
    @RequestMapping(value = "admin/apply/add")
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

    //adminApplySave              监听保存                    apply/save
    @RequestMapping(value = "admin/apply/save",method = RequestMethod.POST)
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

    //adminApplySubmitDir         监听提交                    apply/submit    admin/applying/submit
    @RequestMapping(value = "admin/apply/submit",method = RequestMethod.POST)
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


    //adminApplyDeleteDir         监听删除（多个删）            apply/delete   /admin/apply/delete
    @RequestMapping(value = "admin/apply/delete",method = RequestMethod.POST)
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

    //adminApplyDeleteOneDir      监听删除单个                 apply/deleteOne
    @RequestMapping(value = "admin/apply/deleteOne",method = RequestMethod.POST)
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


    //adminApplyEitDir            监听编辑                    apply/edit
    @RequestMapping("admin/apply/edit")
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

    //adminApplyDetailDir         监听详情                    apply/detail
    @RequestMapping("admin/apply/detail")
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


    //adminApplyChangeDir         监听修改                    apply/change
    @RequestMapping(value = "admin/apply/change",method = RequestMethod.POST)
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



    /**
     * 模块说明：
     *          1、查看职员的各种信息----职位-----个人信息------他们负责的订单
     *          2、添加删除职员
     *          3、角色管理----增删改查
     *
     * 本函数功能说明：
     *          返回数据表格
     *
     * 页面响应数据:
     *      adminEmployeeTableDir       员工数据表格的请求地址        employee/table
     *      adminAddEmployeeDir         响应员工添加页面             employee/add
     *      adminAddEmployeeStepFirDir  保存主要信息                employee/stepFir
     *      adminAddEmployeeStepSecDir  保存个人信息                employee/stepSec
     *      adminEmployeeSearchDir      搜索框                     employee/search
     *      adminEmployeeDeleteMoreDir  删除多个                    employee/deleteMore
     *      adminEmployeeDeleteOneDir   删除单个                    employee/deleteOne
     *      adminEmployeeDetailDir      详情按钮                    employee/detail
     *      adminEmployeeChangeBasicDir 修改员工基本信息              employee/changeBasic
     *      adminEmployeeChangeMainDir  修改员工主要信息              employee/changeMain
     *
     *
     *
     *      adminSearchByIdDir          根据申请单Id搜索
     *      adminSearchDir              根据条件搜索指定的申请单       apply/search
     *      adminApplyAddDir            添加申请单                  apply/add
     *      adminApplySaveDir           监听保存                    apply/save
     *      adminApplySubmitDir         监听提交                    apply/submit
     * @return 员工管理页面
     */
    @RequestMapping(value = "admin/employee/manage")
    private ModelAndView employeeManage(){
        System.out.println("进入-----employeeManage");
        ModelAndView mv = new ModelAndView();
        mv.addObject("adminEmployeeTableDir","employee/table");

        mv.addObject("adminAddEmployeeDir","employee/add");

        mv.addObject("adminAddEmployeeStepSecDir","employee/stepSec");

        mv.addObject("adminEmployeeSearchDir","employee/search");
        List<Employee> employeeList = employeeimpl.findAll();
        mv.addObject("employeeList",employeeList);
        List<Position> positionList = positionimpl.findAll();
        mv.addObject("positionList",positionList);

        mv.addObject("adminEmployeeDeleteMoreDir","employee/deleteMore");
        mv.addObject("adminEmployeeDeleteOneDir","employee/deleteOne");

        mv.addObject("adminEmployeeDetailDir","employee/detail");

        mv.setViewName("static/page/admin/employee");

        return mv;
    }

   //adminEmployeeTableDir       员工数据表格的请求地址        employee/table
   @RequestMapping(value = "admin/employee/table",method = RequestMethod.POST)
   @ResponseBody
   public JSONObject employeeTable(){
       System.out.println("进入------employeeTable");

       JSONObject jsonObject = new JSONObject();
       EmployeeTableResult employeeTableResult = null;

       try {
           List<Employee> employeeList = employeeimpl.findAll();
           if( employeeList.size()==0 ){
               employeeTableResult = new EmployeeTableResult(404,"暂无职员信息",0,null);
           }else {//有职员信息
               List<EmployeeTable> employeeTableList = new ArrayList<EmployeeTable>();
               for (Employee employee :employeeList){

                   //获取职员的职位
                   String positionString = "";
                   String sexString = "";

                   Set<Position> positionSet = employee.getPosition();
                   for (Position position : positionSet) {
                       positionString = positionString.concat(position.getName()+"/");
                   }
                   //消失上个步骤产生的额外字符
                   positionString = positionString.substring(0, positionString.length() - 1);

                   if( employee.getEmployeeinformation().getSex()==0 ){
                       sexString = "男";
                   }else {
                       sexString = "女";
                   }

                   EmployeeTable employeeTable = new EmployeeTable(
                           employee.getId(),
                           employee.getUsername(),
                           employee.getEmployeeinformation().getName(),
                           sexString,
                           positionString,
                           employee.getEmployeeinformation().getPhone()

                   );

                   employeeTableList.add(employeeTable);


               }
               employeeTableResult = new EmployeeTableResult(0,"员工信息请求成功",employeeTableList.size(),employeeTableList);

           }
       }catch (Exception e){
           System.out.println("处理失败------employeeTable");
           employeeTableResult = new EmployeeTableResult(0,"员工信息请求失败",0,null);
       }

       jsonObject = (JSONObject) JSONObject.toJSON(employeeTableResult);

       return jsonObject;
   }

   //adminAddEmployeeDir         响应员工添加页面             employee/add
   @RequestMapping(value = "admin/employee/add")//添加用户响应页面
   private ModelAndView employeeAdd(){
       System.out.println("进入------employeeAdd");
       ModelAndView mv = new ModelAndView();

       mv.setViewName("static/page/admin/employeeing");
       List<Position> positionList = positionimpl.findAll();
       mv.addObject("positionList",positionList);

       mv.addObject("adminAddEmployeeStepFirDir","employee/stepFir");

       mv.addObject("adminAddEmployeeStepSecDir","employee/stepSec");

       //标记添加
       mv.addObject("state",0);

       //响应用户名
       mv.addObject("username","admin");
       return mv;
   }

   //adminAddEmployeeStepFirDir  保存主要信息                 employee/stepFir
    @RequestMapping(value = "admin/employee/stepFir")
    @ResponseBody
    private JSONObject employeeStepFir(
            @RequestBody Map param,//username: "1", new_password: "111111", again_password: "111111"
            HttpServletRequest httpServletRequest){

        System.out.println("进入------employeeStepFir");
        JSONObject jsonObject = new JSONObject();
        String username = param.get("username").toString();
        String password = param.get("new_password").toString();
        Employee employee = new Employee();
        Result result = null;
        Long employeeId = (Long) httpServletRequest.getSession().getAttribute("employeeId");




//        try {
            //判断用户名是否已存在
            Boolean exit = employeeimpl.existsByUsername(username);
            if(exit&&employeeId==null){
                result = new Result(2,"该用户已存在");
            }else {

                if( employeeId!=null ){
                    employee.setId(employeeId);
                    Employee employee1 = employeeimpl.getOne(employeeId);
                    PasswordEncoder encoder = new BCryptPasswordEncoder();
                    employee1.setPassword(encoder.encode(password));
                    employeeimpl.save(employee1);

                }else {
                    PasswordEncoder encoder = new BCryptPasswordEncoder();
                    employee.setPassword(encoder.encode(password));
                    employee.setUsername(username);
                    employee.setCreatetime(new Date());
                    employee.setLogintime(new Date());
                    //四个true
                    employee.setIscredentials(true);
                    employee.setIsenable(true);
                    employee.setIslock(true);
                    employee.setIsexpired(true);

                    EmployeeInformation employeeInformation = new EmployeeInformation();
                    employeeInformation.setEmployee(employee);
                    employee.setEmployeeinformation(employeeInformation);



                    Set<Position> positionSet = new HashSet<Position>();
                    employee.setPosition(new HashSet<Position>());


                    Employee save = employeeimpl.save(employee);
                    //将employee存入session中
                    httpServletRequest.getSession().setAttribute("employeeId",save.getId());
                }


                result = new Result(0,"保存成功");
            }


//        }
//        catch (Exception e){
//            result = new Result(404,"服务器处理失败");
//        }

        jsonObject =  (JSONObject)JSONObject.toJSON(result);
        return jsonObject;

    }



    //{name: "丁聪", position: "1", sex: "1", phone: "11111111", email: "111111",address}
    //adminAddEmployeeStepSecDir  保存个人信息                 employee/stepSec
    @RequestMapping(value = "admin/employee/stepSec")
    @ResponseBody
    public JSONObject employeeStepSec(
            @RequestBody Map param,
            HttpServletRequest httpServletRequest){
        System.out.println("进入------employeeStepSec");

        Result result = null;

        try {
            Long employeeId = (Long)httpServletRequest.getSession().getAttribute("employeeId");//获取ID

            Employee employee = employeeimpl.getOne(employeeId);

            Position position = positionimpl.getOne(Long.parseLong(param.get("position").toString()));
            Set<Position> positionSet = employee.getPosition();
            positionSet.add(position);
            employee.setPosition(positionSet);



            EmployeeInformation employeeinformation = employee.getEmployeeinformation();
            employeeinformation.setName(param.get("name").toString());
            employeeinformation.setAddress(param.get("address").toString());
            employeeinformation.setPhone(param.get("phone").toString());
            employeeinformation.setSex(Integer.parseInt(param.get("sex").toString()));

            employeeinformationimpl.save(employeeinformation);

            result = new Result(0,"个人信息添加成功");

            httpServletRequest.getSession().removeAttribute("employeeId");//去除ID


        }catch (Exception e){
            System.out.println("处理失败------employeeStepSec");
            result = new Result(404,"个人信息添加失败");
        }

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);
        return jsonObject;
    }



    //adminEmployeeSearchDir      搜索框                     employee/search
    @RequestMapping(value = "admin/employee/search")
    @ResponseBody
    public JSONObject employeeSearch(
            @RequestParam(value = "employeeId",required = false, defaultValue = "") String employeeId,
            @RequestParam(value = "position",required = false,defaultValue = "") String positionId,
            @RequestParam(value = "sex", required = false, defaultValue = "") String sex,
            @RequestParam(value = "username", required = false, defaultValue = "") String username){
        System.out.println("go into employeeSearch");

        EmployeeTableResult employeeTableResult = new EmployeeTableResult();
        List<EmployeeTable> employeeTableList = new ArrayList<EmployeeTable>();
        JSONObject jsonObject = new JSONObject();

        try {
            boolean flag = true;

            //employeeId 查询
            if( !employeeId.isEmpty()){

                List<EmployeeTable> tableList = new ArrayList<EmployeeTable>();

                Employee employee = employeeimpl.getOne(Long.parseLong(employeeId));
                Set<Position> positionSet = employee.getPosition();

                //获取职员的职位
                String positionString = "";
                String sexString = "";

                for (Position position : positionSet) {
                    positionString = positionString.concat(position.getName()+"/");
                }
                //消失上个步骤产生的额外字符
                positionString = positionString.substring(0, positionString.length() - 1);

                if( employee.getEmployeeinformation().getSex()==0 ){
                    sexString = "男";
                }else {
                    sexString = "女";
                }

                EmployeeTable employeeTable = new EmployeeTable(
                        employee.getId(),
                        employee.getUsername(),
                        employee.getEmployeeinformation().getName(),
                        sexString,
                        positionString,
                        employee.getEmployeeinformation().getPhone()

                );
                tableList.add(employeeTable);

                if( flag ){//并集(先做差集再做添加所有）
                    employeeTableList.removeAll(tableList); // bingList为 [1, 2]
                    employeeTableList.addAll(tableList);  //添加[3,4,5,6]
                    flag = false;
                }else {//交集
                    employeeTableList.retainAll(tableList);
                }


            }

            if( !positionId.isEmpty()){
                List<EmployeeTable> tableList = new ArrayList<EmployeeTable>();

                List<Employee> employeeList =  employeeimpl.findByPosition(positionimpl.getOne(Long.parseLong(positionId)));


                for (Employee employee:employeeList){
                    Set<Position> positionSet = employee.getPosition();

                    //获取职员的职位
                    String positionString = "";
                    String sexString = "";

                    for (Position position : positionSet) {
                        positionString = positionString.concat(position.getName()+"/");
                    }
                    //消失上个步骤产生的额外字符
                    positionString = positionString.substring(0, positionString.length() - 1);

                    if( employee.getEmployeeinformation().getSex()==0 ){
                        sexString = "男";
                    }else {
                        sexString = "女";
                    }

                    EmployeeTable employeeTable = new EmployeeTable(
                            employee.getId(),
                            employee.getUsername(),
                            employee.getEmployeeinformation().getName(),
                            sexString,
                            positionString,
                            employee.getEmployeeinformation().getPhone()

                    );
                    tableList.add(employeeTable);
                }

                if( flag ){//并集(先做差集再做添加所有）
                    employeeTableList.removeAll(tableList); // bingList为 [1, 2]
                    employeeTableList.addAll(tableList);  //添加[3,4,5,6]
                    flag = false;
                }else {//交集
                    employeeTableList.retainAll(tableList);
                }
            }

            if( !sex.isEmpty()){
                List<EmployeeInformation> employeeInformationList = employeeinformationimpl.findBySex(Integer.parseInt(sex));

                List<EmployeeTable> tableList = new ArrayList<EmployeeTable>();
                for (EmployeeInformation employeeInformation:employeeInformationList){
                    Employee employee = employeeInformation.getEmployee();
                    Set<Position> positionSet = employee.getPosition();

                    //获取职员的职位
                    String positionString = "";
                    String sexString = "";

                    for (Position position : positionSet) {
                        positionString = positionString.concat(position.getName()+"/");
                    }
                    //消失上个步骤产生的额外字符
                    positionString = positionString.substring(0, positionString.length() - 1);

                    if( employee.getEmployeeinformation().getSex()==0 ){
                        sexString = "男";
                    }else {
                        sexString = "女";
                    }

                    EmployeeTable employeeTable = new EmployeeTable(
                            employee.getId(),
                            employee.getUsername(),
                            employee.getEmployeeinformation().getName(),
                            sexString,
                            positionString,
                            employee.getEmployeeinformation().getPhone()

                    );
                    tableList.add(employeeTable);
                }

                if( flag ){//并集(先做差集再做添加所有）
                    employeeTableList.removeAll(tableList); // bingList为 [1, 2]
                    employeeTableList.addAll(tableList);  //添加[3,4,5,6]
                    flag = false;
                }else {//交集
                    employeeTableList.retainAll(tableList);
                }
            }

            if( !username.isEmpty()){

                List<EmployeeTable> tableList = new ArrayList<EmployeeTable>();

                Employee employee = employeeimpl.findByUsername(username);
                Set<Position> positionSet = employee.getPosition();

                //获取职员的职位
                String positionString = "";
                String sexString = "";

                for (Position position : positionSet) {
                    positionString = positionString.concat(position.getName()+"/");
                }
                //消失上个步骤产生的额外字符
                positionString = positionString.substring(0, positionString.length() - 1);

                if( employee.getEmployeeinformation().getSex()==0 ){
                    sexString = "男";
                }else {
                    sexString = "女";
                }

                EmployeeTable employeeTable = new EmployeeTable(
                        employee.getId(),
                        employee.getUsername(),
                        employee.getEmployeeinformation().getName(),
                        sexString,
                        positionString,
                        employee.getEmployeeinformation().getPhone()

                );
                tableList.add(employeeTable);

                if( flag ){//并集(先做差集再做添加所有）
                    employeeTableList.removeAll(tableList); // bingList为 [1, 2]
                    employeeTableList.addAll(tableList);  //添加[3,4,5,6]
                    flag = false;
                }else {//交集
                    employeeTableList.retainAll(tableList);
                }
            }

            employeeTableResult.setCode(0);
            employeeTableResult.setCount(employeeTableList.size());
            employeeTableResult.setMsg("查询成功");
            employeeTableResult.setData(employeeTableList);

            jsonObject = (JSONObject) JSONObject.toJSON(employeeTableResult);
        }catch (Exception e){
            System.out.println("employeeSearch processing failed");
            jsonObject = (JSONObject) JSONObject.toJSON(new EmployeeTableResult(404,"查询错误",0,null));
        }


        return jsonObject;
    }


    //adminEmployeeDeleteMoreDir  删除多个                    employee/deleteMore  admin/employee/deleteMore
    @RequestMapping(value = "admin/employee/deleteMore",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject employeeDeleteMore(@RequestBody List<EmployeeTable> employeeTableList){
        System.out.println("go into employeeDeleteMore");
        Result result = null;
        try {
            for (EmployeeTable employeeTable:employeeTableList){
                Long employeeId = employeeTable.getEmployeeId();
                Employee employee = employeeimpl.getOne(employeeId);
                employeeimpl.delete(employee);
            }

            result = new Result(0,"删除成功");

        }catch (Exception e){
            System.out.println("employeeDeleteMore processing failed");
            result = new Result(404,"删除失败");
        }
        return (JSONObject) JSONObject.toJSON(result);
    }

    //adminEmployeeDeleteOneDir   删除单个                    employee/deleteOne
    @RequestMapping(value = "admin/employee/deleteOne",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject employeeDeleteOne(@RequestBody EmployeeTable employeeTable){
        System.out.println("go into employeeDeleteOne");
        Result result = null;
        try {
            Long employeeId = employeeTable.getEmployeeId();
            Employee employee = employeeimpl.getOne(employeeId);
            employeeimpl.delete(employee);
            result = new Result(0,"删除成功");
        }catch (Exception e){
            System.out.println("employeeDeleteOne processing failed");
            result = new Result(404,"删除失败");
        }
        return (JSONObject) JSONObject.toJSON(result);
    }


    //adminEmployeeDetailDir      详情按钮                    employee/detail
    @RequestMapping(value = "admin/employee/detail")
    @ResponseBody
    public ModelAndView employeeDetail(String employeeId){

        ModelAndView mv = new ModelAndView();

        Employee employee = employeeimpl.getOne(Long.parseLong(employeeId));
        EmployeeInformation employeeInformation = employee.getEmployeeinformation();
        Set<Position> positionSet = employee.getPosition();
        List<Position> positionList = positionimpl.findAll();

        mv.addObject("employee",employee);
        mv.addObject("employeeInformation",employeeInformation);
        mv.addObject("positionSet",positionSet);
        mv.addObject("positionList",positionList);

        mv.addObject("adminEmployeeChangeBasicDir","employee/changeBasic");
        mv.addObject("adminEmployeeChangeMainDir","employee/changeMain");

        mv.setViewName("static/page/admin/employee-detail");
        return mv;
    }

    //adminEmployeeChangeBasicDir 修改员工基本信息              employee/changeBasic
    @RequestMapping(value = "admin/employee/changeBasic",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject employeeChangeBasic(@RequestBody Map param){

        System.out.println("go into employeeChangeBasic");
        JSONObject jsonObject = null;

        try {
            Employee employee = employeeimpl.getOne(Long.parseLong(param.get("employeeId").toString()));

            if(param.get("isexpired")!=null)//账户过期
                employee.setIsexpired(false);

            if(param.get("islock")!=null)//账户被锁定
                employee.setIslock(false);

            if(param.get("iscredentials")==null)//账户凭证失效
                employee.setIscredentials(false);

            if (param.get("isenable")==null)//账户失能
                employee.setIsenable(false);

            employeeimpl.save(employee);
            jsonObject = (JSONObject) JSONObject.toJSON(new Result(0,"修改成功"));

        } catch (Exception e){
            System.out.println("employeeChangeBasi processing failed");
            System.out.println(e.toString());
            jsonObject = (JSONObject) JSONObject.toJSON(new Result(404,"修改失败"));

        }

        return jsonObject;

    }


    //adminEmployeeChangeMainDir  修改员工主要信息              employee/changeMain
    @RequestMapping(value = "admin/employee/changeMain",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject employeeChangeMain(@RequestBody Map param){
        System.out.println("go into employeeChangeMain");
        JSONObject jsonObject = null;
        Employee employee = employeeimpl.getOne(Long.parseLong(param.get("employeeId").toString()));

        //更新员工信息
        EmployeeInformation employeeinformation = employee.getEmployeeinformation();
        employeeinformation.setAddress(param.get("address").toString());
        employeeinformation.setEmail(param.get("email").toString());
        employeeinformation.setName(param.get("name").toString());
        employeeinformation.setPhone(param.get("phone").toString());
        employeeinformation.setSex(Integer.parseInt(param.get("sex").toString()));

        Set<Position> newPositionSet = new HashSet<Position>();
        newPositionSet.add(positionimpl.getOne(Long.parseLong(param.get("position").toString())));

        employee.setPosition(newPositionSet);
        employee.setEmployeeinformation(employeeinformation);

        employeeimpl.save(employee);

        Result result = new Result(0, "修改成功");


        return (JSONObject) JSONObject.toJSON(result);
    }



    /**
     *  权限管理
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