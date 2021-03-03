package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.util.AliyunUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/setmeal")
public class SetmealController {


    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetmealService setmealService;

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkGroupIds){
        try {
            setmealService.add(setmeal, checkGroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    // 文件上传
    // 要在spring中配置一下上传组件
    // 设置上传的参数名
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) throws IOException {
        try{
            // 获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            assert originalFilename != null;
            int i = originalFilename.lastIndexOf(".");
            // 获取文件后缀
            String substring = originalFilename.substring(i);
            // 使用UUID产生文件名
            String uuid = UUID.randomUUID().toString();
            // 将uuid名称和文件后缀拼接
            String fileName = uuid+substring;
            // 上传图片
            AliyunUtil.upload2AliYun(imgFile.getBytes(),fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            // 返回信息，因为数据库需要文件的地址，所以数据也要返回
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }


    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.findPage(queryPageBean);
    }


    @RequestMapping("/findById")
    public Result findById(int id){
        Setmeal setmeal = setmealService.findById(id);
        try {
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findCheckGroupIdsBySetMeal")
    public Result findCheckGroupIdsBySetMeal(int id){
        List<Integer> checkGroupIds = setmealService.findCheckGroupIdsBySetMeal(id);
        try {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupIds);
        }catch (Exception e){
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }


    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkGroupIds){
        try {
            setmealService.edit(setmeal,checkGroupIds);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }









    @RequestMapping("/delete")
    public Result delete(int id){
        try {
            setmealService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_MEMBER_SUCCESS);
        }
        catch (Exception e){
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }
}
