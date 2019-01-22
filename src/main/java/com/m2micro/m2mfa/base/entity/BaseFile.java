package com.m2micro.m2mfa.base.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Entity
@ApiModel(value="上传文件对象", description="上传文件对象")
public class BaseFile extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    @NotEmpty(message="上传文件对象不能为空",groups = {UpdateGroup.class})
    private String id;
    @ApiModelProperty(value = "名字")
    @NotEmpty(message="名字不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String fileName;
    @ApiModelProperty(value = "文件存储路径")
   @NotEmpty(message="文件存储路径不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String filePath;
    //创建时间
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}