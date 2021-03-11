package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

public interface MemberService {
    // 根据手机号查询会员
    public Member findByTelephone(String phone);
    public void add(Member member);
    public List<Integer> findMemberCountByMonth(List<String> months);
}
