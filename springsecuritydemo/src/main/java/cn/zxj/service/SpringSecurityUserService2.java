package cn.zxj.service;

import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SpringSecurityUserService2 implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private void initUserData(){
        User user1 = new User();
        user1.setUsername("admin");
        user1.setPassword(bCryptPasswordEncoder.encode("admin"));   // 加密

        User user2 = new User();
        user2.setUsername("xiaoming");
        user2.setPassword(bCryptPasswordEncoder.encode("1234"));

        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }
    public  Map<String, User> map = new HashMap<>();

    @Override
    // 根据用户查询用户信息,框架调用的
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        initUserData();
        System.out.println(username);
        // 根据用户查询数据库获得用户对象
        User user = map.get(username);
        if (user==null){
            return null;
        }else {
            // 为当前用户授权，需要从数据库中查询到该角色的所有权限
            List<GrantedAuthority> list = new ArrayList<>();
            list.add(new SimpleGrantedAuthority("add")); //设置权限权限
            list.add(new SimpleGrantedAuthority("delete")); //设置权限权限

//            if (username.equals("xiaoming")){
//                list.add(new SimpleGrantedAuthority("add"));
//            }

            if (username.equals("admin")){
                list.add(new SimpleGrantedAuthority("ROLE_ADMIN")); //角色
            }
            for (GrantedAuthority grantedAuthority : list) {
                System.out.println("service---"+grantedAuthority);
            }
            org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),list);
            // 传入用户名，密码，list集合（权限）验证之后给(jǐ)予的权限
            return securityUser;
        }
        // 将用户对象返回给框架
        // 框架会进行密码比对
    }
}
