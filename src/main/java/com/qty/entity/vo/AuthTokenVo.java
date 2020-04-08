package com.qty.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回给前端的回显实体（登陆后）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenVo implements Serializable{

    private String accessToken;

    private Long accessExpire;

    private String userName;

    private String userEmail;

    private String userTelephoneNumber;

    private String tenantId;

}