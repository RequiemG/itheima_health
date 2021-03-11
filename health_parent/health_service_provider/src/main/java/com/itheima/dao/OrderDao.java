package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    public List<Order> findByCondition(Order order);
    public void add(Order order);
    public Map findById4Detail(int id);
    public Integer findOrderCountByDate(String date);
    public Integer findOrderCountAfterDate(String date);

    public Integer findVisitsCountByDate(String date);
    public Integer findVisitsCountAfterDate(String date);

    public List<Map> findHotSetmeal();
}
