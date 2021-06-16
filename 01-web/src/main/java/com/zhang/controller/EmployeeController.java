package com.zhang.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhang.dao.*;
import com.zhang.entity.Employee;
import com.zhang.entity.EmployeeInformation;
import com.zhang.entity.Position;
import com.zhang.vo.Result;
import com.zhang.vo.employeeTableResult.EmployeeTable;
import com.zhang.vo.employeeTableResult.EmployeeTableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class EmployeeController {
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
     * 模块说明：
     *          1、查看职员的各种信息----职位-----个人信息------他们负责的订单
     *          2、添加删除职员
     *          3、角色管理----增删改查
     *
     * 本函数功能说明：
     *          返回数据表格
     *
     * 页面响应数据:
     *      employeeTableDir            员工数据表格的请求地址        employee/table
     *      employeeSearchDir           根据条件搜索指定的申请单      employee/search
     *      employeeAddDir              响应员工添加页面             employee/add
     *      employeeAddStepFirDir       保存主要信息                employee/stepFir
     *      employeeAndStepSecDir       保存个人信息                employee/stepSec
     *      employeeDeleteDir           删除多个                    employee/delete
     *      employeeDeleteOneDir        删除单个                    employee/deleteOne
     *      employeeDetailDir           详情按钮                    employee/detail
     *      employeeChangeBasicDir      修改员工基本信息              employee/changeBasic
     *      employeeChangeMainDir       修改员工主要信息              employee/changeMain
     *
     * @return 员工管理页面
     */
    @RequestMapping(value = "employee/manage")
    private ModelAndView employeeManage(){
        System.out.println("进入-----employeeManage");
        ModelAndView mv = new ModelAndView();

        List<Employee> employeeList = employeeimpl.findAll();
        mv.addObject("employeeList",employeeList);
        List<Position> positionList = positionimpl.findAll();
        mv.addObject("positionList",positionList);


        mv.addObject("employeeTableDir","employee/table");

        mv.addObject("employeeSearchDir","employee/search");

        mv.addObject("employeeAddDir","employee/add");

        mv.addObject("employeeAddStepFirDir","employee/stepSec");
        mv.addObject("employeeAndStepSecDir","employee/stepSec");


        mv.addObject("employeeDeleteDir","employee/delete");
        mv.addObject("employeeDeleteOneDir","employee/deleteOne");

        mv.addObject("employeeDetailDir","employee/detail");

        mv.setViewName("static/page/admin/employee");

        return mv;
    }


    //employeeTableDir       员工数据表格的请求地址         employee/table
    @RequestMapping(value = "employee/table",method = RequestMethod.POST)
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


    //employeeSearchDir          搜索框                     employee/search
    @RequestMapping(value = "employee/search")
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




    //employeeAddDir         响应员工添加页面             employee/add
    @RequestMapping(value = "employee/add")//添加用户响应页面
    private ModelAndView employeeAdd(){
        System.out.println("进入------employeeAdd");
        ModelAndView mv = new ModelAndView();

        mv.setViewName("static/page/admin/employeeing");
        List<Position> positionList = positionimpl.findAll();
        mv.addObject("positionList",positionList);

        mv.addObject("employeeAddStepFirDir","employee/stepFir");

        mv.addObject("employeeAndStepSecDir","employee/stepSec");

        //标记添加
        mv.addObject("state",0);

        //响应用户名
        mv.addObject("username","admin");
        return mv;
    }

    //employeeAddStepFirDir     保存主要信息                 employee/stepFir
    @RequestMapping(value = "employee/stepFir")
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
    //employeeAndStepSecDir   保存个人信息                 employee/stepSec
    @RequestMapping(value = "employee/stepSec")
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






    //employeeDeleteDir      删除多个                    employee/delete         employee/delete
    @RequestMapping(value = "employee/delete",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject employeeDelete(@RequestBody List<EmployeeTable> employeeTableList){
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

    //employeeDeleteOneDir        删除单个                    employee/deleteOne
    @RequestMapping(value = "employee/deleteOne",method = RequestMethod.POST)
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


    //employeeDetailDir      详情按钮                    employee/detail
    @RequestMapping(value = "employee/detail")
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

        mv.addObject("employeeChangeBasicDir","employee/changeBasic");
        mv.addObject("employeeChangeMainDir","employee/changeMain");

        mv.setViewName("static/page/admin/employee-detail");
        return mv;
    }

    //employeeChangeBasicDir 修改员工基本信息               employee/changeBasic
    @RequestMapping(value = "employee/changeBasic",method = RequestMethod.POST)
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


    //employeeChangeMainDir  修改员工主要信息              employee/changeMain
    @RequestMapping(value = "employee/changeMain",method = RequestMethod.POST)
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


}
