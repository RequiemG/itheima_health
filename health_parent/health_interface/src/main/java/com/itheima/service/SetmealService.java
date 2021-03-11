package com.itheima.service;


import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    public void add(Setmeal setmeal, Integer[] checkGroupIds);
    public PageResult findPage(QueryPageBean queryPageBean);
    public Setmeal findById(int id);
    public List<Integer> findCheckGroupIdsBySetMeal(int id);
    public void edit(Setmeal setmeal, Integer[] checkGroupIds);
    public void deleteById(int id);

    public List<Setmeal> findAll();
    public Setmeal findById4Mobile(int id);
    public List<Map<String,Object>> findSetmealCount();
}
