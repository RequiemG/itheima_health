package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    public void add(OrderSetting orderSetting);
    public int findCountByOrderDate(Date orderDate);
    public void editNumberByOrderDate(OrderSetting orderSetting);
    public List<OrderSetting> getOrderSettingByMonth(Map map);
    public void editNumberByDate(OrderSetting orderSetting);

    public OrderSetting findOrderByDate(Date orderDate);
    public void editReservationsByOrderDate(OrderSetting orderSetting);
}
