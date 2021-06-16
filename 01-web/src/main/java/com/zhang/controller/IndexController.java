package com.zhang.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhang.dao.EmployeeImpl;
import com.zhang.dao.EmployeeInformationImpl;
import com.zhang.dao.PositionImpl;
import com.zhang.entity.Employee;
import com.zhang.entity.EmployeeInformation;
import com.zhang.entity.Position;
import com.zhang.vo.Result;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("templates/myLogin");
        return mv;
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
