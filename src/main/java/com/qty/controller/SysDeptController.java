package com.qty.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qty.entity.SysDept;
import com.qty.entity.SysRole;
import com.qty.entity.Ztree;
import com.qty.response.BaseResponse;
import com.qty.response.StatusCode;
import com.qty.service.SysDeptService;
import com.qty.util.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "部门", tags = "部门")
@RestController
@AllArgsConstructor
@RequestMapping("/sys/dept")
public class SysDeptController {

    private SysDeptService sysDeptService;


    //TODO 少一个接口获取真正的部门树（在新增时后用）参考若依的/treeData
    //TODO 少一个已勾选的接口
    //todo 全部前端转树形结构

    @ApiOperation(value = "部门列表展示(带5种权限)", notes = "部门列表展示(带5种权限)")
    @PostMapping("/deptList")
    public BaseResponse deptList(@RequestBody SysDept sysDept){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        List<SysDept>depts=Lists.newArrayList();
        try {
            depts=sysDeptService.selectDeptList(sysDept);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(depts);
        return response;
    }

    //在角色管理中新增角色时加载的部门树
    @ApiOperation(value = "在角色管理中新增角色时加载的部门树", notes = "在角色管理中新增角色时加载的部门树")
    @GetMapping("/roleDeptTreeData")
    public List<Ztree> roleDeptTreeData(SysRole role){
        List<Ztree>sysDepts= sysDeptService.roleDeptTreeData(role);
        return sysDepts;
    }

    @ApiOperation(value = "在部门管理中新增部门时加载的部门树", notes = "在部门管理中新增部门时加载的部门树")
    @GetMapping("/deptTreeData")
    public List<Ztree> deptTreeData(){
        List<Ztree>sysDepts= sysDeptService.selectDeptListTree(new SysDept());
        return sysDepts;
    }

//    @ApiOperation(value = "部门树勾选数据", notes = "部门树勾选数据")
//    @GetMapping("/roleDeptData")
//    public BaseResponse getRoleDept(@RequestParam Long roleId){
//        BaseResponse response=new BaseResponse(StatusCode.Success);
//        List<Long>deptIds=Lists.newArrayList();
//        try {
//            deptIds=sysDeptService.roleDeptData(roleId);
//        }catch (Exception e){
//            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
//        }
//        response.setData(deptIds);
//        return response;
//    }

    @ApiOperation(value = "部门新增", notes = "部门新增")
    @PostMapping(value = "/save")
    public BaseResponse save(@RequestBody @Validated SysDept sysDept, BindingResult result) {

        //校验工具做校验
        String res = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            sysDeptService.save(sysDept);
        } catch (Exception e) {
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

//    不需要，前端直接显示
//    @ApiOperation(value = "获取部门详情", notes = "获取部门详情")
//    @GetMapping("/detail/{deptId}")
//    public BaseResponse detail(@PathVariable Long deptId){
//        BaseResponse response=new BaseResponse(StatusCode.Success);
//        Map<String,Object>resMap=Maps.newHashMap();
//        try{
//            resMap.put("dept",sysDeptService.getById(deptId));
//        }catch (Exception e){
//            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
//        }
//        response.setData(resMap);
//        return response;
//    }

    @ApiOperation(value = "修改部门", notes = "修改部门")
    @PostMapping(value = "/update")
    public BaseResponse update(@RequestBody @Validated SysDept dept,BindingResult result){
        String res=ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }
        if (dept.getDeptId()==null||dept.getDeptId()<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            sysDeptService.updateById(dept);
        } catch (Exception e) {
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "删除部门", notes = "删除部门")
    @PostMapping(value = "/delete")
    public BaseResponse delete(Long deptId){
        if (deptId==null || deptId<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            //如果当前部门有子部门，则需要要求先删除下面的所有子部门，再删除当前部门
            List<Long> subIds=sysDeptService.getSubDeptIdList(deptId);
            if (subIds!=null && !subIds.isEmpty()){
                return new BaseResponse(StatusCode.DeptHasSubDeptCanNotBeDelete);
            }

            sysDeptService.removeById(deptId);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }




}
