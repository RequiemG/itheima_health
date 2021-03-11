package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService{

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        // t_setmeal表，添加套餐基本信息
        setmealDao.add(setmeal);
        // 得到套餐的id值
        int mealId = getMealId(setmeal);
        // 关联边，设置关联
        this.setMealAndCheckGroup(mealId,checkGroupIds);
        // 保存图片名称
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
    }

    public void setMealAndCheckGroup(Integer mealId, Integer[] checkGroupIds){
        if (checkGroupIds!=null && checkGroupIds.length>0){
            for (Integer checkGroupId : checkGroupIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("setmeal_id", mealId);
                map.put("checkgroup_id", checkGroupId);
                setmealDao.setMealAndCheckGroup(map);
            }
        }
    }

    public int getMealId(Setmeal setmeal){
        return setmealDao.getMealId(setmeal);
    }


    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage,pageSize);

        Page<Setmeal> setmealList = setmealDao.findPage(queryString);
        long total = setmealList.getTotal();
        List<Setmeal> result = setmealList.getResult();
        return new PageResult(total, result);
    }

    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Integer> findCheckGroupIdsBySetMeal(int id) {
        return setmealDao.findCheckGroupIdsBySetMeal(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {
        // 先删除关联表中的关系
        Integer id = setmeal.getId();
        deleteAssociation(id);
        setMealAndCheckGroup(id,checkGroupIds);
        setmealDao.edit(setmeal);
    }

    public void deleteAssociation(int id){
        setmealDao.deleteAssociation(id);
    }

    @Override
    public void deleteById(int id) {
        deleteAssociation(id);
        setmealDao.deleteById(id);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findById4Mobile(int id) {
        return setmealDao.findById4Mobile(id);
    }

    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}

