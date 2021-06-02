package com.zhang.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhang.dao.MaterialImpl;
import com.zhang.dao.RequisitionImpl;
import com.zhang.entity.Material;
import com.zhang.entity.Requisition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping()
public class TestController {

//    @RequestMapping("/test")
//    private ModelAndView test01(){
//        ModelAndView mv = new ModelAndView();
//        mv.setViewName("page");
//        return mv;
//    }

    @Autowired
    private MaterialImpl materialimpl;

    @Autowired
    private RequisitionImpl requisitionimpl;

    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test(@RequestBody JSONObject json) throws Exception {
//        System.out.println("param="+param);
    //    param = "["+param+"]";
//        JSONObject json = new JSONObject();
//        List<Map> parseArray = json.parseArray(param, Map.class);

        String yuanliao0 = json.get("yuanliao0").toString();
        System.out.println(yuanliao0);
//        System.out.println("parseArray="+parseArray);
        return null;
    }


    @RequestMapping(value = "test2",method = RequestMethod.POST)
    @ResponseBody
    public String test2(@RequestBody Map o) {
        System.out.println("进入application/save");
//        String name = (String) o.get("name");
//        String sex = (String) o.get("sex");
        String yuanliao0 = (String) o.get("yuanliao0");
        System.out.println(yuanliao0);
        return "进入application/save";
    }

    @RequestMapping(value = "application/save",method = RequestMethod.POST)
    @ResponseBody
    public String get(@RequestBody Map o) {
        System.out.println("进入application/save");
        String name = (String) o.get("name");
        String sex = (String) o.get("sex");
        System.out.println(name+"======="+sex);
        return "进入application/save";
    }

    @RequestMapping("/test3")
    public String test3(){
        System.out.println("进入----yest3");
        return "";
    }

    //申请单编辑测试
    @RequestMapping("/test4")
    public ModelAndView test4(String id){
        System.out.println("进入------test4");
        System.out.println("id : "+id);
        ModelAndView mv = new ModelAndView();

        //映射原料
        List<Material> mainIngredients = materialimpl.findAllByState(1);

        mv.addObject("mainIngredients",mainIngredients);

        Requisition requisition = requisitionimpl.getOne(Integer.parseInt(id));
        mv.addObject("requisition",requisition);

        mv.addObject("id",8);

        mv.setViewName("static/page/table/add");
        return mv;
    }

    //页面初始化
    @RequestMapping(value = "test5",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject test5(){
        String testString = "{\n" +
                "  \"homeInfo\": {\n" +
                "    \"title\": \"主页\",\n" +
                "    \"href\": \"page/welcome-1.html?t=1\"\n" +
                "  },\n" +
                "  \"logoInfo\": {\n" +
                "    \"title\": \"后台管理系统\",\n" +
                "    \"image\": \"images/logo.png\",\n" +
                "    \"href\": \"\"\n" +
                "  },\n" +
                "  \"menuInfo\": [\n" +
                "    {\n" +
                "      \"title\": \"常规管理\",\n" +
                "      \"icon\": \"fa fa-address-book\",\n" +
                "      \"href\": \"\",\n" +
                "      \"target\": \"_self\",\n" +
                "      \"child\": [\n" +
                "        {\n" +
                "          \"title\": \"申请表\",\n" +
                "          \"href\": \"\",\n" +
                "          \"icon\": \"fa fa-home\",\n" +
                "          \"target\": \"_self\",\n" +
                "          \"child\": [\n" +
                "            {\n" +
                "              \"title\": \"申请\",\n" +
                "              \"href\": \"page/apply/applying.html\",\n" +
                "              \"icon\": \"fa fa-tachometer\",\n" +
                "              \"target\": \"_self\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";



        return JSON.parseObject(testString);
    }

    //申请单详情测试
    @RequestMapping("/test6")
    public ModelAndView test6(){
        System.out.println("进入------test6");
        ModelAndView mv = new ModelAndView();
        List<Material> mainIngredients = materialimpl.findAllByState(1);
        mv.addObject("mainIngredients",mainIngredients);
        mv.setViewName("static/page/table/add");
        return mv;
    }

    @RequestMapping(value = "test7",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject test7(@RequestBody Map param ){
        System.out.println("进入-----test7");
        String json = "{\n" +
                "  \"code\": 1,\n" +
                "  \"msg\": \"服务端清理缓存成功\"\n" +
                "}";
        return JSON.parseObject(json);
    }



}
