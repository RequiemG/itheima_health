package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService{

    @Autowired
    private OrderSettingDao orderSettingDao;

    // 预约设置导入的excel文件
    @Override
    public void add(List<OrderSetting> list) {
        if (list!=null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                Date orderDate = orderSetting.getOrderDate();
                int dateNum = orderSettingDao.findCountByOrderDate(orderDate);
                if(dateNum>0){
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        // data=> 2021-03
        String begin = date + "-" + "01";
        String end = date + "-" + "31";
        HashMap<String, String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        List<OrderSetting> mapList = orderSettingDao.getOrderSettingByMonth(map);

        List<Map> data = new ArrayList<>();

        for (OrderSetting orderSetting : mapList) {
            Map<String, Object> orderSettingMap = new HashMap<>();
            int date1 = orderSetting.getOrderDate().getDate();
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
            orderSettingMap.put("date",date1);
            orderSettingMap.put("number",number);
            orderSettingMap.put("reservations",reservations);
            data.add(orderSettingMap);
        }
        return data;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        int countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (countByOrderDate>0){
            orderSettingDao.editNumberByDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }
}
