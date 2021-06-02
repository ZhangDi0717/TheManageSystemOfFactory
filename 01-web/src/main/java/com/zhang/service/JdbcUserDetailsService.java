package com.zhang.service;

import com.zhang.comment.MyFailureHandler;
import com.zhang.comment.MySuccessHandler;
import com.zhang.dao.EmployeeImpl;
import com.zhang.entity.Position;
import com.zhang.entity.Employee;
import com.zhang.entity.Permission;
import com.zhang.entity.Position;
import com.zhang.vo.LoginResult;
import com.zhang.vo.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.*;


/**
 * 登陆用户信息封装类
 */
@Service
public class JdbcUserDetailsService implements UserDetailsService {


    @Autowired
    private MySuccessHandler successHandler = new MySuccessHandler();

    @Autowired
    private MyFailureHandler failureHandler = new MyFailureHandler();
    @Autowired
    private EmployeeImpl employeeimpl;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.获取用户信息
        Employee employee = employeeimpl.findByUsername(username);
        if (employee !=null){
            //获取权限
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            GrantedAuthority authority = null;

            Set<Position> positionSet = employee.getPosition();

            Iterator<Position> positionIterator = positionSet.iterator();
            while(positionIterator.hasNext()) {
                Position position = positionIterator.next();
                Set<Permission> permissionSet = position.getPermission();
                Iterator<Permission> permissionIterator = permissionSet.iterator();
                while (permissionIterator.hasNext()){
                    Permission permission = permissionIterator.next();
                    authority = new SimpleGrantedAuthority(permission.getPermission());
                    authorities.add(authority);
                }
            }

            //更新用户登陆时间
            employee.setLogintime(new Date());
            //存入数据库
            employeeimpl.save(employee);


            //封装权限和应答路劲
            urlAndauthority(authorities,employee.getUsername());


            //封装MyUserDetails
            MyUserDetails myUserDetails = new MyUserDetails(employee.getUsername(),
                    employee.getPassword(),
                    employee.getIsexpired(),
                    employee.getIslock(),
                    employee.getIscredentials(),
                    employee.getIsenable(),
                    authorities);

            return myUserDetails;
        }else {
            //用户名把密码不正确
            LoginResult loginResult = new LoginResult();
            loginResult.setCode(203);
            loginResult.setMsg("用户名和密码不正确");
            failureHandler.setLoginResult(loginResult);
        }

        System.out.println("=======执行错误============");
        return null;
    }


    private void urlAndauthority(List<GrantedAuthority> authorities,String username){
        for (GrantedAuthority grantedAuthority:authorities) {
            if(grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                LoginResult loginResult = new LoginResult();
                loginResult.setCode(200);
                loginResult.setMsg("/adminpage?username="+username);
                successHandler.setLoginResult(loginResult);
            }else if( grantedAuthority.getAuthority().equals("ROLE_APPLY") ){
                LoginResult loginResult = new LoginResult();
                loginResult.setCode(200);
                loginResult.setMsg("/applypage?username="+username);
                successHandler.setLoginResult(loginResult);
            }
        }
    }

}
