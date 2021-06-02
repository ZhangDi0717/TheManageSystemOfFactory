package com.zhang.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhang.dao.*;
import com.zhang.entity.*;
import com.zhang.vo.InformationResult;
import com.zhang.vo.MaterialResult;
import com.zhang.vo.PageInit;
import com.zhang.vo.Result;
import com.zhang.vo.applyTableResult.ApplyTable;
import com.zhang.vo.applyTableResult.ApplyTableResult;
import com.zhang.vo.initPage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class ApplyController {

    @Autowired
    private EmployeeImpl employeeimpl;

    @Autowired
    private RequisitionImpl requisitionimpl;

    @Autowired
    private DistributionImpl distributionimpl;

    @Autowired
    private MaterialImpl materialimpl;

    @Autowired
    private MainIngredientImpl mainIngredientimpl;

    @Autowired
    private IngredientImpl ingredientimpl;

    @Autowired
    private EmployeeInformationImpl employeeInformationImpl;

    //响应主页
    @RequestMapping("applypage")
    private ModelAndView homePage(String username){
        System.out.println("进入-------homePage");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("templates/applypage");
        mv.addObject("username",username);
        mv.addObject("informationDir","applied/information?username="+username);
        mv.addObject("initDir","apply/init");
        return mv;
    }

    //初始化目录
    @RequestMapping(value = "apply/init",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject init(){
        System.out.println("进入--------init");

        PageInit pageInit = new PageInit();
        //  1
        HomeInfo homeInfo = new HomeInfo("主页","page/apply/applied.html");
        pageInit.setHomeInfo(homeInfo);
        //  2
        LogoInfo logoInfo = new LogoInfo("后台管理系统","images/logo.png","");
        pageInit.setLogoInfo(logoInfo);
        //  3
        List<MenuInfo> menuInfos = new ArrayList<MenuInfo>();

        List<Son> sons1 = new ArrayList<Son>();
        List<Son> sons2 = new ArrayList<Son>();

        /****  添加三级菜单 START ****/

//        Son son1 = new Son("申请材料","applying","fa fa-tachometer","_self");
//        sons1.add(son1);

        Son son2 = new Son("查看申请","page/apply/applied.html","fa fa-tachometer","_self");
        sons1.add(son2);

        Son son3 = new Son("个人信息","page/user-setting.html","fa fa-tachometer","_self");
        sons2.add(son3);

        Son son4 = new Son("修改密码","page/user-password.html","fa fa-tachometer","_self");
        sons2.add(son4);

        /****  添加三级菜单 EDN ****/


        List<Child> children = new ArrayList<Child>();

        /****  添加二级菜单 START ****/

        Child child1 = new Child("申请单","","fa fa-file-text-o","_self",sons1);
        children.add(child1);

        Child child2 = new Child("个人设置","","fa fa-user","_self",sons2);
        children.add(child2);

        /****  添加二级菜单 END ****/

        MenuInfo menuInfo = new MenuInfo("常规管理","fa fa-address-book","","_self",children);

        menuInfos.add(menuInfo);

        pageInit.setMenuInfo(menuInfos);

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(pageInit);

        return jsonObject;
    }

    //申请原料
    @RequestMapping("applying")
    private ModelAndView saveRequition(){
        System.out.println("进入------saveRequition");
        ModelAndView mv = new ModelAndView();
        List<Material> mainIngredients = materialimpl.findAllByState(1);
//        System.out.println(mainIngredients);
        mv.addObject("mainIngredients",mainIngredients);

        List<Material> ingredients = materialimpl.findAllByState(0);
//        System.out.println(ingredients);
        mv.addObject("ingredients",ingredients);

        mv.addObject("state",0);

        mv.setViewName("static/page/apply/applying");
        return mv;

    }

    //保存订单
    @RequestMapping(value = "applying/save",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject save(@RequestBody Map param) {
        System.out.println("进入---save");

        //返回结果
        Result result = new Result();
        try{

        if(param.get("requisitionid").toString().isEmpty()){//未保存申清单
            /**
             * 创建配送表
             */
            Distribution distribution = new Distribution();

            distribution.setEmployee(employeeimpl.findByUsername("zhangsan"));

            /**
             * 创建申请表
             */
            Requisition requisition = new Requisition();
            Employee employee = employeeimpl.findByUsername(param.get("username").toString());
            requisition.setEmployee(employee);

            requisition.setDate(new Date());
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
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
            result.setCode(400);
            result.setMsg("保存失败");
        }

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);

        return jsonObject;
    }

    //提交订单
    @RequestMapping(value = "applying/submit",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject submit(@RequestBody Map param) {
        System.out.println("进入---submit");
        //返回结果
        Result result = new Result();
        try{

            if(param.get("requisitionid").toString().isEmpty()){//未保存申清单
                /**
                 * 创建配送表
                 */
                Distribution distribution = new Distribution();

                distribution.setEmployee(employeeimpl.findByUsername("zhangsan"));

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
            result.setCode(400);
            result.setMsg("保存失败");
        }

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);

        return jsonObject;
    }


    @RequestMapping("applied/edit")
    private ModelAndView editRequition(String id,String v){
        System.out.println("进入------editRequition");
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

        mv.setViewName("static/page/apply/applying");
        return mv;
    }




    //响应申请单数据表格
    @RequestMapping(value = "applied/table",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getTable(String username,String page,String limit) {
        System.out.println("进入---getTable");

        int start = (Integer.parseInt(page)-1)*Integer.parseInt(limit);
        int end = Integer.parseInt(limit)+start;

        JSONObject jsonObject = null;
        try {
            //获取职员
            Employee employee = employeeimpl.findByUsername(username);
            //从数据库中获取订单
            List<Requisition> requisitionList = requisitionimpl.getByEmployee(employee);
            //封装响应数据

            ApplyTableResult applyTableResult = new ApplyTableResult();
            applyTableResult.setCode(0);
            applyTableResult.setCount(requisitionList.size());
            applyTableResult.setMsg("申请单获取成功");
            List<ApplyTable> applyTableList = new ArrayList<ApplyTable>();
            ApplyTable applyTable = null;
            Requisition requisition = null;
            for (int index=start; (index<end) && (index<requisitionList.size()) ; index++) {
                requisition = requisitionList.get(index);
                applyTable = new ApplyTable(
                        requisition.getId(),
                        requisition.getDateline(),
                        requisition.getDate(),
                        requisition.getState(),
                        requisition.getState());
                applyTableList.add(applyTable);
            }
            applyTableResult.setData(applyTableList);

            jsonObject = (JSONObject) JSONObject.toJSON(applyTableResult);
        }catch (Exception e){
            ApplyTableResult applyTableResult = new ApplyTableResult();
            applyTableResult.setCode(1);
            applyTableResult.setCount(0);
            applyTableResult.setMsg("申请单获取失败");
            System.out.println("申请单获取失败");
            jsonObject = (JSONObject) JSONObject.toJSON(applyTableResult);
        }
        return jsonObject;
    }



    @RequestMapping(value = "applied/delete",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(@RequestBody List<ApplyTable> applyTableList) {
        System.out.println("进入---delete");

        ApplyTable applyTable = null;
        JSONObject jsonObject = null;
        try {
            for (int index = 0; index < applyTableList.size(); index++) {
                applyTable = applyTableList.get(index);
                requisitionimpl.deleteById(applyTable.getId());
            }
            jsonObject = (JSONObject) JSONObject.toJSON(new Result(0, "删除成功"));
        } catch (Exception e) {
            jsonObject = (JSONObject) JSONObject.toJSON(new Result(400, "删除失败"));
        }
        return jsonObject;

    }

    //删除一个
    @RequestMapping(value = "applied/deleteone",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteOne(@RequestBody ApplyTable applyTable) {
        System.out.println("进入---deleteOne");
        JSONObject jsonObject = null;
        try {
            requisitionimpl.deleteById(applyTable.getId());
            jsonObject = (JSONObject) JSONObject.toJSON(new Result(0,"删除成功"));
        }catch (Exception e){
            jsonObject = (JSONObject) JSONObject.toJSON(new Result(400,"删除失败"));
        }
            return jsonObject;

    }

    //指定查找
    @RequestMapping(value = "applied/search",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject search(@RequestParam(value = "id", required = false,defaultValue = "") String id,
                             @RequestParam(value = "state", required = false, defaultValue = "") String state) {
        System.out.println("进入---search");
        JSONObject jsonObject = null;
        try {
            ApplyTableResult applyTableResult = new ApplyTableResult();

            if( (id.isEmpty())&&(state.isEmpty()) ){//全空
                applyTableResult.setCode(204);
                applyTableResult.setData(null);
                applyTableResult.setCount(0);
                applyTableResult.setMsg("搜索框为空");
            }else if( !id.isEmpty()&&(state.isEmpty()) ){//查询Id
                Requisition requisition = requisitionimpl.getOne(Integer.parseInt(id));
                applyTableResult.setCode(0);
                applyTableResult.setMsg("申请单编号查询成功");
                applyTableResult.setCount(1);

                List<ApplyTable> applyTableList = new ArrayList<ApplyTable>();
                applyTableList.add(new ApplyTable(
                        requisition.getId(),
                        requisition.getDateline(),
                        requisition.getDate(),
                        requisition.getState(),
                        requisition.getState()
                ));
                applyTableResult.setData(applyTableList);
            }else if( id.isEmpty()&&!state.isEmpty() ){//查询状态
                List<Requisition> requisitionList =  requisitionimpl.findByState(Integer.parseInt(state));
                applyTableResult.setCode(0);
                applyTableResult.setMsg("申请单审核状态查询成功");
                applyTableResult.setCount(requisitionList.size());

                List<ApplyTable> applyTableList = new ArrayList<ApplyTable>();
                for (Requisition requisition :requisitionList) {
                    applyTableList.add(new ApplyTable(
                            requisition.getId(),
                            requisition.getDateline(),
                            requisition.getDate(),
                            requisition.getState(),
                            requisition.getState()
                    ));
                }

                applyTableResult.setData(applyTableList);

            }else if( (!id.isEmpty())&&(!state.isEmpty()) ){//双向查询
                List<Requisition> requisitionList =  requisitionimpl.findByIdAndState(Integer.parseInt(id),Integer.parseInt(state));
                applyTableResult.setCode(0);
                applyTableResult.setMsg("请求成功");
                applyTableResult.setCount(requisitionList.size());

                List<ApplyTable> applyTableList = new ArrayList<ApplyTable>();
                for (Requisition requisition :requisitionList) {
                    applyTableList.add(new ApplyTable(
                            requisition.getId(),
                            requisition.getDateline(),
                            requisition.getDate(),
                            requisition.getState(),
                            requisition.getState()
                    ));
                }

                applyTableResult.setData(applyTableList);
            }


            jsonObject = (JSONObject) JSONObject.toJSON(applyTableResult);
        }catch (Exception e){
            jsonObject = (JSONObject) JSONObject.toJSON(new ApplyTableResult(500,"请求响应失败",0,null));
        }
        return jsonObject;

    }


    //指定日期查找
    @RequestMapping(value = "applied/searchbytime",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject searchByTime(@RequestParam(value = "begin",required = false,defaultValue = "") String beginDate,
                                   @RequestParam(value = "end",required = false,defaultValue = "") String endDate,
                                   @RequestParam(value = "state",required = false,defaultValue = "") String state){
        System.out.println("进入---searchByTime");
        JSONObject jsonObject = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ApplyTableResult applyTableResult = new ApplyTableResult();
            List<Requisition> requisitionList = null;

            if( Integer.parseInt(state)==1 ){//查询截止日期范围
                requisitionList = requisitionimpl.findByDatelineBetween(sdf.parse(beginDate),sdf.parse(endDate));
           }else {
               requisitionList = requisitionimpl.findByDatelineBetween(sdf.parse(beginDate),sdf.parse(endDate));
           }

            applyTableResult.setCode(0);
            applyTableResult.setMsg("查询成功");
            applyTableResult.setCount(requisitionList.size());

            List<ApplyTable> applyTableList = new ArrayList<ApplyTable>();
            for (Requisition requisition :requisitionList) {
                applyTableList.add(new ApplyTable(
                        requisition.getId(),
                        requisition.getDateline(),
                        requisition.getDate(),
                        requisition.getState(),
                        requisition.getState()
                ));
            }

            applyTableResult.setData(applyTableList);
            jsonObject = (JSONObject) JSONObject.toJSON(applyTableResult);
        }catch (Exception e){
            jsonObject = (JSONObject) JSONObject.toJSON(new ApplyTableResult(500,"请求响应失败",0,null));
        }
        return jsonObject;
    }


    //响应个人信息
    @RequestMapping(value = "apply/information",method = RequestMethod.POST)
    @ResponseBody//username=zhangsan
    public JSONObject information(String username){
        System.out.println("进入-----information");

        JSONObject jsonObject = null;
        try{
            Employee employee = employeeimpl.findByUsername(username);
            EmployeeInformation employeeInformation = employeeInformationImpl.findByEmployee(employee);

            //获取员工的职位
            Set<Position> positionSet = employee.getPosition();
            String name = null;
            for (Position position:positionSet) {
                name = position.getName();
            }
            //封住响应函数
            InformationResult informationResult = new InformationResult(
                    200,
                    "个人信息请求成功",
                    employeeInformation.getName(),
                    name,
                    employeeInformation.getSex(),
                    employeeInformation.getPhone(),
                    employeeInformation.getEmail(),
                    employeeInformation.getAddress());

            //jsonObject = (JSONObject) JSONObject.toJSON(applyTableResult);
            jsonObject = (JSONObject)JSONObject.toJSON(informationResult);
        }catch (Exception e){
            InformationResult informationResult = new InformationResult(400, "个人信息请求失败");
            jsonObject = (JSONObject) JSONObject.toJSON(informationResult);
        }

        return jsonObject;
    }

    //修改个人信息
    @RequestMapping(value = "apply/changeinformation")
    @ResponseBody
    public JSONObject changeInformation(@RequestBody Map param){
        System.out.println("进入------changeInformation");
        Result result = new Result();
        JSONObject jsonObject = new JSONObject();

        try{
            Employee employee = employeeimpl.findByUsername(param.get("username").toString());
            EmployeeInformation employeeInformation = employeeInformationImpl.findByEmployee(employee);
            employeeInformation.setName(param.get("name").toString());
            employeeInformation.setSex(Integer.parseInt(param.get("sex").toString()));
            employeeInformation.setPhone(param.get("phone").toString());
            employeeInformation.setEmail(param.get("email").toString());
            employeeInformation.setAddress(param.get("address").toString());

            employeeInformationImpl.save(employeeInformation);

            result.setCode(0);
            result.setMsg("用户信息修改成功");
            jsonObject = (JSONObject) JSONObject.toJSON(result);
        }catch (Exception e){
            result.setCode(200);
            result.setMsg("处理失败");
            jsonObject = (JSONObject) JSONObject.toJSON(result);
            System.out.println("error=====changeInformation");
        }



        return jsonObject;
    }


    //修改个人信息
    @RequestMapping(value = "apply/changepassword",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject changePassword(@RequestBody Map param){

        System.out.println("进入------changePassword");
        Result result = new Result();
        JSONObject jsonObject = new JSONObject();

        try{
            Employee employee = employeeimpl.findByUsername(param.get("username").toString());
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean f = encoder.matches(param.get("old_password").toString(),employee.getPassword());
            if( f==false ){//密码不正确
                result.setCode(200);
                result.setMsg("密码不正确");
            }else {//密码正确
                employee.setPassword(encoder.encode(param.get("again_password").toString()));
                employeeimpl.save(employee);
                result.setCode(0);
                result.setMsg("密码修改成功");
            }
            jsonObject = (JSONObject) JSONObject.toJSON(result);
        }catch (Exception e){
            result.setCode(200);
            result.setMsg("处理失败");
            jsonObject = (JSONObject) JSONObject.toJSON(result);
            System.out.println("error=====changePassword");
        }

        return jsonObject;
    }

    /*
    {
    "code": 0,
    "msg": "dsdsd",
    "count": 16,
    "data": [
        { "id":"008", "username":"刘晓庆", "sex":"女" },
        { "id":"009", "username":"刘晓庆", "sex":"女" },
        { "id":"010", "username":"刘晓庆", "sex":"女" },
        { "id":"011", "username":"刘晓庆", "sex":"女" },
        { "id":"012", "username":"刘晓庆", "sex":"女" },
        { "id":"013", "username":"刘晓庆", "sex":"女" },
        { "id":"014", "username":"刘晓庆", "sex":"女" },
        { "id":"015", "username":"刘晓庆", "sex":"女" },
        { "id":"016", "username":"刘晓庆", "sex":"女" }
    ]
}


    {
    "msg":"申请单编号查询成功",
    "code":200,
    "data":[
        {"date":"2021-05-27T16:47:59.473+00:00","dateline":"2021-05-28T16:00:00.000+00:00","id":8,"state":1,"state1":1}
        ],
    "count":1}

     */

}







//    //获取辅料
//    @RequestMapping(value = "applying/getIngredient",method = RequestMethod.POST)
//    @ResponseBody
//    public JSONObject getIngredient() {
//        System.out.println("进入---getIngredient");
//        MaterialResult materialResult = null;
//        try {
//            //从数据库中获取辅料
//            List<Material> ingredients = materialimpl.findAllByState(0);//辅料
//
//            //从辅料中获取需要的信息
//            List<com.zhang.vo.Material> materials = new ArrayList<com.zhang.vo.Material>();
//            for (Material ingredient:ingredients) {
//                com.zhang.vo.Material material = new com.zhang.vo.Material(
//                        ingredient.getId(),
//                        ingredient.getName(),
//                        ingredient.getUnit());
//                materials.add(material);
//            }
//            //主料信息
//            materialResult = new MaterialResult(200, "辅料信息请求成功",materials );
//
//
//        }catch (Exception e){
//            materialResult = new MaterialResult(404, "辅料信息请求失败", null);
//        }
//        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(materialResult);
//        System.out.println(jsonObject.toString());
//        return jsonObject;
//    }

//    //获取主料
//    @RequestMapping(value = "applying/getMainIngredient",method = RequestMethod.POST)
//    @ResponseBody
//    public JSONObject getMainIngredient() {
//        System.out.println("进入---getMainIngredient");
//        MaterialResult materialResult = null;
//        try {
//            //从数据库中获取主料
//            List<Material> mainIngredients = materialimpl.findAllByState(1);//主料
//
//            //从主料中获取需要的信息
//            List<com.zhang.vo.Material> materials = new ArrayList<com.zhang.vo.Material>();
//            for (Material mainIngredient:mainIngredients) {
//                JSONObject jsonObject = new JSONObject();
//                com.zhang.vo.Material material = new com.zhang.vo.Material(
//                        mainIngredient.getId(),
//                        mainIngredient.getName(),
//                        mainIngredient.getUnit());
//                materials.add(material);
//            }
//            //主料信息
//            materialResult = new MaterialResult(200, "主料信息请求成功",materials );
//
//
//        }catch (Exception e){
//            materialResult = new MaterialResult(404, "主料信息请求失败", null);
//        }
//        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(materialResult);
//        System.out.println(jsonObject.toString());
//        return jsonObject;
//    }




