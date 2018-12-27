package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 包装
 * @author wanglei
 * @since 2018-12-27
 */
@Entity
@ApiModel(value="包装", description="包装")
public class BasePack extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @Size(max=50,message = "料件编号长度不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "料件编号")
    private String partId;
    @ApiModelProperty(value = "类型(0单品,1盒,2箱,3板)")
    private Integer category;
    @Size(max=50,message = "包装条件长度不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "包装条件")
    private String packMode;
    @ApiModelProperty(value = "容量")
    private BigDecimal qty;
    @ApiModelProperty(value = "标签打印方式")
    @Size(max=50,message = "标签打印方式长度不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    private String printMode;
    @ApiModelProperty(value = "标签打印份数")
    private Integer printPage;
    @ApiModelProperty(value = "序号长度")
    private Integer lableLength;
    @ApiModelProperty(value = "净重")
    private BigDecimal nw;
    @ApiModelProperty(value = "毛重")
    private BigDecimal gw;
    @ApiModelProperty(value = "长")
    private BigDecimal l;
    @ApiModelProperty(value = "宽")
    private BigDecimal w;
    @ApiModelProperty(value = "高")
    private BigDecimal h;
    @ApiModelProperty(value = "材积")
    private BigDecimal cuft;
    @ApiModelProperty(value = "图档")
    @Size(max=200,message = "图档长度不能大于200位",groups = {AddGroup.class, UpdateGroup.class})
    private String imageUrl;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public Integer getCategory() {
        return category;
    }
    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getPackMode() {
        return packMode;
    }
    public void setPackMode(String packMode) {
        this.packMode = packMode;
    }

    public BigDecimal getQty() {
        return qty;
    }
    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getPrintMode() {
        return printMode;
    }
    public void setPrintMode(String printMode) {
        this.printMode = printMode;
    }

    public Integer getPrintPage() {
        return printPage;
    }
    public void setPrintPage(Integer printPage) {
        this.printPage = printPage;
    }

    public Integer getLableLength() {
        return lableLength;
    }
    public void setLableLength(Integer lableLength) {
        this.lableLength = lableLength;
    }

    public BigDecimal getNw() {
        return nw;
    }
    public void setNw(BigDecimal nw) {
        this.nw = nw;
    }

    public BigDecimal getGw() {
        return gw;
    }
    public void setGw(BigDecimal gw) {
        this.gw = gw;
    }

    public BigDecimal getL() {
        return l;
    }
    public void setL(BigDecimal l) {
        this.l = l;
    }

    public BigDecimal getW() {
        return w;
    }
    public void setW(BigDecimal w) {
        this.w = w;
    }

    public BigDecimal getH() {
        return h;
    }
    public void setH(BigDecimal h) {
        this.h = h;
    }

    public BigDecimal getCuft() {
        return cuft;
    }
    public void setCuft(BigDecimal cuft) {
        this.cuft = cuft;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



}