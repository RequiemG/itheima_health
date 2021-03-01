package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    public void add(CheckItem checkItem);
    public Page<CheckItem> findPage(String queryString);
    public CheckItem findById(int id);
    public void edit(CheckItem checkItem);
    public void deleteById(int id);
    public long isRelevance(int id);
    public List<CheckItem> findAll();
}
