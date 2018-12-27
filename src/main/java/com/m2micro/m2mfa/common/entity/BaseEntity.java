package com.m2micro.m2mfa.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * @Auther: liaotao
 * @Date: 2018/11/21 17:20
 * @Description:
 */
@ApiModel(value="BaseEntity对象")
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @ApiModelProperty(value = "创建日期",example = "2018-11-21 12:00:00")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createOn;

    @ApiModelProperty(value = "创建用户主键")
    @CreatedBy
    @Column(updatable = false)
    private String createBy;

    @ApiModelProperty(value = "修改日期",example = "2018-11-21 12:00:00")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @LastModifiedDate
    @Column(nullable = false)
    private Date modifiedOn;

    @ApiModelProperty(value = "修改用户主键")
    @LastModifiedBy
    private String modifiedBy;

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
