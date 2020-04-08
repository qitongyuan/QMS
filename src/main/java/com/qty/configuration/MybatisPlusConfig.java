package com.qty.configuration;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.qty.shiro.ShiroUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qty
 * date 2020-01-30
 */
@Configuration
public class MybatisPlusConfig {

    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor interceptor=new PaginationInterceptor();
        List<ISqlParser>sqlParserList=new ArrayList<>();
        TenantSqlParser tenantSqlParser=new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                //从token中获取
                String tenantId=ShiroUtil.getUserInfo().getTenantId();
                return new StringValue(tenantId);
            }

            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                //部分表过滤掉，不加tenantId
                List<String> tableNameList = Arrays.asList("tb_users","tb_role","tb_user_role");
                if (tableNameList.contains(tableName)){
                    return true;
                }
                return false;
            }
        });
        sqlParserList.add(tenantSqlParser);
        interceptor.setSqlParserList(sqlParserList);
        return interceptor;
    }

    //防sql注入攻击
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }
}
