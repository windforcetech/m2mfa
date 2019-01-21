package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 员工（职员）表
 * @author liaotao
 * @since 2018-11-26
 */
@Entity
@ApiModel(value="BaseStaff对象", description="员工（职员）表")
public class BaseStaff extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "员工主键")
    @Id
    @NotEmpty(message="员工主键不能为空",groups = {UpdateGroup.class})
    private String staffId;
    @ApiModelProperty(value = "名字")
    @Size(max=50,message = "名字字节不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="名字不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String staffName;
    @ApiModelProperty(value = "工号")
    @Size(max=50,message = "工号字节不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="工号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String code;
    @Size(max=20,message = "性别字节不能大于20位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "性别")
    private String gender;
    @Size(max=50,message = "相片字节不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "相片")
    private String photoUrl;
    @ApiModelProperty(value = "公司主键")
    //@NotEmpty(message="公司主键不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String companyId;
    @NotEmpty(message="基础织架构岗位主键不能为空",groups = {AddGroup.class, UpdateGroup.class})
    //@NotEmpty(message="部门主键不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "基础组织架构岗位主键")
    private String departmentId;

    @ApiModelProperty(value = "mes职能主键")
    private String dutyId;
    @Size(max=50,message = "身份证号码字节不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "身份证号码")
    private String idCard;
    @Size(max=50,message = "电子邮箱字节不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "电子邮箱")
    private String email;
    @Size(max=20,message = "手机字节不能大于20位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "手机")
    private String mobile;
    @Size(max=20,message = "电话号码字节不能大于20位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "电话号码")
    private String telephone;
    @ApiModelProperty(value = "离职")
    private Boolean isDimission;
    @ApiModelProperty(value = "离职日期")
    private Date dimissionDate;
    @Size(max=50,message = "离职原因字节不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "离职原因")
    private String dimissionCause;
    @ApiModelProperty(value = "有效")
    private Boolean enabled;
    @ApiModelProperty(value = "删除标识")
    private Boolean deletionStateCode;

    @Transient
    @ApiModelProperty(value = "所属工位ID")
    private String stationId;

    @Transient
    @ApiModelProperty(value = "工位名称")
    private String  stationName;

    @Size(max=15,message = "卡号长度不能大于15位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "卡号")
    private String icCard;

    public String getIcCard() {
        return icCard;
    }

    public void setIcCard(String icCard) {
        this.icCard = icCard;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Size(max=32,message = "描述字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "用户主键")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @ApiModelProperty(value = "是否自动创建关联账户")
    private  Boolean needAddUser;

    public Boolean getNeedAddUser() {
        return needAddUser;
    }

    public void setNeedAddUser(Boolean needAddUser) {
        this.needAddUser = needAddUser;
    }

    public String getStaffId() {
        return staffId;
    }
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDutyId() {
        return dutyId;
    }
    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public String getIdCard() {
        return idCard;
    }
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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
    public void setDimission(Boolean isDimission) {
        this.isDimission = isDimission;
    }

    public Date getDimissionDate() {
        return dimissionDate;
    }
    public void setDimissionDate(Date dimissionDate) {
        this.dimissionDate = dimissionDate;
    }

    public String getDimissionCause() {
        return dimissionCause;
    }
    public void setDimissionCause(String dimissionCause) {
        this.dimissionCause = dimissionCause;
    }

    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getDeletionStateCode() {
        return deletionStateCode;
    }
    public void setDeletionStateCode(Boolean deletionStateCode) {
        this.deletionStateCode = deletionStateCode;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }



}