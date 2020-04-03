package com.qty.util;

import com.alibaba.fastjson.JSONObject;
import com.qty.entity.SysUser;
import com.qty.response.StatusCode;
import io.jsonwebtoken.*;
import org.bouncycastle.util.encoders.Base64;
import org.joda.time.DateTime;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * jwt+Redis通用工具类
 * 主要用于创建token和解析token
 *
 * **/
public class JwtRedisUtil {

    //生成密钥
    public static SecretKey generalKey(){
        byte[] encodedKey=Base64.decode(ConstantParameter.JWT_SECRET);
        SecretKey key=new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
        return key;
    }
    //创建token
    public static String createJWT(final String id, final SysUser subject){
        //定义生成签名的算法
        SignatureAlgorithm algorithm=SignatureAlgorithm.HS256;
        //定义生成签名的密钥
        SecretKey key=generalKey();

        Date now=DateTime.now().toDate();
        //借助于第三方依赖组件jwt的api来实现
        JwtBuilder builder=Jwts.builder()
                //用户id
                .setId(id)
                //主体(user类)
                .setSubject(JSONObject.toJSONString(subject))
                //签发者
                .setIssuer("qty")
                //签发时间
                .setIssuedAt(now)
                //签发时指定 加密算法、密钥
                .signWith(algorithm,key);

        //设定过期时间（使用redis的情况下不需要设定过期时间，Token串依靠redis过期）
//      if (expireMills>=0){
//            Long realExpire=System.currentTimeMillis() + expireMills;
//            builder.setExpiration(new Date(realExpire));
//        }
        //生成access token
        return builder.compact();
    }

    //验证解析token
    public static Claims validateJWT(final String accessToken) throws Exception{
        Claims claims=null;
        try {
            claims=parseJWT(accessToken);
        }catch (SignatureException e){
            throw new RuntimeException(StatusCode.TokenValidateCheckFail.getMsg());
        }catch (Exception e){
            throw new RuntimeException(StatusCode.TokenValidateCheckFail.getMsg());
        }
        return claims;
    }


    //解析token（被validateJWT调用）
    public static Claims parseJWT(final String accessToken) throws Exception{
        SecretKey key=generalKey();
        return Jwts.parser().setSigningKey(key).parseClaimsJws(accessToken).getBody();
    }

}










































