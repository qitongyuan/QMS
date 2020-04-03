package com.qty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qty.entity.SysDept;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface SysDeptService extends IService<SysDept> {

    //获取当前登录用户的部门数据ID列表
    public Set<Long>getCurrUserDataDeptId();

    //获取子节点的id列表
    List<Long>getSubDeptIdList(Long deptId);


    //获取部门列表
    List<SysDept> queryAll(Map<String,Object> map);

    //================新
    List<SysDept>selectDeptList(SysDept dept);

}
