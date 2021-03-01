package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    // 注意：要添加，就要先设置检查项和检查组之间的关系
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        checkGroupDao.add(checkGroup);
        // 数据添加到数据库，并没有指定id，所以参数列表中的id属性为null，要先从数据库中获取id，不能直接从参数中获取
        int groupId = getGroupId(checkGroup);
        // 得到id后，进入关联表设置关联
        setCheckGroupAndCheckItem(groupId,checkItemIds);
    }

    public int getGroupId(CheckGroup checkGroup){
        return checkGroupDao.getGroupId(checkGroup);
    }

    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkItemIds){
        if (checkItemIds != null && checkItemIds.length>0){
            for (Integer checkItemId : checkItemIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id",checkGroupId);
                map.put("checkitem_id",checkItemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        };
    }

    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage,pageSize);

        Page<CheckGroup> checkGroups = checkGroupDao.pageQuery(queryString);
        long total = checkGroups.getTotal();
        List<CheckGroup> result = checkGroups.getResult();
        return new PageResult(total, result);
    }




    public CheckGroup findById(int id) {
        return checkGroupDao.findById(id);
    }

    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }


    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        /*
        1.删除关联表中的关系
        2.向关联表中插入新数据
        3.修改数据
        */
        Integer id = checkGroup.getId();
        checkGroupDao.deleteAssociation(id);

        setCheckGroupAndCheckItem(id,checkItemIds);

        checkGroupDao.edit(checkGroup);
    }




    public void deleteById(int id) {
        checkGroupDao.deleteAssociation(id);
        checkGroupDao.deleteById(id);
    }

    public List<CheckGroup> findAll(){
        return checkGroupDao.findAll();
    };


}
