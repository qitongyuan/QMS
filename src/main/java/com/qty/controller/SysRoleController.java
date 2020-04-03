package com.qty.controller;

import com.qty.response.BaseResponse;
import com.qty.response.StatusCode;
import com.qty.service.SysRoleService;
import com.qty.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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


}
