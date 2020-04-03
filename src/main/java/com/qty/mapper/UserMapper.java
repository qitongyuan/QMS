package com.qty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qty.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author qty
 * date 2020-02-02
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

    //根据用户ID查询该用户的所有权限
    List<String> queryAllPerms(Long userId);

    List<String>queryAllRoles(Long userId);
}
