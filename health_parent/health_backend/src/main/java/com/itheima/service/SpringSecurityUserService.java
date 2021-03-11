package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    // 使用dubbo远程调用拿到数据库中的用户信息
    @Reference
    private UserService userService;


    // 根据用户名查询数据库，获取用户信息
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null){
            return null;
        }
        // 动态为当前用户授权
        Set<Role> roles = user.getRoles();
        List<GrantedAuthority> list = new ArrayList<>();
        // 遍历角色集合，为用户添加角色
        for (Role role : roles) {
            // 遍历，添加到列表
            list.add(new SimpleGrantedAuthority(role.getKeyword()));

            // 遍历权限集合，为用户授权
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }


        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),list);
    }
}
