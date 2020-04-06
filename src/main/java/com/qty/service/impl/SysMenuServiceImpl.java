package com.qty.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qty.entity.SysMenu;
import com.qty.entity.SysRole;
import com.qty.entity.Ztree;
import com.qty.mapper.SysMenuMapper;
import com.qty.service.SysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> selectMenu(SysMenu menu, Long userId) {
        List<SysMenu> menuList=null;
        //判断是否是超管，超管拥有最大权限
        if (userId==1L){
            //全部菜单
            menuList=sysMenuMapper.queryAllMenu(menu);
        }else {
            //返回该角色所分配的菜单
            menuList=sysMenuMapper.queryMenuByUserId(menu,userId);
        }
        return menuList;
    }

    @Override
    public List<Ztree> roleMenuTreeData(SysRole role, Long userId) {
        Long roleId = role.getRoleId();
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<SysMenu> menuList = this.selectMenu(null,userId);
        if (com.qty.util.StringUtils.isNotNull(roleId))
        {
            List<Long>menuIds=sysMenuMapper.selectRoleMenuByRoleId(roleId);
            ztrees = initZtree(menuList, menuIds, true);
        }
        else
        {
            ztrees = initZtree(menuList, null, true);
        }
        return ztrees;
    }

    @Override
    public List<Ztree> menuTreeData(Long userId) {
        List<SysMenu> menuList = this.selectMenu(null,userId);
        return initZtree(menuList);
    }

    public List<Ztree> initZtree(List<SysMenu> menuList, List<Long> menuIds, boolean permsFlag)
    {
        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = com.qty.util.StringUtils.isNotNull(menuIds);
        for (SysMenu menu : menuList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(menu.getMenuId());
            ztree.setPId(menu.getParentId());
            //这里放菜单权限标识
            ztree.setName(transMenuName(menu, permsFlag));
            ztree.setTitle(menu.getName());
            if (isCheck)
            {
                ztree.setChecked(menuIds.contains(menu.getMenuId()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }

    public List<Ztree> initZtree(List<SysMenu> menuList)
    {
        return initZtree(menuList, null, false);
    }

    public String transMenuName(SysMenu menu, boolean permsFlag)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(menu.getName());
        if (permsFlag)
        {
            sb.append("<font color=\"#888\">&nbsp;&nbsp;&nbsp;" + menu.getPerms() + "</font>");
        }
        return sb.toString();
    }

    @Override
    public int selectCountRoleMenuByMenuId(Long menuId) {
        return sysMenuMapper.selectCountRoleMenuId(menuId);
    }

    @Override
    public int selectCountMenuByParentId(Long menuId) {
        return sysMenuMapper.selectCountMenuByParentId(menuId);
    }

    @Override
    public boolean checkMenuNameRepeat(SysMenu menu) {
        int res= sysMenuMapper.checkMenuNameRepeat(menu.getName(),menu.getParentId());
        if (res>0){
            return false;
        }else {
            return true;
        }
    }
}
