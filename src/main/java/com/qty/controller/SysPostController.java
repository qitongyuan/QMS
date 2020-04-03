package com.qty.controller;

import com.google.common.collect.Maps;
import com.qty.entity.SysPost;
import com.qty.response.BaseResponse;
import com.qty.response.StatusCode;
import com.qty.service.SysPostService;
import com.qty.util.PageUtil;
import com.qty.util.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@Api(value = "岗位",tags = "岗位")
@RestController
@RequestMapping("/sys/post")
@AllArgsConstructor
@Slf4j
public class SysPostController {

    private SysPostService sysPostService;

    @ApiOperation(value="分页展示岗位信息",notes="分页展示岗位信息")
    @PostMapping("/list")
    public BaseResponse list(@RequestBody Map<String,Object>params){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        PageUtil page=null;
        try {
            page=sysPostService.queryPage(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseResponse(StatusCode.Fail);
        }
        response.setData(page);
        return response;
    }

    @ApiOperation(value = "新增岗位")
    @PostMapping(value = "/save")
    public BaseResponse save(@RequestBody @Validated SysPost entity, BindingResult result){
        String res= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try{
            sysPostService.savePost(entity);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //详情
    @ApiOperation(value = "岗位详情")
    @GetMapping(value = "/info/{id}")
    public BaseResponse info(@PathVariable Long id){
        if (id==null || id<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap= Maps.newHashMap();
        try {
            resMap.put("post",sysPostService.getById(id));

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }

    //修改
    @ApiOperation(value = "岗位更新")
    @RequestMapping(value = "/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse update(@RequestBody @Validated SysPost entity, BindingResult result){
        String res= ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(res)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),res);
        }
        if (entity.getPostId()==null || entity.getPostId()<=0){
            return new BaseResponse(StatusCode.InvalidParams);
        }

        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            sysPostService.updatePost(entity);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "岗位删除")
    @RequestMapping(value = "/delete",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse delete(@RequestBody Long[] ids){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            sysPostService.removeByIds(Arrays.asList(ids));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }

        return response;
    }
    
}
