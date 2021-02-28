package com.itheima.dao;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;


public interface CheckGroupDao {
    public void add(CheckGroup checkGroup);
    public int getGroupId(CheckGroup checkGroup);
    public void setCheckGroupAndCheckItem(Map map);
    public Page<CheckGroup> pageQuery(String queryString);
    public void deleteById(int id);
    public CheckGroup findById(int id);
    public List<Integer> findCheckItemIdsByCheckGroupId(int id);
    public void edit(CheckGroup checkGroup);
    public void deleteAssociation(int id);
}
