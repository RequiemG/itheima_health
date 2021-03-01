package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    public void add(Setmeal setmeal);
    public int getMealId(Setmeal setmeal);
    public void setMealAndCheckGroup(Map<String, Integer> map);
    public Page<Setmeal> findPage(String queryString);
    public Setmeal findById(int id);
    public List<Integer> findCheckGroupIdsBySetMeal(int id);
    public void edit(Setmeal setmeal);
    public void deleteAssociation(int id);
    public void deleteById(int id);
}
