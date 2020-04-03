package com.qty.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class QueryUtil<T> {

    /**
     * 根据前端传递过来的参数进行转换 - 过滤
     * @param params
     * @return
     */
    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, false);
    }

    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(params.get(ConstantParameter.PAGE) != null){
            curPage = Long.parseLong((String)params.get(ConstantParameter.PAGE));
        }
        if(params.get(ConstantParameter.LIMIT) != null){
            limit = Long.parseLong((String)params.get(ConstantParameter.LIMIT));
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        params.put(ConstantParameter.PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject((String)params.get(ConstantParameter.ORDER_FIELD));
        String order = (String)params.get(ConstantParameter.ORDER);

        //前端字段排序
        if(StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)){
            if(ConstantParameter.ASC.equalsIgnoreCase(order)) {
                return page.setAsc(orderField);
            }else {
                return page.setDesc(orderField);
            }
        }

        //默认排序
        if(isAsc) {
            page.setAsc(defaultOrderField);
        }else {
            page.setDesc(defaultOrderField);
        }

        return page;
    }

    //重载的查询
    public IPage<T> getQueryPage(Map<String, Object> params) {
        //当前第几页、每页显示的条目
        long curPage = ConstantParameter.CURPAGE;
        long limit = ConstantParameter.LIMIT_SIZE;

        if (params.get(ConstantParameter.PAGE)!=null){
            curPage=Long.valueOf(params.get(ConstantParameter.PAGE).toString());
        }
        if (params.get(ConstantParameter.LIMIT)!=null){
            limit=Long.valueOf(params.get(ConstantParameter.LIMIT).toString());
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //前端请求的字段排序
        if(params.get(ConstantParameter.ORDER)!=null && params.get(ConstantParameter.ORDER_FIELD)!=null){

            SQLFilter.sqlInject((String) params.get(ConstantParameter.ORDER_FIELD));


            if(ConstantParameter.ASC.equalsIgnoreCase(params.get(ConstantParameter.ORDER).toString())) {
                return page.setAsc(params.get(ConstantParameter.ORDER_FIELD).toString());
            }else {
                return page.setDesc(params.get(ConstantParameter.ORDER_FIELD).toString());
            }
        }

        return page;
    }
}




























