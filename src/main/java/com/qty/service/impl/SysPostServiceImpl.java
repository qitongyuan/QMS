package com.qty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qty.entity.SysPost;
import com.qty.mapper.SysPostMapper;
import com.qty.response.StatusCode;
import com.qty.service.SysPostService;
import com.qty.util.PageUtil;
import com.qty.util.QueryUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    /**
     * 分页模糊查询
     * @param params
     * @return
     */
    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String search= (params.get("search") == null)? "": params.get("search").toString();
        //封装分页查询工具
        IPage<SysPost>queryPage= new QueryUtil<SysPost>().getQueryPage(params);
        QueryWrapper wrapper=new QueryWrapper<SysPost>()
                .like(StringUtils.isNotBlank(search),"post_code",search.trim())
                .or(StringUtils.isNotBlank(search))
                .like(StringUtils.isNotBlank(search),"post_name",search.trim());
        IPage<SysPost>resPage=this.page(queryPage,wrapper);
        return new PageUtil(resPage);
    }

    @Override
    public void savePost(SysPost entity) {
        if (this.getOne(new QueryWrapper<SysPost>().eq("post_code",entity.getPostCode()))!=null){
            throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
        }
        entity.setCreateTime(DateTime.now().toDate());
        save(entity);
    }

    @Override
    public void updatePost(SysPost entity) {
        SysPost old=this.getById(entity.getPostId());
        if (old!=null && !old.getPostCode().equals(entity.getPostCode())){
            if (this.getOne(new QueryWrapper<SysPost>().eq("post_code",entity.getPostCode()))!=null){
                throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
            }
        }
        entity.setUpdateTime(DateTime.now().toDate());
        updateById(entity);
    }
}
