package com.itheima.dao;

import com.itheima.pojo.Member;

import java.util.List;

public interface MemberDao {
    public Member findByTelephone(String telephone);
    public void add(Member member);
    // 图表统计
    public Integer findMemberCountBeforeDate(String date);
    public Integer findMemberCountByDate(String date);
    public Integer findMemberTotalCount();
    // 运营数据统计
    public Integer findMemberCountAfterDate(String date);
}
