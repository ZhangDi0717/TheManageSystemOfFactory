package com.zhang.config;

import com.zhang.filter.VerificationCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;


/**
 * spring security 安全框架配置类
 */
@Configuration
@EnableWebSecurity
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("jdbcUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;
    /**
     * 为框架提供UserDetailsService对象
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("=================configure AuthenticationManagerBuilder =========");

//        super.configure(auth);
        //指定UserDetailsService对象、
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());//加密方法
    }




    /**
     * 登陆页面管理
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("=================configure HttpSecurity =========");
        http.authorizeRequests()
                .antMatchers("/mylogin",
                        "/login",
                        "/api/**",
                        "/css/**",
                        "/font/**",
                        "/images/**",
                        "/img/**",
                        "/js/**",
                        "/layui-v2.6.5/**",
                        "/lib/**",
                        "/captcha/**",
                        "/signout/success").permitAll()//设置访问的白名单
                .antMatchers("/page/admin/**").hasRole("ADMIN")
                .antMatchers("/page/apply/**").hasRole("APPLY")
//                .anyRequest().authenticated()//正式使用
                .anyRequest().permitAll()//测试
                .and()
                .formLogin()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .loginPage("/mylogin")//自定义登陆界面
                .loginProcessingUrl("/login")//登陆表单提交界面
                .and()
                .logout()
                .logoutUrl("/signout") // 退出登录的url
                .logoutSuccessUrl("/mylogin") // 退出登录成功跳转的url
                .and()
                .csrf().disable();//跨域
//                .cors().and();

        //在过滤器链条中添加自定义的过滤器
        http.addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}
