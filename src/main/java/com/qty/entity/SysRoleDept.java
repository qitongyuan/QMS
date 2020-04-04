package com.qty.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("tb_role_dept")
public class SysRoleDept implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    //角色ID
    private Long roleId;

    //部门ID
    private Long deptId;

}
