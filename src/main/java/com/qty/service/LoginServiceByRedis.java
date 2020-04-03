package com.qty.service;


import com.qty.entity.vo.AuthTokenVo;
import com.qty.response.BaseResponse;

/**
 * @author qty
 * date 2020-02-02
 */
public interface LoginServiceByRedis {

    //登录认证并创建token
    public AuthTokenVo authAndCreateToken(String userName, String password) throws Exception;


    //jwt验证解析token(这个接口方法原来被JwtTokenRedisInterceptor中调用，主要是为了校验token)
    //该接口现在被ShiroJwtFilter这个方法替代
    public BaseResponse validateToken(final String accessToken);

}
