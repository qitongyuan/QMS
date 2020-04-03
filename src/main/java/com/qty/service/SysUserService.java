package com.qty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qty.entity.SysUser;
import com.qty.entity.vo.UpdatePsdVo;


/**
 * @author qty
 * date 2020-02-02
 */
public interface SysUserService extends IService<SysUser> {

    //根据用户名密码在数据库中查找对应实体类
    public SysUser authUser(String userName, String password) throws Exception;

    //密码修改
    public void updatePasswd(final SysUser sysUser, final UpdatePsdVo updatePsdVo)throws Exception;

    //注销redis中的token
    public void invalidateByAccessToken(final String accessToken) throws Exception;
}
