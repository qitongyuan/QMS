package com.qty.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qty.entity.SysDept;
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

    @ApiOperation(value = "部门列表展示(带5种权限)", notes = "部门列表展示(带5种权限)")
    @PostMapping("/deptList")
    public BaseResponse deptList(@RequestBody SysDept sysDept){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        response.setData(sysDeptService.selectDeptList(sysDept));
        return response;
    }

    /**
     * 由前端转化成tree型结构
     *
     * @return
     */
    @ApiOperation(value = "部门列表展示", notes = "部门列表展示")
    @GetMapping("/list")
    public List<SysDept> list() {
        return sysDeptService.queryAll(Maps.newHashMap());
    }

    /**
     * 不同的返回类型
     *
     * @return
     */
    @ApiOperation(value = "部门树展示", notes = "部门树展示")
    @GetMapping("/tree")
    public BaseResponse selectTree() {
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String, Object> resMap = Maps.newHashMap();
        List<SysDept> deptList = Lists.newLinkedList();
        try {
            deptList = sysDeptService.queryAll(Maps.newHashMap());

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        resMap.put("deptList", deptList);
        response.setData(resMap);
        return response;
    }

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

    @ApiOperation(value = "获取部门详情", notes = "获取部门详情")
    @GetMapping("/detail/{deptId}")
    public BaseResponse detail(@PathVariable Long deptId){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object>resMap=Maps.newHashMap();
        try{
            resMap.put("dept",sysDeptService.getById(deptId));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

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
