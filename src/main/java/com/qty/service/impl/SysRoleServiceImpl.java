package com.qty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qty.entity.SysRole;
import com.qty.entity.SysRoleMenu;
import com.qty.mapper.SysRoleMapper;
import com.qty.mapper.SysRoleMenuMapper;
import com.qty.service.SysRoleService;
import com.qty.util.PageUtil;
import com.qty.util.QueryUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

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

    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        //角色ID
        Long roleId = com.qty.util.StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info=sysRoleMapper.checkRoleNameUnique(role.getRoleName());
        if (com.qty.util.StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            //已存在
            return false;
        }
        //不存在该角色名
        return true;
    }

    @Override
    public boolean checkRoleKeyUnique(SysRole role) {
        //角色ID
        Long roleId = com.qty.util.StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info=sysRoleMapper.checkRoleKeyUnique(role.getRoleKey());

        if (com.qty.util.StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            //已存在
            return false;
        }
        //不存在该角色名
        return true;
    }

    /**
     * 新增保存角色
     * @param role
     * @return
     */
    @Transactional
    @Override
    public boolean insertRole(SysRole role) {
        role.setCreateTime(DateTime.now().toDate());
        //保存新增角色
        this.save(role);
        if (insertRoleMenu(role)>0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 新增角色菜单信息
     * @param role
     * @return
     */
    public int insertRoleMenu(SysRole role){
        int rows=1;
        List<SysRoleMenu>sysRoleMenus=new ArrayList<>();
        for (Long menuId : role.getMenuIdList()){
            SysRoleMenu rm=new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            sysRoleMenus.add(rm);
        }
        //此处不一定新增该角色的菜单
        if (sysRoleMenus.size()>0){
            rows=sysRoleMenuMapper.insertBatch(sysRoleMenus);
        }
        return rows;
    }

    @Transactional
    @Override
    public boolean updateRole(SysRole role) {
        //根据角色ID进行更新
        this.updateById(role);
        //级联删除所有菜单
        sysRoleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        if (insertRoleMenu(role)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void checkRoleAllowed(SysRole role) {
        if (com.qty.util.StringUtils.isNotNull(role.getRoleId())&&role.isAdmin()){
            throw new RuntimeException("超管不允许被操作");
        }
    }
}
