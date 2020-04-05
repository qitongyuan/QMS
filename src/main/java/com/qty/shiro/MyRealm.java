package com.qty.shiro;



import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.qty.entity.SysUser;
import com.qty.mapper.SysUserMapper;
import com.qty.util.ConstantParameter;
import com.qty.util.JwtRedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * MyRealm:自定义一个授权
 *
 * @author zhangxiaoxiang
 * @date: 2019/07/12
 */

@Component
@Slf4j
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SysUserMapper sysUserMapper;


    /**
     * 必须重写此方法，不然Shiro会报错
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取token
        String accessToken=(String)principals.getPrimaryPrincipal();
        SysUser sysUser =new ShiroUtil().getUserInfo(accessToken);
        //查询权限集合赋给shiro
        List<String>roles=Lists.newLinkedList();
        List<String>perms= Lists.newLinkedList();
        roles= sysUserMapper.queryAllRoles(sysUser.getUserId());
        //在数据库中关联查询该用户的权限集合
        perms= sysUserMapper.queryAllPerms(sysUser.getUserId());
        //对于每一个授权编码进行 , 的解析拆分
        Set<String> stringPermissions= Sets.newHashSet();
        if (perms!=null && !perms.isEmpty()){
            for (String p:perms){
                if (StringUtils.isNotBlank(p)){
                    stringPermissions.addAll(Arrays.asList(StringUtils.split(p.trim(),",")));
                }
            }
        }
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        for (String role:roles) {
            //赋予角色
            info.addRole(role);
        }
        //赋予资源
        info.addStringPermissions(stringPermissions);
        return info;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     * @param auth
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String accessToken = (String) auth.getCredentials();
        // 将token提交后,在该方法中验证token的合法性
        try {
            //验证token的合法性全部交给jwt
            Claims claims= JwtRedisUtil.validateJWT(accessToken);
            if (claims==null || StringUtils.isBlank(claims.getId())){
                throw new AuthenticationException("无效的Token");
            }
            //正常情况下，到这一步验证就算结束了，但是我们需要用到rendis的过期功能，其余事宜交有redis完成
            //失效过期等判断交给缓存中间件redis
            final String key= ConstantParameter.JWT_TOKEN_REDIS_KEY_PREFIX+claims.getId();
            if (!stringRedisTemplate.hasKey(key)){
                throw new AuthenticationException("Token不存在或已经过期-请重新登录!");
            }
            //最后是校验一下当前过来的Token是否是之前采用加密算法生成的Token并存于缓存中
            ValueOperations<String,String> valueOperations=stringRedisTemplate.opsForValue();
            String redisToken=valueOperations.get(key);
            if (!accessToken.equals(redisToken)){
                throw new AuthenticationException("无效的Token!");
            }
            //执行到下面这一步时，即代表验证成功
            return new SimpleAuthenticationInfo(accessToken, accessToken, "my_realm");
        }catch (Exception e){
            throw new AuthenticationException(e.getMessage());
        }
    }
}
