package com.qty.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author qty
 * date 2020-01-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName(value = "tb_sorts")
public class Sort implements Serializable {

    @ApiModelProperty("分类主键")
    @TableId(value = "sort_id",type = IdType.AUTO)
    private Long sortId;

    @ApiModelProperty("分类名")
    private String sortName;

    @ApiModelProperty("分类别名")
    private String sortAlias;

    @ApiModelProperty("分类描述")
    private String sortDescription;

    @ApiModelProperty("分类父ID")
    private Long parentSortId;

}
