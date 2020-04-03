package com.qty.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.qty.annotation.DataScope;
import com.qty.entity.SysDept;
import com.qty.entity.SysUser;
import com.qty.mapper.SysDeptMapper;
import com.qty.service.SysDeptService;
import com.qty.shiro.ShiroUtil;
import com.qty.util.CommonUtil;
import com.qty.util.ConstantParameter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    /**
     * 查询子部门id列表
     * @param deptId
     * @return
     */
    @Override
    public List<Long> getSubDeptIdList(Long deptId) {
        List<Long> deptIdList= Lists.newLinkedList();
        //第一级部门Id列表
        List<Long> subIdList=sysDeptMapper.queryDeptIds(deptId);
        getDeptTreeList(subIdList,deptIdList);
        return deptIdList;
    }

    /**
     * 递归
     * @param subIdList 第一级部门数据Id列表
     * @param deptIdList 每次递归时循环存储的结果数据Id列表
     */
    private void getDeptTreeList(List<Long> subIdList,List<Long> deptIdList){
        List<Long> list;
        for (Long subId:subIdList){
            list=sysDeptMapper.queryDeptIds(subId);
            if (list!=null && !list.isEmpty()){
                //调用递归之处
                getDeptTreeList(list,deptIdList);
            }

            //执行到这里时，就表示当前递归结束
            deptIdList.add(subId);
        }
    }

    @DataScope(deptAlias = "td")
    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        return sysDeptMapper.selectDeptList(dept);
    }

    @Override
    public List<Long> roleDeptData(Long roleId) {
        return sysDeptMapper.selectRoleDeptById(roleId);
    }
}
