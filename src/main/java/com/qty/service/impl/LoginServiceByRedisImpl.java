package com.qty.service.impl;

import com.qty.entity.SysUser;
import com.qty.entity.vo.AuthTokenVo;
import com.qty.response.BaseResponse;
import com.qty.response.StatusCode;
import com.qty.service.LoginServiceByRedis;
import com.qty.service.SysUserService;
import com.qty.util.ConstantParameter;
import com.qty.util.JwtRedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author qty
 * date 2020-02-02
 */
@Slf4j
@Service
public class LoginServiceByRedisImpl implements LoginServiceByRedis {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public AuthTokenVo authAndCreateToken(String userName, String password) throws Exception {
        //访问数据库获取User对象
        SysUser sysUser = sysUserService.authUser(userName,password);
        if (sysUser !=null){
//            String accessToken= JwtUtil.createJWT(sysUser.getUserId().toString(),sysUser, ConstantParameter.ACCESS_TOKEN_EXPIRE);
            //创建token,不需要过期时间了
            String accessToken= JwtRedisUtil.createJWT(sysUser.getUserId().toString(), sysUser);
            //缓存token设置过期失效时间
            ValueOperations<String,String>valueOperations= stringRedisTemplate.opsForValue();
            //将token存入到redis中设置过期时间
            valueOperations.set(ConstantParameter.JWT_TOKEN_REDIS_KEY_PREFIX+ sysUser.getUserId(),accessToken,ConstantParameter.ACCESS_TOKEN_EXPIRE, TimeUnit.MILLISECONDS);
            log.info("--jwt+redis用户认证成功，成功生成accessToken--");
            AuthTokenVo tokenModel=new AuthTokenVo(accessToken,ConstantParameter.ACCESS_TOKEN_EXPIRE, sysUser.getUserName(), sysUser.getUserEmail(), sysUser.getUserTelephoneNumber());
            return tokenModel;
        }
        //如果获取不到返回空
        return null;
    }

    @Override
    public BaseResponse validateToken(String accessToken) {
        try {
            //验证token的合法性全部交给jwt
            Claims claims=JwtRedisUtil.validateJWT(accessToken);
            if (claims==null || StringUtils.isBlank(claims.getId())){
                return new BaseResponse(StatusCode.AccessTokenInvalidate);
            }
            //正常情况下，到这一步验证就算结束了，但是我们需要用到rendis的过期功能，其余事宜交有redis完成
            //失效过期等判断交给缓存中间件redis
            final String key=ConstantParameter.JWT_TOKEN_REDIS_KEY_PREFIX+claims.getId();
            if (!stringRedisTemplate.hasKey(key)){
                return new BaseResponse(StatusCode.AccessTokenNotExistRedis);
            }
            //最后是校验一下当前过来的Token是否是之前采用加密算法生成的Token并存于缓存中
            ValueOperations<String,String> valueOperations=stringRedisTemplate.opsForValue();
            String redisToken=valueOperations.get(key);
            if (!accessToken.equals(redisToken)){
                return new BaseResponse(StatusCode.AccessTokenInvalidate);
            }
            //执行到下面这一步时，即代表验证成功
            return new BaseResponse(StatusCode.Success);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
    }
}
