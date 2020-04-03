package com.qty.shiro;

import com.alibaba.fastjson.JSONObject;
import com.qty.entity.SysUser;
import com.qty.util.ConstantParameter;
import com.qty.util.JwtRedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qty
 * shiro工具类，主要用于生成加密密码
 * 获取当前登录用户信息
 * date 2020-02-06
 */
@Slf4j
public class ShiroUtil {


    /**
     * 使用sha256对用户输入密码进行加密
     * @param password
     * @param salt
     * @return
     */
    public static String sha256(String password, String salt) {
        return new SimpleHash(ConstantParameter.HASHALGORITHMNAME, password, salt, ConstantParameter.HASHITERATIONS).toString();
    }



    //获取redis中的token,解析token拿到当前登录用户
    public static SysUser getUserInfo(){
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String accessToken=request.getHeader("Authorization");
        Claims claims= null;
        try {
            claims = JwtRedisUtil.validateJWT(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (claims==null){
            return null;
        }
        String userInfo= claims.getSubject();
        SysUser sysUser = JSONObject.parseObject(userInfo, SysUser.class);
        return sysUser;
    }

    //根据token获取当前用户
    public SysUser getUserInfo(String accessToken){
        Claims claims= null;
        try {
            claims = JwtRedisUtil.validateJWT(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (claims==null){
            return null;
        }
        String userInfo= claims.getSubject();
        SysUser sysUser = JSONObject.parseObject(userInfo, SysUser.class);
        return sysUser;
    }

//    public static void main(String[] args) {
//        String password="123456";
//        String salt= RandomStringUtils.randomAlphanumeric(20);
//        System.out.println("盐值 "+salt);
//        System.out.println("密码2 "+ShiroUtil.sha256(password, salt));
//        System.out.println("密码2 "+ShiroUtil.sha256(password, "1TsRU8nItmj4HvujFlxY"));
//    }
}
