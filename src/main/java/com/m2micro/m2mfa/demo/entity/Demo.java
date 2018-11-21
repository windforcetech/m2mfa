package com.m2micro.m2mfa.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Auther: liaotao
 * @Date: 2018/11/19 10:02
 * @Description:
 */
@Entity
@ApiModel(description = "demo信息")
public class Demo implements Serializable {

    @Id
    @GeneratedValue
    @ApiModelProperty("主键ID")
    private Long id;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("电话号码")
    private String tel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
