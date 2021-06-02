package com.zhang;

import com.zhang.dao.*;
import com.zhang.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringBootApplication
public class Application {

    @Autowired
    private PositionImpl positionimpl;
    @Autowired
    private EmployeeImpl employeeimpl;
    @Autowired
    private EmployeeInformationImpl employeeInformationimpl;
    @Autowired
    private PermissionImpl permissionimpl;
    @Autowired
    private MaterialImpl materialimpl;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 添加原料
     */
//    @PostConstruct
    public void jdbcAddMaterial(){
        Material material = new Material();
        material.setId(1);
        material.setName("主料1");
        material.setUnit("Kg");
        material.setState(1);
        material.setAllowance(100000.0);
        materialimpl.save(material);

        material.setId(2);
        material.setName("主料2");
        materialimpl.save(material);

        material.setId(3);
        material.setName("主料3");
        materialimpl.save(material);



        material.setId(4);
        material.setName("辅料1");
        material.setState(0);
        materialimpl.save(material);


        material.setId(5);
        material.setName("辅料2");
        materialimpl.save(material);


        material.setId(6);
        material.setName("辅料3");
        materialimpl.save(material);

    }


    /**
     * 添加管理员和个人信息
     */
//    @PostConstruct
    public void jdbcAddEmployee1() {
        /**
         * 添加个人信息
         */
        EmployeeInformation adminInformation = new EmployeeInformation();
        adminInformation.setName("管理员");
        EmployeeInformation applyInformation = new EmployeeInformation();
        applyInformation.setName("原料申请员");

        /**
         * 添加用户
         */
        Employee admin = new Employee();
        //用户名
        admin.setUsername("admin");
        //密码
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        admin.setPassword(encoder.encode("admin"));
        //四个true
        admin.setIscredentials(true);
        admin.setIsenable(true);
        admin.setIslock(true);
        admin.setIsexpired(true);

        //两个时间
        admin.setCreatetime(new Date());
        admin.setLogintime(new Date());


        Employee apply = new Employee();
        //用户名
        apply.setUsername("zhangsan");
        //密码
        apply.setPassword(encoder.encode("123456"));
        //四个true
        apply.setIscredentials(true);
        apply.setIsenable(true);
        apply.setIslock(true);
        apply.setIsexpired(true);

        //两个时间
        apply.setCreatetime(new Date());
        apply.setLogintime(new Date());


        /**
         * 保存用户个人详细信息
         */
        admin.setEmployeeinformation(adminInformation);
        adminInformation.setEmployee(admin);
        employeeimpl.save(admin);

        apply.setEmployeeinformation(applyInformation);
        applyInformation.setEmployee(apply);
        employeeimpl.save(apply);



        /*********     添加生产部门和权限    ***************/

        Permission roleAdmin = new Permission();
        roleAdmin.setPermission("ROLE_ADMIN");
        permissionimpl.save(roleAdmin);


        /**
         * 添加部门信息
         */
        Position adminPosition = new Position();
        adminPosition.setName("管理部门");
        adminPosition.setSuggest("管理部门工作");


        Set<Employee> adminSet = new HashSet<Employee>();
        adminSet.add(admin);
        adminPosition.setEmployee(adminSet);


        Set<Permission> permissionAdminSet = new HashSet<Permission>();
        permissionAdminSet.add(roleAdmin);
        adminPosition.setPermission(permissionAdminSet);

        positionimpl.save(adminPosition);



        Permission roleApply = new Permission();
        roleApply.setPermission("ROLE_APPLY");
        permissionimpl.save(roleApply);


        /**
         * 添加部门信息
         */
        Position applyPosition = new Position();
        applyPosition.setName("生产部门");
        applyPosition.setSuggest("进行生产，原料的申请");


        Set<Employee> applySet = new HashSet<Employee>();
        applySet.add(apply);
        applyPosition.setEmployee(applySet);


        Set<Permission> permissionApplySet = new HashSet<Permission>();
        permissionApplySet.add(roleAdmin);
        applyPosition.setPermission(permissionApplySet);

        positionimpl.save(applyPosition);



    }







}
