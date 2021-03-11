package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String phone) {
        return memberDao.findByTelephone(phone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        ArrayList<Integer> memberCount = new ArrayList<>();
        // 传入的参数格式为yyyy.MM每；但是数据库中的日期格式为yyyy.MM.dd，所以我们要处理一下数据格式
        for (String month : months) {
            String date = month+".31";
            // 查询sql
            Integer countBeforeDate = memberDao.findMemberCountBeforeDate(date);
            memberCount.add(countBeforeDate);
        }
        return memberCount;
    }
}
