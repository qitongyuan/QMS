package com.qty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qty.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

    List<SysDept> queryListIn(Map<String, Object> params);

    Set<Long> queryAllDeptIds();

    Set<Long>queryDeptIdsByUserId(Long userId);

    //根据父级部门id查询子部门id列表
    List<Long> queryDeptIds(Long parentId);

    //获取部门列表（具有数据权限判断）
    List<SysDept>selectDeptList(SysDept dept);

    //根据角色ID获取对应选中的部门ID
    List<Long>selectRoleDeptById(Long roleId);
}
