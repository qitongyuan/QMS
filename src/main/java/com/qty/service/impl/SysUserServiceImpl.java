package com.qty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qty.entity.SysUser;
import com.qty.entity.vo.UpdatePsdVo;
import com.qty.mapper.UserMapper;
import com.qty.service.SysUserService;
import com.qty.shiro.ShiroUtil;
import com.qty.util.ConstantParameter;
import com.qty.util.JwtRedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author qty
 * date 2020-02-02
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements SysUserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public SysUser authUser(String userName, String password) throws Exception {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
            throw new RuntimeException("用户名或者密码不能为空！");
        }
        QueryWrapper<SysUser> wrapper=new QueryWrapper<>();
        wrapper.eq("user_name",userName);
        SysUser sysUser =userMapper.selectOne(wrapper);
        if (sysUser ==null){
            throw new RuntimeException("当前用户不存在！");
        }
        //将当前前端输入的密码和数据库中拿出来的盐值结合生成加密后的密码，再做比对
        String passwdEncpy=ShiroUtil.sha256(password.trim(), sysUser.getSalt().trim());
        if (!passwdEncpy.equals(sysUser.getUserPassword())){
            throw new RuntimeException("用户名密码不匹配！");
        }
        return sysUser;
    }

    @Override
    @Transactional
    public void updatePasswd(SysUser sysUser, UpdatePsdVo updatePsdVo) throws Exception {
        //在数据库中根据token中的id和name查找是否有当前用户
        QueryWrapper<SysUser>wrapper=new QueryWrapper<>();
        wrapper.eq("user_id", sysUser.getUserId());
        wrapper.eq("user_name", sysUser.getUserName());
        SysUser sysUserInDb = userMapper.selectOne(wrapper);
        if (sysUserInDb ==null){
            throw new RuntimeException("当前Token对应的是无效的用户！");
        }
        if (!sysUserInDb.getUserPassword().equals(updatePsdVo.getOldPassword())){
            throw new RuntimeException("旧密码不匹配！");
        }
        sysUserInDb.setUserPassword(updatePsdVo.getNewPassword());
        int res=userMapper.updateById(sysUserInDb);
        if (res<=0){
            throw new RuntimeException("修改密码失败~请重新尝试或者联系管理员！");
        }
        //拿到当前header里的token，取出token中的键然后在redis中注销,修改完密码需要将Redis中的token失效掉
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String accessToken=request.getHeader("Authorization");
        this.invalidateByAccessToken(accessToken);
    }

    //失效token(一般修改密码、退出登录的时候调用)
    public void invalidateByAccessToken(final String accessToken) throws Exception{
        if (StringUtils.isNotBlank(accessToken)){
            //正确解析access token
            Claims claims= JwtRedisUtil.validateJWT(accessToken);
            //key是UserAuth:JWT:Key:+userId+
            final String key= ConstantParameter.JWT_TOKEN_REDIS_KEY_PREFIX+claims.getId();
            if (stringRedisTemplate.hasKey(key)){
                stringRedisTemplate.delete(key);
            }
        }
    }
}
