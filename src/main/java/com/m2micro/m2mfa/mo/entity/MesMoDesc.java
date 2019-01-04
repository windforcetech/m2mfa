package com.m2micro.m2mfa.mo.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 工单主档
 * @author liaotao
 * @since 2018-12-10
 */
@Entity
@ApiModel(value="MesMoDesc对象", description="工单主档")
public class MesMoDesc extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "工单id")
    @Id
    @NotEmpty(message="主键不能为空",groups = {UpdateGroup.class})
    private String moId;
    @Size(max=32,message = "工单号码不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "工单号码")
    @NotEmpty(message="工单号码不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String moNumber;
    @ApiModelProperty(value = "工单类型")
    @NotEmpty(message="工单类型不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String category;
    @NotEmpty(message="料件id不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "料件id")
    private String partId;
    @Transient
    @ApiModelProperty(value = "料件品名")
    private String partName;
    @Transient
    @ApiModelProperty(value = "料件编号")
    private String partNo;
    @ApiModelProperty(value = "目标量")
    @NotNull(message="目标量不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private Integer targetQty;
    @Max(value = 32,message = "工单版本不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "工单版本")
    private Integer revsion;
    @ApiModelProperty(value = "特性码")
    private String distinguish;
    @ApiModelProperty(value = "母工单")
    private String parentMo;
    @ApiModelProperty(value = "bom版本")
    private Integer bomRevsion;
    @ApiModelProperty(value = "预计投入时间")
    private Date planInputDate;
    @ApiModelProperty(value = "预计完工时间")
    private Date planCloseDate;
    @ApiModelProperty(value = "实际投入时间")
    private Date actualInputDate;
    @ApiModelProperty(value = "实际完成时间")
    private Date actualcLoseDate;
    @ApiModelProperty(value = "生产途程")
    private String routeId;
    @ApiModelProperty(value = "投入工序")
    private String inputProcessId;
    @ApiModelProperty(value = "产出工序")
    private String outputProcessId;
    @ApiModelProperty(value = "达交时间")
    private Date reachDate;
    @ApiModelProperty(value = "预排机台数")
    private Integer machineQty;
    @ApiModelProperty(value = "客户主键")
    private String customerId;
    @ApiModelProperty(value = "订单号码")
    private String orderId;
    @ApiModelProperty(value = "订单项次")
    private Integer orderSeq;
    @ApiModelProperty(value = "完成排产")
    private Integer isSchedul;
    @ApiModelProperty(value = "已排产数量")
    private Integer schedulQty;
    @ApiModelProperty(value = "已投入数量")
    private Integer inputQty;
    @ApiModelProperty(value = "已产出数量")
    private Integer outputQty;

    @Transient
    @ApiModelProperty(value = "未排量")
    private Integer notQty;

    @ApiModelProperty(value = "报废数量")
    private Integer scrappedQty;
    @ApiModelProperty(value = "不良数量")
    private Integer failQty;
    @ApiModelProperty(value = "状态")
    private Integer closeFlag;
    @ApiModelProperty(value = "冻结前状态")
    private Integer prefreezingState;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @Size(max=32,message = "描述信息不能大于200位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "描述")
    private String description;

    public MesMoDesc(@NotEmpty(message = "主键不能为空", groups = {UpdateGroup.class}) String moId, @Size(max = 32, message = "工单号码不能大于32位", groups = {AddGroup.class, UpdateGroup.class}) @NotEmpty(message = "工单号码不能为空", groups = {AddGroup.class, UpdateGroup.class}) String moNumber, @NotEmpty(message = "工单类型不能为空", groups = {AddGroup.class, UpdateGroup.class}) String category, @NotEmpty(message = "料件id不能为空", groups = {AddGroup.class, UpdateGroup.class}) String partId, String partName, String partNo, @NotNull(message = "目标量不能为空", groups = {AddGroup.class, UpdateGroup.class}) Integer targetQty, @Max(value = 32, message = "工单版本不能大于32位", groups = {AddGroup.class, UpdateGroup.class}) Integer revsion, String distinguish, String parentMo, Integer bomRevsion, Date planInputDate, Date planCloseDate, Date actualInputDate, Date actualcLoseDate, String routeId, String inputProcessId, String outputProcessId, Date reachDate, Integer machineQty, String customerId, String orderId, Integer orderSeq, Integer isSchedul, Integer schedulQty, Integer inputQty, Integer outputQty, Integer notQty, Integer scrappedQty, Integer failQty, Integer closeFlag, Integer prefreezingState, Boolean enabled, @Size(max = 32, message = "描述信息不能大于200位", groups = {AddGroup.class, UpdateGroup.class}) String description) {
        this.moId = moId;
        this.moNumber = moNumber;
        this.category = category;
        this.partId = partId;
        this.partName = partName;
        this.partNo = partNo;
        this.targetQty = targetQty;
        this.revsion = revsion;
        this.distinguish = distinguish;
        this.parentMo = parentMo;
        this.bomRevsion = bomRevsion;
        this.planInputDate = planInputDate;
        this.planCloseDate = planCloseDate;
        this.actualInputDate = actualInputDate;
        this.actualcLoseDate = actualcLoseDate;
        this.routeId = routeId;
        this.inputProcessId = inputProcessId;
        this.outputProcessId = outputProcessId;
        this.reachDate = reachDate;
        this.machineQty = machineQty;
        this.customerId = customerId;
        this.orderId = orderId;
        this.orderSeq = orderSeq;
        this.isSchedul = isSchedul;
        this.schedulQty = schedulQty;
        this.inputQty = inputQty;
        this.outputQty = outputQty;
        this.notQty = notQty;
        this.scrappedQty = scrappedQty;
        this.failQty = failQty;
        this.closeFlag = closeFlag;
        this.prefreezingState = prefreezingState;
        this.enabled = enabled;
        this.description = description;
    }

    public MesMoDesc() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMoId() {
        return moId;
    }

    public void setMoId(String moId) {
        this.moId = moId;
    }

    public String getMoNumber() {
        return moNumber;
    }

    public void setMoNumber(String moNumber) {
        this.moNumber = moNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public Integer getTargetQty() {
        return targetQty;
    }

    public void setTargetQty(Integer targetQty) {
        this.targetQty = targetQty;
    }

    public Integer getRevsion() {
        return revsion;
    }

    public void setRevsion(Integer revsion) {
        this.revsion = revsion;
    }

    public String getDistinguish() {
        return distinguish;
    }

    public void setDistinguish(String distinguish) {
        this.distinguish = distinguish;
    }

    public String getParentMo() {
        return parentMo;
    }

    public void setParentMo(String parentMo) {
        this.parentMo = parentMo;
    }

    public Integer getBomRevsion() {
        return bomRevsion;
    }

    public void setBomRevsion(Integer bomRevsion) {
        this.bomRevsion = bomRevsion;
    }

    public Date getPlanInputDate() {
        return planInputDate;
    }

    public void setPlanInputDate(Date planInputDate) {
        this.planInputDate = planInputDate;
    }

    public Date getPlanCloseDate() {
        return planCloseDate;
    }

    public void setPlanCloseDate(Date planCloseDate) {
        this.planCloseDate = planCloseDate;
    }

    public Date getActualInputDate() {
        return actualInputDate;
    }

    public void setActualInputDate(Date actualInputDate) {
        this.actualInputDate = actualInputDate;
    }

    public Date getActualcLoseDate() {
        return actualcLoseDate;
    }

    public void setActualcLoseDate(Date actualcLoseDate) {
        this.actualcLoseDate = actualcLoseDate;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getInputProcessId() {
        return inputProcessId;
    }

    public void setInputProcessId(String inputProcessId) {
        this.inputProcessId = inputProcessId;
    }

    public String getOutputProcessId() {
        return outputProcessId;
    }

    public void setOutputProcessId(String outputProcessId) {
        this.outputProcessId = outputProcessId;
    }

    public Date getReachDate() {
        return reachDate;
    }

    public void setReachDate(Date reachDate) {
        this.reachDate = reachDate;
    }

    public Integer getMachineQty() {
        return machineQty;
    }

    public void setMachineQty(Integer machineQty) {
        this.machineQty = machineQty;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public Integer getIsSchedul() {
        return isSchedul;
    }

    public void setIsSchedul(Integer isSchedul) {
        this.isSchedul = isSchedul;
    }

    public Integer getSchedulQty() {
        return schedulQty;
    }

    public void setSchedulQty(Integer schedulQty) {
        this.schedulQty = schedulQty;
    }

    public Integer getInputQty() {
        return inputQty;
    }

    public void setInputQty(Integer inputQty) {
        this.inputQty = inputQty;
    }

    public Integer getOutputQty() {
        return outputQty;
    }

    public void setOutputQty(Integer outputQty) {
        this.outputQty = outputQty;
    }

    public Integer getNotQty() {
        return notQty;
    }

    public void setNotQty(Integer notQty) {
        this.notQty = notQty==null ? 0 :notQty ;
    }

    public Integer getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Integer scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public Integer getFailQty() {
        return failQty;
    }

    public void setFailQty(Integer failQty) {
        this.failQty = failQty;
    }

    public Integer getCloseFlag() {
        return closeFlag;
    }

    public void setCloseFlag(Integer closeFlag) {
        this.closeFlag = closeFlag;
    }

    public Integer getPrefreezingState() {
        return prefreezingState;
    }

    public void setPrefreezingState(Integer prefreezingState) {
        this.prefreezingState = prefreezingState;
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
}