package com.qty.service.impl;

import com.qty.entity.SysRole;
import com.qty.mapper.SysRoleMapper;
import com.qty.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> getRolesById(Long userId) {
        //根据用户ID获取角色列表
        List<SysRole>roles= sysRoleMapper.getRolesById(userId);
        return roles;
    }
}
