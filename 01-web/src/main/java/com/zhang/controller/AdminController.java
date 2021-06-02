package com.zhang.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员服务
 */
@RestController
public class AdminController {


    @RequestMapping("/adminpage")
    @ResponseBody
    private String admin(){
        return "hello admin";
    }

}
