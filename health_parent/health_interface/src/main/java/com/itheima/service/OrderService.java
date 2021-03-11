package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.Order;

import java.text.ParseException;
import java.util.Map;

public interface OrderService {
    public Result order(Map map) throws Exception;
    public Map findById(int id) throws Exception;
}
