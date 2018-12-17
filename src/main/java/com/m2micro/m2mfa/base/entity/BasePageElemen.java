package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 页面元素
 * @author chenshuhong
 * @since 2018-12-14
 */
@Entity
@ApiModel(value="BasePageElemen对象", description="页面元素")
public class BasePageElemen extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id")
    @Id
    private String elemenId;
    @ApiModelProperty(value = "页面元素")
    private String elemen;

    public String getElemenId() {
        return elemenId;
    }
    public void setElemenId(String elemenId) {
        this.elemenId = elemenId;
    }

    public String getElemen() {
        return elemen;
    }
    public void setElemen(String elemen) {
        this.elemen = elemen;
    }



}