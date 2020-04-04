package com.qty.controller;

import com.qty.entity.SysRole;
import com.qty.response.BaseResponse;
import com.qty.response.StatusCode;
import com.qty.service.SysRoleService;
import com.qty.util.PageUtil;
import com.qty.util.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 角色管理中可以分别查看该角色拥有的菜单权限和数据权限
 */
@Api(value = "角色", tags = "角色")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/role")
public class SysRoleController {

    private SysRoleService sysRoleService;

    //此处策略改变，取详情数据时由前端来操作
    @ApiOperation(value = "角色列表展示", notes = "部门列表展示")
    @PostMapping("/list")
    public BaseResponse list(@RequestBody Map<String,Object>params){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            PageUtil page=sysRoleService.queryPage(params);
            response.setData(page);
        } catch (Exception e) {
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //ToDO 角色的增删改

    /**
     * 新增角色，在角色新增的同时维护进菜单信息
     * @param role
     * @return
     */
    @ApiOperation(value = "角色新增", notes = "角色新增")
    @PostMapping("/add")
    public BaseResponse add(@RequestBody @Validated SysRole role, BindingResult result){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        //校验用户输入不为空
        String res = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }
        //判断角色名是否重复
        if (!sysRoleService.checkRoleNameUnique(role)){
            throw new RuntimeException("角色名重复");
        }
        if (!sysRoleService.checkRoleKeyUnique(role)){
            throw new RuntimeException("角色编码重复");
        }
        //新增角色同时新增菜单信息
        try {
            boolean flag= sysRoleService.insertRole(role);
            if (!flag){
                throw new RuntimeException("角色新增失败");
            }
        } catch (Exception e) {
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 角色修改
     */
    @PostMapping("/update")
    public BaseResponse updateRole(@Validated SysRole role,BindingResult result){
        //不允许操作超级管理的角色
        sysRoleService.checkRoleAllowed(role);
        BaseResponse response=new BaseResponse(StatusCode.Success);
        //校验用户输入不为空
        String res = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }

        //判断角色名是否重复
        if (!sysRoleService.checkRoleNameUnique(role)){
            throw new RuntimeException("角色名重复");
        }
        if (!sysRoleService.checkRoleKeyUnique(role)){
            throw new RuntimeException("角色编码重复");
        }
        //TODO 修改完角色后是否需推出登录（后续完成）
        try {
            boolean flag= sysRoleService.updateRole(role);
            if (!flag){
                throw new RuntimeException("角色修改失败");
            }
        } catch (Exception e) {
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());;
        }
        return response;
    }


}
