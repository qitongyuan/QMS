package com.qty.controller;


import com.qty.response.BaseResponse;
import com.qty.response.StatusCode;
import com.qty.service.LoginServiceByRedis;
import com.qty.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qty
 * date 2020-02-02
 */
@Api(value = "登入登出",tags = "登入登出")
@RestController
@RequestMapping("/sys")
@Slf4j
public class LoginController {


    @Autowired
    private LoginServiceByRedis loginServiceByRedis;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户基于shiro完成认账，生成jwt token返回给客户端
     * @param userName
     * @param passwd
     * @return
     */
    @ApiOperation(value="用户登录",notes="用户登录")
    @PostMapping("/login")
    public BaseResponse login(String userName,String passwd) throws Exception{
        log.warn("用户名：{} 密码：{} ",userName,passwd);
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(passwd)){
            return new BaseResponse(StatusCode.UserNamePasswordNotBlank);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
//        try {
            response.setData(loginServiceByRedis.authAndCreateToken(userName, passwd));
//        }catch (Exception e){
//            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
//        }
        return response;
    }

    @ApiOperation(value="用户退出",notes="用户退出")
    @PostMapping("/logout")
    public BaseResponse logout(){
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String accessToken=request.getHeader("Authorization");
        if (StringUtils.isBlank(accessToken)){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            sysUserService.invalidateByAccessToken(accessToken);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

}
