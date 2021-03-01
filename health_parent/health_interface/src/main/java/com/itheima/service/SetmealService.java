package com.itheima.service;


import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    public void add(Setmeal setmeal, Integer[] checkGroupIds);
    public PageResult findPage(QueryPageBean queryPageBean);
    public Setmeal findById(int id);
    public List<Integer> findCheckGroupIdsBySetMeal(int id);
    public void edit(Setmeal setmeal, Integer[] checkGroupIds);
    public void deleteById(int id);
}
