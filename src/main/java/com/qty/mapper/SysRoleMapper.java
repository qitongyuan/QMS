package com.qty.mapper;

import com.qty.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    List<SysRole>getRolesById(Long userId);
}