package com.qty.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("tb_role")
public class SysRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id",type = IdType.AUTO)
    private Long roleId;

    @NotBlank(message="角色名称不能为空")
    private String roleName;

    private String remark;

    @NotBlank(message="角色编码不能为空")
    private String roleKey;

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限）
     */
    private String dataScope;

    /**
     * 菜单组
     */
    @TableField(exist=false)
    private List<Long> menuIdList;

    /**
     * 部门组
     */
    @TableField(exist=false)
    private List<Long> deptIdList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public boolean isAdmin(){
        return isAdmin(this.roleId);
    }
    public static boolean isAdmin(Long roleId)
    {
        return roleId != null && 1L == roleId;
    }

}
