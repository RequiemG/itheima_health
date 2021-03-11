package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.util.SMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        // 从Redis中获取保存的验证码
        // 将用户输入的验证码和保存的进行比对
        // 比对成功调用服务
        // 不成功返回结果
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        map.put("orderType", Order.ORDERTYPE_WEIXIN);

        if (codeInRedis!=null && codeInRedis.equals(validateCode)){
            // 调用体检预约服务
            Result result = null;
            try {
                result = orderService.order(map);
            }catch (Exception e){
                e.printStackTrace();
                return result;  //预约失败
            }

            if (result.isFlag()){
                // 预约成功发送短信
                try {
                    SMSUtil.sendShortMessage(SMSUtil.ORDER_NOTICE,telephone,map.get("orderDate").toString());
                } catch (ClientException e) {
                    e.printStackTrace();
                    return result;// 预约失败
                }
            }

            return new Result(true, MessageConstant.VALIDATECODE_ERROR);
        }else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }


    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            return new Result(true, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
