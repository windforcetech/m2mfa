package com.m2micro.m2mfa.barcode.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 标签打印表单
 *
 * @author liaotao
 * @since 2019-03-27
 */
@Entity
@ApiModel(value = "BarcodePrintApply对象", description = "标签打印表单")
public class BarcodePrintApply extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "编号")
    @Id
    private String id;
    //    @Size(max=50,message = "料件编号长度不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="打印类型不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "打印类型")
    private String printCategory;
    @NotEmpty(message="来源类型不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "来源类型")
    private String category;
    @NotEmpty(message="来源单号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "来源单号")
    private String source;
    @ApiModelProperty(value = "来源项次")
    private Integer sequence;
//    @NotEmpty(message="标签类型不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "标签类型")
    private String lableCategory;
    @NotEmpty(message="标签模板不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "标签模板")
    private String templateId;
//    @NotEmpty(message="客户编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "客户编号")
    private String customerNo;
    @NotEmpty(message="料件Id不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "料件Id")
    private String partId;
//    @NotEmpty(message="数量不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "数量")
    private Integer qty;
    @ApiModelProperty(value = "审核状态")
    private Integer checkFlag;
    @ApiModelProperty(value = "审核日期")
    private Date checkOn;
    @ApiModelProperty(value = "审核用户主键")
    private String checkBy;
    @ApiModelProperty(value = "领用时间")
    private Date collarOn;
    @ApiModelProperty(value = "领用用户主键")
    private String collarBy;
    @ApiModelProperty(value = "状态")
    private Integer flag;
    @ApiModelProperty(value = "排序码")
    private Integer sortCode;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrintCategory() {
        return printCategory;
    }

    public void setPrintCategory(String printCategory) {
        this.printCategory = printCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getLableCategory() {
        return lableCategory;
    }

    public void setLableCategory(String lableCategory) {
        this.lableCategory = lableCategory;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(Integer checkFlag) {
        this.checkFlag = checkFlag;
    }

    public Date getCheckOn() {
        return checkOn;
    }

    public void setCheckOn(Date checkOn) {
        this.checkOn = checkOn;
    }

    public String getCheckBy() {
        return checkBy;
    }

    public void setCheckBy(String checkBy) {
        this.checkBy = checkBy;
    }

    public Date getCollarOn() {
        return collarOn;
    }

    public void setCollarOn(Date collarOn) {
        this.collarOn = collarOn;
    }

    public String getCollarBy() {
        return collarBy;
    }

    public void setCollarBy(String collarBy) {
        this.collarBy = collarBy;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getSortCode() {
        return sortCode;
    }

    public void setSortCode(Integer sortCode) {
        this.sortCode = sortCode;
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


    public static String serialNumber(Integer number) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String format = df.format(new Date());
        int l = 4;
        String num = number.toString();
        while (num.length() < l)
            num = "0" + num;
        return format + num;
    }


}