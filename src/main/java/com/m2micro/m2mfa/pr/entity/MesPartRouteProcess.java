package com.m2micro.m2mfa.pr.entity;


import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 料件途程设定工序
 * @author liaotao
 * @since 2018-12-19
 */
@Entity
@ApiModel(value="MesPartRouteProcess对象", description="料件途程设定工序")
public class MesPartRouteProcess extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "关联主键")
    private String partrouteid;
    @ApiModelProperty(value = "步骤")
    @NotNull(message="步骤不能为空。",groups = {AddGroup.class, UpdateGroup.class})
    private Integer setp;
    @ApiModelProperty(value = "工序")
    private String processid;
    @ApiModelProperty(value = "数据采集方式")
    @Size(max=32,message = "数据采集方式长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String collection;
    @ApiModelProperty(value = "下一工序")
    private String nextprocessid;
    @ApiModelProperty(value = "不良工序")
    private String failprocessid;
    @ApiModelProperty(value = "允许跳过")
    private Integer jump;
    @ApiModelProperty(value = "包装配置档")
    private String packid;
    @ApiModelProperty(value = "检验配置档")
    private String qualitysolutionid;
    @ApiModelProperty(value = "排序码")
    private Integer sortcode;
    @ApiModelProperty(value = "有效否")
    private Integer enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    @Transient
    @ApiModelProperty(value = "不良工序名称")
    private String failprocessName;

    @Transient
    @ApiModelProperty(value = "作业工序名称")
    private String processidName;

    @Transient
    @ApiModelProperty(value = "包装配合名称")
    private String packName;

    @Transient
    @ApiModelProperty(value = "检验方案名称")
    private String qualitysolutionName;

    public MesPartRouteProcess(String id, String partrouteid, @NotNull(message = "步骤不能为空。", groups = {AddGroup.class, UpdateGroup.class}) Integer setp, String processid, @Size(max = 32, message = "数据采集方式长度不能大于32位", groups = {AddGroup.class, UpdateGroup.class}) String collection, String nextprocessid, String failprocessid, Integer jump, String packid, String qualitysolutionid, Integer sortcode, Integer enabled, String description, String failprocessName, String processidName, String packName, String qualitysolutionName) {
        this.id = id;
        this.partrouteid = partrouteid;
        this.setp = setp;
        this.processid = processid;
        this.collection = collection;
        this.nextprocessid = nextprocessid;
        this.failprocessid = failprocessid;
        this.jump = jump;
        this.packid = packid;
        this.qualitysolutionid = qualitysolutionid;
        this.sortcode = sortcode;
        this.enabled = enabled;
        this.description = description;
        this.failprocessName = failprocessName;
        this.processidName = processidName;
        this.packName = packName;
        this.qualitysolutionName = qualitysolutionName;
    }

    public MesPartRouteProcess() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartrouteid() {
        return partrouteid;
    }

    public void setPartrouteid(String partrouteid) {
        this.partrouteid = partrouteid;
    }

    public Integer getSetp() {
        return setp;
    }

    public void setSetp(Integer setp) {
        this.setp = setp;
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getNextprocessid() {
        return nextprocessid;
    }

    public void setNextprocessid(String nextprocessid) {
        this.nextprocessid = nextprocessid;
    }

    public String getFailprocessid() {
        return failprocessid;
    }

    public void setFailprocessid(String failprocessid) {
        this.failprocessid = failprocessid;
    }

    public Integer getJump() {
        return jump;
    }

    public void setJump(Integer jump) {
        this.jump = jump;
    }

    public String getPackid() {
        return packid;
    }

    public void setPackid(String packid) {
        this.packid = packid;
    }

    public String getQualitysolutionid() {
        return qualitysolutionid;
    }

    public void setQualitysolutionid(String qualitysolutionid) {
        this.qualitysolutionid = qualitysolutionid;
    }

    public Integer getSortcode() {
        return sortcode;
    }

    public void setSortcode(Integer sortcode) {
        this.sortcode = sortcode;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFailprocessName() {
        return failprocessName;
    }

    public void setFailprocessName(String failprocessName) {
        this.failprocessName = failprocessName;
    }

    public String getProcessidName() {
        return processidName;
    }

    public void setProcessidName(String processidName) {
        this.processidName = processidName;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getQualitysolutionName() {
        return qualitysolutionName;
    }

    public void setQualitysolutionName(String qualitysolutionName) {
        this.qualitysolutionName = qualitysolutionName;
    }
}