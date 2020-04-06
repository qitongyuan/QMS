package com.qty.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.qty.entity.Sort;
import com.qty.response.BaseResponse;
import com.qty.response.StatusCode;
import com.qty.service.SortService;
import com.qty.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author qty
 * date 2020-01-30
 */
@Api(value = "分类",tags = "分类")
@RestController
@RequestMapping("/sort")
@Slf4j
public class SortController {

    @Autowired
    private SortService sortService;

    //新增分类
    @ApiOperation(value="新增分类",notes="新增分类")
    @PostMapping(value = "/manage/add")
    public BaseResponse add(@RequestBody Sort sort){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        //新增前判断是否重复分类名
        QueryWrapper<Sort>wrapper=new QueryWrapper<>();
        wrapper.eq("sort_name",sort.getSortName());
        int count=sortService.count(wrapper);
        if (count>0){
            return new BaseResponse(StatusCode.RepeatAdd);
        }
        boolean isSuccess=sortService.save(sort);
        if (!isSuccess){
            return new BaseResponse(StatusCode.Fail);
        }
        return response;
    }

    //删除分类
    @ApiOperation(value="删除分类",notes="删除分类")
    @GetMapping(value = "/manage/delete")
    public BaseResponse delete(@RequestParam Long id){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        boolean isSuccess=sortService.removeById(id);
        if (!isSuccess){
            return new BaseResponse(StatusCode.Fail);
        }
        return response;
    }

    //根据ID查询分类
    @ApiOperation(value="根据ID查询分类",notes="根据ID查询分类")
//    @RequiresPermissions("sort:manage:getone")
    @GetMapping(value = "/manage/getOne")
    public BaseResponse<Sort> getById(@RequestParam Long id){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Sort sort=sortService.getById(id);
        if (null==sort){
            return new BaseResponse(StatusCode.Fail);
        }
        response.setData(sort);
        return response;
    }

    //编辑分类
    @ApiOperation(value="更新分类",notes="更新分类")
    @PostMapping(value = "/manage/update")
    public BaseResponse update(@RequestBody Sort sort){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        UpdateWrapper<Sort>wrapper=new UpdateWrapper<>();
        wrapper.eq("sort_id",sort.getSortId());
        boolean isSuccess=sortService.update(sort,wrapper);
        if (!isSuccess){
            return new BaseResponse(StatusCode.Fail);
        }
        return response;
    }


    //分页展示分类（分类不分级别）
    @ApiOperation(value="分页展示分类",notes="分页展示分类")
//    @RequiresPermissions("sort:manage:list")
//    @RequiresRoles(value={"admin"})
    @PostMapping(value = "/manage/list")
    public BaseResponse list(@RequestBody Map<String,Object> params){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        PageUtil page=null;
        try {
            page=sortService.queryPage(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseResponse(StatusCode.Fail);
        }
        response.setData(page);
        return response;
    }
}
