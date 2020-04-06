package com.qty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qty.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    //超管查菜单的接口
    List<SysMenu> queryAllMenu(SysMenu menu);

    //角色用户查菜单的接口
    List<SysMenu>queryMenuByUserId(@Param("menu") SysMenu menu, @Param("userId") Long userId);

    int selectCountRoleMenuId(Long menuId);

    int selectCountMenuByParentId(Long parentId);

    //同一父级菜单下不能重名
    int checkMenuNameRepeat(@Param("menuName") String menuName, @Param("parentId") Long parentId);

    //根据角色ID查询已经勾选中的菜单ID
    List<Long> selectRoleMenuByRoleId(Long roleId);
}
