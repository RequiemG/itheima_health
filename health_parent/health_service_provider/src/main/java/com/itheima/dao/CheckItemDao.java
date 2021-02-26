package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

public interface CheckItemDao {
    public void add(CheckItem checkItem);
    public Page<CheckItem> findPage(String queryString);
    public CheckItem findById(int id);
    public void edit(CheckItem checkItem);
    public void deleteById(int id);
    public long isRelevance(int id);
}