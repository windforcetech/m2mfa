package com.m2micro.m2mfa.display.entity;


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
 * 显示列
 * @author liaotao
 * @since 2019-04-25
 */
@Entity
@ApiModel(value="DisplayColumn对象", description="显示列")
public class DisplayColumn extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "显示列主键")
    @Id
    @NotEmpty(message="显示列主键不能为空",groups = {UpdateGroup.class})
    private String columnId;
    @ApiModelProperty(value = "用户主键")
    @NotEmpty(message="用户主键不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String userId;
    @ApiModelProperty(value = "模块主键")
    @NotEmpty(message="模块主键不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String moduleId;
    @ApiModelProperty(value = "显示列内容")
    private String content;

    public String getColumnId() {
        return columnId;
    }
    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getModuleId() {
        return moduleId;
    }
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }



}