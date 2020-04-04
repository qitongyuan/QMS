package com.qty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qty.entity.SysRole;
import com.qty.util.PageUtil;

import java.util.List;
import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    //根据用户id查询该用户所拥有的
    List<SysRole>getRolesById(Long userId);

    //分页显示角色信息
    PageUtil queryPage(Map<String,Object> params);

    //校验角色名是否重复
    public boolean checkRoleNameUnique(SysRole role);

    //校验角色权限码是否重复
    public boolean checkRoleKeyUnique(SysRole role);

    //新增角色信息（维护菜单信息）
    public boolean insertRole(SysRole role);
}
