package com.qty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qty.entity.SysDept;
import com.qty.entity.SysRole;
import com.qty.entity.Ztree;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface SysDeptService extends IService<SysDept> {


    //根据部门ID获取子节点的id列表
    List<Long>getSubDeptIdList(Long deptId);

    //查询部门管理列表（根据注解分数据权限权限）
    List<SysDept>selectDeptList(SysDept dept);

    //查询部门管理列表（根据注解分数据权限权限）转成数的结构列表
    List<Ztree>selectDeptListTree(SysDept dept);

    //获得已经勾选的部门id
    List<Long>roleDeptData(Long roleId);

    /**
     * 根据角色ID查询菜单
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    public List<Ztree> roleDeptTreeData(SysRole role);



}
