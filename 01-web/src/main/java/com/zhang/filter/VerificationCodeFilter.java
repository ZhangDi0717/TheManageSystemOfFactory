package com.zhang.filter;

import com.zhang.comment.MyFailureHandler;
import com.zhang.comment.VerificationException;
import com.zhang.vo.LoginResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 自定义验证码拦截类
 */
public class VerificationCodeFilter extends OncePerRequestFilter {

   @Autowired
   private MyFailureHandler failureHandler = new MyFailureHandler();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        //只有是login操作，才需要这个过滤器
        String uri = httpServletRequest.getRequestURI();
        if( !"/login".equals(uri)){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }else {
            //登陆操作
            try {
                //验证 验证码是否正确
                verificationCode(httpServletRequest);
                //如果验证通过过滤器正常执行
                filterChain.doFilter(httpServletRequest,httpServletResponse);

            } catch (VerificationException e) {

                LoginResult loginResult = new LoginResult();
                loginResult.setCode(100);
                loginResult.setMsg("验证码错误");
                failureHandler.setLoginResult(loginResult);
                failureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
            }
        }



    }

    /**
     * 判断验证码是否正确
     * @param httpServletRequest
     * @throws VerificationException
     */
    private void verificationCode(HttpServletRequest httpServletRequest) throws VerificationException {

        //获取用户输入验证码
        String requestCode = httpServletRequest.getParameter("code");

        //获取会话
        HttpSession session = httpServletRequest.getSession();
        //获取系统验证码
        String sessionCode = "";
        Object attr = session.getAttribute("code");
        if( attr != null ){
            sessionCode = (String)attr;
        }
        //处理逻辑
        if( !StringUtils.isEmpty(sessionCode)){
            /**
             * 如果进入这段代码，说明用户已经使用这个验证码了，
             * 那么这个验证码应该失效了
             */
            session.removeAttribute("code");
        }

        System.out.println("requestCode: "+requestCode+"  ---  sessionCode"+sessionCode);
        //判断验证是否正确
        if( StringUtils.isEmpty(requestCode) ||
            StringUtils.isEmpty(sessionCode)||
            !requestCode.equalsIgnoreCase(sessionCode) ){
            //验证失败 抛出异常
            throw new VerificationException();
        }
    }




}
