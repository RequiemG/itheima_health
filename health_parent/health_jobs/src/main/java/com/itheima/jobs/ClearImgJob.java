package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.util.AliyunUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (set!=null){
            for (String s : set) {
                AliyunUtil.deleteFileFromAliYun(s);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,s);
                System.out.println("自定义任务删除垃圾图片=>"+s);
            }
        }
    }
}
