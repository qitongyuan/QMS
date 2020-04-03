package com.qty.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenVo implements Serializable{

    private String accessToken;

    private Long accessExpire;

    private String userName;

    private String userEmail;

    private String userTelephoneNumber;

}