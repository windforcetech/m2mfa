package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 班别基本资料
 * @author liaotao
 * @since 2018-11-26
 */
@Entity
@ApiModel(value="BaseShift对象", description="班别基本资料")
public class BaseShift extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    @NotEmpty(message="主键不能为空",groups = {UpdateGroup.class})
    private String shiftId;
    @ApiModelProperty(value = "编号")
    @Size(max=32,message = "编号字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String code;
    @ApiModelProperty(value = "名称")
    @Size(max=32,message = "名称字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    @ApiModelProperty(value = "类型")
    @Size(max=32,message = "类型字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="类型不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String category;
    @Transient
    @ApiModelProperty(value = "类型名称")
    @Size(max=32,message = "类型名称字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String categoryName;
    @ApiModelProperty(value = "上班1")
    @Pattern(regexp="([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]",message="日期格式不正确",groups = {AddGroup.class, UpdateGroup.class})
    private String onTime1;
    @ApiModelProperty(value = "下班1")
    @Pattern(regexp="([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]",message="日期格式不正确",groups = {AddGroup.class, UpdateGroup.class})
    private String offTime1;
    @ApiModelProperty(value = "累计休息1")
    private Integer restTime1;
    @ApiModelProperty(value = "时间类型")
    private String timeCategory1;
    @ApiModelProperty(value = "上班2")
    @Pattern(regexp="([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]",message="日期格式不正确",groups = {AddGroup.class, UpdateGroup.class})
    private String onTime2;
    @ApiModelProperty(value = "下班2")
    @Pattern(regexp="([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]",message="日期格式不正确",groups = {AddGroup.class, UpdateGroup.class})
    private String offTime2;
    @ApiModelProperty(value = "累计休息2")
    private Integer restTime2;
    @ApiModelProperty(value = "时间类型2")
    private String timeCategory2;
    @ApiModelProperty(value = "上班3")
    @Pattern(regexp="([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]",message="日期格式不正确",groups = {AddGroup.class, UpdateGroup.class})
    private String onTime3;
    @ApiModelProperty(value = "下班3")
    @Pattern(regexp="([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]",message="日期格式不正确",groups = {AddGroup.class, UpdateGroup.class})
    private String offTime3;
    @ApiModelProperty(value = "累计休息3")
    private Integer restTime3;
    @ApiModelProperty(value = "时间类型3")
    private String timeCategory3;
    @ApiModelProperty(value = "上班4")
    @Pattern(regexp="([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]",message="日期格式不正确",groups = {AddGroup.class, UpdateGroup.class})
    private String onTime4;
    @ApiModelProperty(value = "下班4")
    @Pattern(regexp="([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]",message="日期格式不正确",groups = {AddGroup.class, UpdateGroup.class})
    private String offTime4;
    @ApiModelProperty(value = "累计休息4")
    private Integer restTime4;
    @ApiModelProperty(value = "时间类型4")
    private String timeCategory4;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    @Size(max=32,message = "描述字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String description;


    @Transient
    @ApiModelProperty(value = "职员")
    private List<BaseStaff> staffs;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getOnTime1() {
        return onTime1;
    }

    public void setOnTime1(String onTime1) {
        this.onTime1 = onTime1;
    }

    public String getOffTime1() {
        return offTime1;
    }

    public void setOffTime1(String offTime1) {
        this.offTime1 = offTime1;
    }

    public Integer getRestTime1() {
        return restTime1;
    }

    public void setRestTime1(Integer restTime1) {
        this.restTime1 = restTime1;
    }

    public String getTimeCategory1() {
        return timeCategory1;
    }

    public void setTimeCategory1(String timeCategory1) {
        this.timeCategory1 = timeCategory1;
    }

    public String getOnTime2() {
        return onTime2;
    }

    public void setOnTime2(String onTime2) {
        this.onTime2 = onTime2;
    }

    public String getOffTime2() {
        return offTime2;
    }

    public void setOffTime2(String offTime2) {
        this.offTime2 = offTime2;
    }

    public Integer getRestTime2() {
        return restTime2;
    }

    public void setRestTime2(Integer restTime2) {
        this.restTime2 = restTime2;
    }

    public String getTimeCategory2() {
        return timeCategory2;
    }

    public void setTimeCategory2(String timeCategory2) {
        this.timeCategory2 = timeCategory2;
    }

    public String getOnTime3() {
        return onTime3;
    }

    public void setOnTime3(String onTime3) {
        this.onTime3 = onTime3;
    }

    public String getOffTime3() {
        return offTime3;
    }

    public void setOffTime3(String offTime3) {
        this.offTime3 = offTime3;
    }

    public Integer getRestTime3() {
        return restTime3;
    }

    public void setRestTime3(Integer restTime3) {
        this.restTime3 = restTime3;
    }

    public String getTimeCategory3() {
        return timeCategory3;
    }

    public void setTimeCategory3(String timeCategory3) {
        this.timeCategory3 = timeCategory3;
    }

    public String getOnTime4() {
        return onTime4;
    }

    public void setOnTime4(String onTime4) {
        this.onTime4 = onTime4;
    }

    public String getOffTime4() {
        return offTime4;
    }

    public void setOffTime4(String offTime4) {
        this.offTime4 = offTime4;
    }

    public Integer getRestTime4() {
        return restTime4;
    }

    public void setRestTime4(Integer restTime4) {
        this.restTime4 = restTime4;
    }

    public String getTimeCategory4() {
        return timeCategory4;
    }

    public void setTimeCategory4(String timeCategory4) {
        this.timeCategory4 = timeCategory4;
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

    public List<BaseStaff> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<BaseStaff> staffs) {
        this.staffs = staffs;
    }
}