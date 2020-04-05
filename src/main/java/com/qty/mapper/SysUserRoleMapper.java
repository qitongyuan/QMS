package com.qty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qty.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    int insertBatch(List<SysUserRole>sysUserRoles);

    int deleteUserRole(Long roleId,Long userId);
}
