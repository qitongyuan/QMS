package com.qty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qty.entity.SysMenu;
import com.qty.entity.SysRole;
import com.qty.entity.Ztree;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    //插叙菜单列表（这里后续也要转为树形结构所以不需要分页）
    public List<SysMenu>selectMenu(SysMenu menu,Long userId);

//    在角色管理中新增时菜单树列表
    public List<Ztree> roleMenuTreeData(SysRole role, Long userId);

    //菜单管理中新增时菜单树列表
    public List<Ztree>menuTreeData(Long userId);

    //菜单已使用情况
    public int selectCountRoleMenuByMenuId(Long menuId);

    public int selectCountMenuByParentId(Long menuId);

    public boolean checkMenuNameRepeat(SysMenu menu);

}
