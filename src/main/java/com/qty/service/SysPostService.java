package com.qty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qty.entity.SysPost;
import com.qty.util.PageUtil;

import java.util.Map;

public interface SysPostService extends IService<SysPost> {

    PageUtil queryPage(Map<String,Object> params);

    void savePost(SysPost entity);

    void updatePost(SysPost entity);
}
