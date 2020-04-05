package com.qty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.qty.entity.SysRole;
import com.qty.entity.SysRoleDept;
import com.qty.entity.SysRoleMenu;
import com.qty.entity.SysUserRole;
import com.qty.mapper.SysRoleDeptMapper;
import com.qty.mapper.SysRoleMapper;
import com.qty.mapper.SysRoleMenuMapper;
import com.qty.mapper.SysUserRoleMapper;
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

    @Resource
    private SysRoleDeptMapper sysRoleDeptMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

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
     * 新增角色菜单信息（菜单权限）
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

    /**
     * 新增角色部门信息（数据权限）
     * @param role
     * @return
     */
    public int insertRoleDept(SysRole role){
        int rows=1;
        List<SysRoleDept>sysRoleDepts= Lists.newArrayList();
        for (Long deptId:role.getDeptIdList()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            sysRoleDepts.add(rd);
        }
        if (sysRoleDepts.size()>0){
            rows=sysRoleDeptMapper.insertBatch(sysRoleDepts);
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

    @Transactional
    @Override
    public boolean authDataScope(SysRole role) {
        //修改角色信息（主要修改角色对应的DataScope）
        this.updateById(role);
        //删除角色与部门的关联信息
        sysRoleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        //维护新的角色部门信息（如若自定义数据权限则插入有值，如若其他则不对其做修改）
        if (insertRoleDept(role)>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int insertAuthUsers(Long roleId, String userIds) {
        String[] users=userIds.split(",");
        List<SysUserRole>list=Lists.newArrayList();
        for (String userId:users) {
            SysUserRole userRole=new SysUserRole();
            userRole.setUserId(Long.parseLong(userId));
            userRole.setRoleId(roleId);
            list.add(userRole);
        }
        return sysUserRoleMapper.insertBatch(list);
    }

    @Override
    public int deleteAuthUser(Long roleId, Long userId) {
        return sysUserRoleMapper.deleteUserRole(roleId, userId);
    }
}
