package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 条形码子序列字段定义
 * @author wanglei
 * @since 2018-12-29
 */
@Entity
@ApiModel(value="BaseBarcodeRuleDef对象", description="条形码子序列字段定义")
public class BaseBarcodeRuleDef extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @NotEmpty(message="变量名不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "变量名")
    private String name;
    @ApiModelProperty(value = "类型")
    private String category;
    @ApiModelProperty(value = "位置")
    private Integer position;
    @ApiModelProperty(value = "默认值")
    private String defaults;
    @ApiModelProperty(value = "进制")
    private Integer ary;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "长度")
    private Integer length;
    @ApiModelProperty(value = "最大值")
    private String maxNo;
    @ApiModelProperty(value = "分组")
    private String groupBy;
    @ApiModelProperty(value = "排序码")
    private Integer sortCode;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "条形码规则主键")
    private String barcodeId;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getPosition() {
        return position;
    }
    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getDefaults() {
        return defaults;
    }
    public void setDefaults(String defaults) {
        this.defaults = defaults;
    }

    public Integer getAry() {
        return ary;
    }
    public void setAry(Integer ary) {
        this.ary = ary;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLength() {
        return length;
    }
    public void setLength(Integer length) {
        this.length = length;
    }

    public String getMaxNo() {
        return maxNo;
    }
    public void setMaxNo(String maxNo) {
        this.maxNo = maxNo;
    }

    public String getGroupBy() {
        return groupBy;
    }
    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public Integer getSortCode() {
        return sortCode;
    }
    public void setSortCode(Integer sortCode) {
        this.sortCode = sortCode;
    }

    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcodeId() {
        return barcodeId;
    }
    public void setBarcodeId(String barcodeId) {
        this.barcodeId = barcodeId;
    }



}