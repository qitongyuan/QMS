package com.qty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qty.entity.SysRole;
import com.qty.mapper.SysRoleMapper;
import com.qty.service.SysRoleService;
import com.qty.util.PageUtil;
import com.qty.util.QueryUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> getRolesById(Long userId) {
        //根据用户ID获取角色列表
        List<SysRole>roles= sysRoleMapper.getRolesById(userId);
        return roles;
    }

    /**
     * 分页模糊查询
     * @param params
     * @return
     */
    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String search=(params.get("search")!=null)?(String)params.get("search"):"";
        //获取分页信息
        IPage<SysRole>page=new QueryUtil<SysRole>().getQueryPage(params);
        QueryWrapper wrapper=new QueryWrapper<SysRole>()
                .like(StringUtils.isNotBlank(search),"role_name",search);
        IPage<SysRole>res=this.page(page,wrapper);
        return new PageUtil(res);
    }
}
