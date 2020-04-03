package com.qty.service;

import com.qty.entity.SysRole;

import java.util.List;

public interface SysRoleService {

    //根据用户id查询该用户所拥有的
    List<SysRole>getRolesById(Long userId);
}
