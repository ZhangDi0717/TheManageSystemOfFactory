package com.zhang.vo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails{

    private String username;
    private String password;
    private Boolean isexpired;
    private Boolean islock;
    private Boolean iscredentials;
    private Boolean isenable;
    private List<GrantedAuthority> authorities;

    public MyUserDetails(String username,
                         String password,
                         Boolean isexpired,
                         Boolean islock,
                         Boolean iscredentials,
                         Boolean isenable,
                         List<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.isexpired = isexpired;
        this.islock = islock;
        this.iscredentials = iscredentials;
        this.isenable = isenable;
        this.authorities = authorities;
    }

    /**
     * 账号权限
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;

    }

    /**
     * 获取用户密码
     * @return
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 获取用户名
     * @return
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 是否过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return isexpired;
    }

    /**
     * 账号是否锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return islock;
    }

    /**
     * 凭证是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return iscredentials;
    }

    /**
     * 账号是否启用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return isenable;
    }
}
