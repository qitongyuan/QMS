package com.qty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qty.entity.SysRoleDept;
import com.qty.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept> {

    int deleteRoleDeptByRoleId(Long roleId);

    int insertBatch(List<SysRoleDept> roleDepts);
}
