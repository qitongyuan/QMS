package com.qty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qty.entity.SysRole;
import com.qty.util.PageUtil;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    //根据用户ID查询用户角色
    List<SysRole>getRolesById(Long userId);
}
