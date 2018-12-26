package com.m2micro.m2mfa.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel(value="BaseStaffDetailObj", description="职员详情")
public class BaseStaffDetailObj {
    @ApiModelProperty(value = "工号")
   private String code;
    @ApiModelProperty(value = "姓名")
    private String  name;


    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "部门")

    private String department;

    @ApiModelProperty(value = "岗位")
    private String duty;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "电话号码")
    private String telephone;

    @ApiModelProperty(value = "离职")
    private Boolean isDimission;

    @ApiModelProperty(value = "有效")
    private Boolean enabled;

 public String getCode() {
  return code;
 }

 public void setCode(String code) {
  this.code = code;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getGender() {
  return gender;
 }

 public void setGender(String gender) {
  this.gender = gender;
 }

 public String getDepartment() {
  return department;
 }

 public void setDepartment(String department) {
  this.department = department;
 }

 public String getDuty() {
  return duty;
 }

 public void setDuty(String duty) {
  this.duty = duty;
 }

 public String getMobile() {
  return mobile;
 }

 public void setMobile(String mobile) {
  this.mobile = mobile;
 }

 public String getTelephone() {
  return telephone;
 }

 public void setTelephone(String telephone) {
  this.telephone = telephone;
 }

 public Boolean getDimission() {
  return isDimission;
 }

 public void setDimission(Boolean dimission) {
  isDimission = dimission;
 }

 public Boolean getEnabled() {
  return enabled;
 }

 public void setEnabled(Boolean enabled) {
  this.enabled = enabled;
 }
}
