package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.util.DateUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    public Result order(Map map) throws Exception {
        /*
        1、检查用户所选择的预约日期是否已经提前进行了预约设置,如果没有设置则无法进行 预约
        2、检查用户所选择的预约日期是否已经约满,如果已经约满则无法预约
        3、检查当前用户是否为会员,如果是会员则直接完成预约,如果不是会员则自动完成注 册并进行预约
        4、检查用户是否重复预约(同一个用户在同一天预约了同一个套餐),如果是重复预约 则无法完成再次预约
        5、预约成功,更新当日的已预约人数
        */

        //1
        String orderDate = map.get("orderDate").toString();
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(orderDate);
//        DateUtils.parseString2Date(orderDate)

        OrderSetting orderSetting = orderSettingDao.findOrderByDate(date);
        if (orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations>=number){
            //预约满了
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //3 检查当前用户是否为会员,如果是会员判断是否重复预约，如果不是会员则自动完成注册并判断是否重复预约进行预约
        String telephone = map.get("telephone").toString();
        Member member = memberDao.findByTelephone(telephone);
        if (member==null){
            Member newMember = new Member();
            newMember.setName(map.get("name").toString());
            newMember.setPhoneNumber(telephone);
            newMember.setIdCard(map.get("idCard").toString());
            newMember.setSex(map.get("sex").toString());
            newMember.setRegTime(new Date());
            memberDao.add(newMember);
        }else {
            // 检查用户是否重复预约(同一个用户在同一天预约了同一个套餐),如果是重复预约 则无法完成再次预约
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt(map.get("setmealId").toString());
            Order order = new Order(memberId, date, null, null, setmealId);
            List byCondition = orderDao.findByCondition(order);
            if (byCondition.size()>0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }else {
                // 添加到订单表t_order
                String orderType = map.get("orderType").toString();
                Order addOrder = new Order(memberId, date, orderType, Order.ORDERSTATUS_NO, setmealId);
                orderDao.add(addOrder);
                // 设置已预约人数+1
                int i = orderSetting.getReservations()+1;
                orderSetting.setReservations(i);
                orderSettingDao.editReservationsByOrderDate(orderSetting);
                return new Result(true, MessageConstant.ORDER_SUCCESS,addOrder.getId());
            }

        }




        return null;
    }

    @Override
    public Map findById(int id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map!=null){
            Date date = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(date,"yyyy-MM-dd"));
        }
        // 用map封装，因为前端需要的内容在order对象中找不到
        return map;
    }
}
