package com.qty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qty.entity.Sort;
import com.qty.util.PageUtil;


import java.util.List;
import java.util.Map;

/**
 * @author qty
 * date 2020-01-30
 */
public interface SortService extends IService<Sort> {

    //使用mp作分页
    PageUtil queryPage(Map<String, Object> params);

}
