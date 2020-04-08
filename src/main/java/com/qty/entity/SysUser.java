package com.qty.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName(value = "tb_users")
public class SysUser extends BaseEntity implements Serializable {

    @TableId(value = "user_id",type = IdType.AUTO)
    private Long userId;

    private String userIp;

    private String userName;

    private String userPassword;

    private String salt;

    private String userEmail;

    private String userProfilePhoto;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date userRegistrationTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date userBirthday;

    private int userAge;

    private String userTelephoneNumber;

    private String userNickname;

    private Long deptId;

    //租户id
    private String tenantId;


}
