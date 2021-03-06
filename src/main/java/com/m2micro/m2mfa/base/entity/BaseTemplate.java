package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 标签模板
 * @author liaotao
 * @since 2019-01-22
 */
@Data
@Entity
@ApiModel(value="BaseTemplate对象", description="标签模板")
public class BaseTemplate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @ApiModelProperty(value = "主键")
    private String id;
    @Size(max=50,message = "编号字节不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "编号")
    private String number;
    @ApiModelProperty(value = "名称")
    private String name;
    @Size(max=50,message = "标签类型字节不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="标签类型不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "标签类型")
    private String category;
    @Transient
    @ApiModelProperty(value = "标签类型名称")
    private String categoryName;
    @ApiModelProperty(value = "版本")
    private Integer version;
    @ApiModelProperty(value = "标签图片")
    private String imageUrl;
    @Size(max=50,message = "标签模板字节不能大于200位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="标签模板不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "标签模板")
    private String labelFileUrl;
    @ApiModelProperty(value = "排序码")
    private Integer sortCode;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;
    @Transient
    @ApiModelProperty(value = "模板变量")
    private List<BaseTemplateVar> baseTemplateVars;

}
