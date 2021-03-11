package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;


    public User findByUsername(String username) {
        // 查询出来的是用户的基本信息，不包含用户角色
        User user = userDao.findByUsername(username);
        if (user==null){
            return null;
        }
        Integer userId = user.getId();
        // 根据用户id查询角色
        Set<Role> roles = roleDao.findByUserId(userId);
        // 根据角色查询权限
        for (Role role : roles) {
            Integer roleId = role.getId();
            // 根据角色id查询权限
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);
        }

        // 用户关联角色
        user.setRoles(roles);
        return user;
    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public void register(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.register(user);
    }
}
