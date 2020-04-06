package com.qty.controller;

import com.google.common.collect.Lists;
import com.qty.entity.SysMenu;
import com.qty.entity.SysRole;
import com.qty.entity.Ztree;
import com.qty.response.BaseResponse;
import com.qty.response.StatusCode;
import com.qty.service.SysMenuService;
import com.qty.shiro.ShiroUtil;
import com.qty.util.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "菜单",tags = "菜单")
@RestController
@RequestMapping("/sys/menu")
@AllArgsConstructor
@Slf4j
public class SysMenuController {

    private SysMenuService sysMenuService;

    //根据当前用户和搜索框获取菜单列表，
    // 前端转化成树结构
    @ApiOperation(value="菜单列表展示",notes="菜单列表展示")
    @PostMapping(value = "/list")
    public BaseResponse list(@RequestBody SysMenu menu){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        List<SysMenu>list= Lists.newArrayList();
        try {
            list=sysMenuService.selectMenu(menu, ShiroUtil.getUserInfo().getUserId());
        } catch (Exception e) {
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(list);
        return response;
    }

    //TODO 角色管理中加载所有菜单树（在新增的时候用）
    //TODO 菜单管理中加载所有菜单树（新增时用）

    @ApiOperation(value="在角色管理中新增时菜单树列表",notes="在角色管理中新增时菜单树列表")
    @PostMapping("/roleMenuTreeData")
    public List<Ztree>roleMenuTreeData(@RequestBody SysRole role){
        return sysMenuService.roleMenuTreeData(role,ShiroUtil.getUserInfo().getUserId());
    }

    @ApiOperation(value="在菜单管理中新增时菜单树列表",notes="在菜单管理中新增时菜单树列表")
    @PostMapping("/MenuTreeData")
    public List<Ztree>roleMenuTreeData(){
        return sysMenuService.menuTreeData(ShiroUtil.getUserInfo().getUserId());
    }


    //菜单不支持批量删除
    @ApiOperation(value="菜单删除",notes="菜单删除")
    @GetMapping("/delete/{menuId}")
    public BaseResponse delete(@PathVariable("menuId") Long menuId){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        //判断菜单是否已分配
        if (sysMenuService.selectCountRoleMenuByMenuId(menuId)>0){
            throw new RuntimeException("菜单已分配,删除被拒");
        }
        if (sysMenuService.selectCountMenuByParentId(menuId)>0){
            throw new RuntimeException("存在子菜单，请先删除子菜单");
        }
        try {
            sysMenuService.removeById(menuId);
        } catch (Exception e) {
            throw new RuntimeException("删除失败");
        }
        return response;
    }

    @ApiOperation(value="菜单新增",notes="菜单新增")
    @PostMapping("/add")
    public BaseResponse add(@Validated @RequestBody SysMenu menu, BindingResult result){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        //校验工具做校验
        String res = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }
        //判断菜单名称是否存在
        if (!sysMenuService.checkMenuNameRepeat(menu)){
            throw new RuntimeException("菜单名称重复");
        }
        try {
            sysMenuService.save(menu);
        } catch (Exception e) {
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    @ApiOperation(value="菜单编辑",notes="菜单编辑")
    @PostMapping("/update")
    public BaseResponse update(@Validated @RequestBody SysMenu menu, BindingResult result){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        //校验工具做校验
        String res = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.Fail.getCode(),res);
        }
        //判断菜单名称是否存在
        if (!sysMenuService.checkMenuNameRepeat(menu)){
            throw new RuntimeException("菜单名称重复");
        }
        try {
            sysMenuService.updateById(menu);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    @ApiOperation(value="检验菜单是否重名",notes="检验菜单是否重名")
    @PostMapping("/checkMenuNameUnique")
    public BaseResponse checkRepeat(@RequestBody SysMenu menu){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        boolean flag=sysMenuService.checkMenuNameRepeat(menu);
        if (!flag){
            throw new RuntimeException("菜单名称重复");
        }
        return response;
    }
}
