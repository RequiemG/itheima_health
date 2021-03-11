package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.util.SMSUtil;
import com.itheima.util.ValidateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;
    // 用户发送验证码，服务端缓存到redis，便于用户提交时查询

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        // 发送验证码
        Integer validateCode = ValidateCodeUtil.generateValidateCode(4);
        try {
            SMSUtil.sendShortMessage(SMSUtil.VALIDATE_CODE,telephone,validateCode.toString());
            // setex能设置存活时间以秒为单位    (键，时间，值) 时间到自动删除
            // 使用手机号+验证类型作为key
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());

            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }



    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        // 发送验证码
        Integer validateCode = ValidateCodeUtil.generateValidateCode(4);
        try {
            SMSUtil.sendShortMessage(SMSUtil.VALIDATE_CODE,telephone,validateCode.toString());
            // setex能设置存活时间以秒为单位    (键，时间，值) 时间到自动删除
            // 使用手机号+验证类型作为key
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode.toString());

            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
