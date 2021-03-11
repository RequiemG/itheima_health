package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetmealService setmealService;


    @RequestMapping("/getAllSetMeal")
    public Result getAllSetMeal(){
        try {
            List<Setmeal> all = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,all);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_SUCCESS);
        }
    }


    // 根据套餐ID查询 套餐基本详情，对应的检查组信息，检查组对应的检查项信息
    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Setmeal setmeal = setmealService.findById4Mobile(id);
//            System.out.println("------------------------------------"+setmeal);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        }catch (Exception e){
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
