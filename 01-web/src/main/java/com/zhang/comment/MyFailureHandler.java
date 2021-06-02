package com.zhang.comment;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhang.vo.LoginResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 错误信息处理类
 */
@Component
public class MyFailureHandler implements AuthenticationFailureHandler {
    private LoginResult loginResult;

    public LoginResult getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException, ServletException {
        if(loginResult == null){
            loginResult.setCode(404);
            loginResult.setMsg("请求失败");
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
