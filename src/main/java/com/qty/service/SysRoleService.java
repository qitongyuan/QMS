package com.qty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qty.entity.SysRole;
import com.qty.util.PageUtil;

import java.util.List;
import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    //根据用户id查询该用户所拥有的
    List<SysRole>getRolesById(Long userId);

    PageUtil queryPage(Map<String,Object> params);
}
