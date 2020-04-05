package com.qty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qty.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author qty
 * date 2020-02-02
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    //根据用户ID查询该用户的所有权限
    List<String> queryAllPerms(Long userId);

    //根据用户ID查找该用户的所有角色
    List<String>queryAllRoles(Long userId);

    //-------------------以上用于鉴权

    List<SysUser>queryAllAuthUserByRoleId(Map<String, Object> params);

    int queryAllAuthUserCountByRoleId(Map<String, Object> params);

    List<SysUser>queryAllUnAuthUserByRoleId(Map<String, Object> params);

    int queryAllUnAuthUserCountByRoleId(Map<String, Object> params);
}
