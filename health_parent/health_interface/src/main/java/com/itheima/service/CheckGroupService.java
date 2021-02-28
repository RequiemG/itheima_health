package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    public void add(CheckGroup checkGroup, Integer[] checkItemIds);
    public PageResult pageQuery(QueryPageBean queryPageBean);
    public void deleteById(int id);
    public CheckGroup findById(int id);
    public List<Integer> findCheckItemIdsByCheckGroupId(int id);
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds);
}
