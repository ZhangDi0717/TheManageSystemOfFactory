package com.zhang.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhang.vo.LoginResult;
import com.zhang.vo.MyUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 正确信息处理类
 */
@Component
public class MySuccessHandler implements AuthenticationSuccessHandler {

    private LoginResult loginResult;

    public LoginResult getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }

    /**
     *
     * @param httpServletRequest  请求对象
     * @param httpServletResponse  应答对象
     * @param authentication spring security框架验证用户信息的集合：getPrincipal封装这用户信息
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        MyUserDetails myUserDetails = (MyUserDetails)authentication.getPrincipal();

        if(loginResult == null){
            loginResult.setCode(500);
            loginResult.setMsg("服务器响应失败");
        }

        //使用框架验证用户信息成功时执行的方法
        httpServletResponse.setContentType("txt/json;charset=utf-8");

        ServletOutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper om = new ObjectMapper();//创建json封装对象
        om.writeValue(out,loginResult);
        out.flush();
        out.close();
    }
}
