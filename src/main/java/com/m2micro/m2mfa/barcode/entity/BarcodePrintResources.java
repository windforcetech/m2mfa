package com.m2micro.m2mfa.barcode.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 标签打印记录
 * @author liaotao
 * @since 2019-03-27
 */
@Entity
@ApiModel(value="BarcodePrintResources对象", description="标签打印记录")
public class BarcodePrintResources extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String applyId;
    private String barcode;
    private String content;
    private Integer flag;
    private String description;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getApplyId() {
        return applyId;
    }
    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFlag() {
        return flag;
    }
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }



}