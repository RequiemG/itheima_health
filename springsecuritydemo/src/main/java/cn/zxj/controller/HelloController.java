package cn.zxj.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('add')")//表示用户必须拥有add权限才能调用当前方法
    public String add(){
        System.out.println("addddddd");
        return "add success";
    }

    @RequestMapping("/delete1")
    @PreAuthorize("hasAuthority('delete')")
    public String delete1(){
        System.out.println("deleteeeee");
        return "delete success";
    }


    @RequestMapping("/delete2")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete2(){
        System.out.println("deleteeeee");
        return "delete success";
    }

    @RequestMapping("/test1")
    public String test1(){
        System.out.println("tttest111");
        return "test111";
    }


    @RequestMapping("/test2")
    @PreAuthorize("isAuthenticated()")
    public String test2(){
        System.out.println("tttest222");
        return "test222";
    }

}

