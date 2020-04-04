package com.qty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qty.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    int insertBatch(List<SysRoleMenu>roleMenuList);

    //通过角色ID删除所有的关联菜单
    int deleteRoleMenuByRoleId(Long roleId);
}
