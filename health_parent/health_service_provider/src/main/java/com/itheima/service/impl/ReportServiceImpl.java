package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.service.ReportService;
import com.itheima.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {


    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;


    /*
    reportDate:null,
    todayNewMember :0,
    totalMember :0,
    thisWeekNewMember :0,
    thisMonthNewMember :0,

    todayOrderNumber :0,
    todayVisitsNumber :0,
    thisWeekOrderNumber :0,
    thisWeekVisitsNumber :0,
    thisMonthOrderNumber :0,
    thisMonthVisitsNumber :0,
    hotSetmeal :[]
    */

    @Override
    public Map<String,Object> getBusinessReport() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        // 日期
        String today = DateUtils.parseDate2String(new Date());
        map.put("reportDate",today);

        // 今日新增会员数
        Integer count = memberDao.findMemberCountByDate(today);
        map.put("todayNewMember",count);

        // 总会员数
        Integer memberTotalCount = memberDao.findMemberTotalCount();
        map.put("totalMember",memberTotalCount);

        // 本周新增会员数
        // 这一周的周一到周日
        String monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());// 本周一的日期
        Integer memberCountAfterDate = memberDao.findMemberCountAfterDate(monday);// 本周周一到当前的会员数
        map.put("thisWeekNewMember",memberCountAfterDate);

        // 本月新增会员
        String thisMonth4FirstDay = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        Integer countAfterDate = memberDao.findMemberCountAfterDate(thisMonth4FirstDay);
        map.put("thisMonthNewMember",countAfterDate);

        // 今日预约数
        Integer orderCountByDate = orderDao.findOrderCountByDate(today);
        map.put("todayOrderNumber",orderCountByDate);

        // 本周预约数
        Integer orderCountAfterDate = orderDao.findOrderCountAfterDate(monday);
        map.put("thisWeekOrderNumber",orderCountAfterDate);

        // 本月预约数
        Integer orderNum4ThisMonth = orderDao.findOrderCountAfterDate(thisMonth4FirstDay);
        map.put("thisMonthOrderNumber",orderNum4ThisMonth);

        // 今日到诊数
        Integer todayVisiNum = orderDao.findVisitsCountByDate(today);
        map.put("todayVisitsNumber",todayVisiNum);

        // 本周到诊数
        Integer weekVisionNum = orderDao.findVisitsCountAfterDate(monday);
        map.put("thisWeekVisitsNumber",weekVisionNum);

        // 本月到诊数
        Integer monthVisionNum = orderDao.findVisitsCountAfterDate(thisMonth4FirstDay);
        map.put("thisMonthVisitsNumber",monthVisionNum);

        // 热门套餐
        List<Map> setmealCount = orderDao.findHotSetmeal();

        map.put("hotSetmeal",setmealCount);
        return map;
    }


}
