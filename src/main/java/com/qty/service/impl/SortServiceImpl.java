package com.qty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qty.entity.Sort;
import com.qty.mapper.SortMapper;
import com.qty.service.SortService;
import com.qty.util.ConstantParameter;
import com.qty.util.PageUtil;
import com.qty.util.QueryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author qty
 * date 2020-01-30
 */
@Service
@Slf4j
public class SortServiceImpl extends ServiceImpl<SortMapper, Sort> implements SortService {

    @Resource
    private SortMapper sortMapper;

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        //取出检索条件
        String search = (params.get(ConstantParameter.SEARCH) == null) ? "" : params.get(ConstantParameter.SEARCH).toString();
        Long parentSortId = (params.get(ConstantParameter.PARENTSORTID) == null) ? 0L : Long.valueOf(params.get(ConstantParameter.PARENTSORTID).toString());
        //取出分页信息用于包装
        IPage<Sort> queryPage = new QueryUtil<Sort>().getQueryPage(params);

        //组织搜索条件
        QueryWrapper<Sort> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(search), "sort_name", search.trim());
        wrapper.eq("parent_sort_id", parentSortId);
        return new PageUtil(this.page(queryPage, wrapper));
    }


}
