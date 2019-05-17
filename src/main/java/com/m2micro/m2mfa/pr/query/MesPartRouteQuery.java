package com.m2micro.m2mfa.pr.query;
import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//途程查找
@ApiModel(description = "途程基本资料")
public class MesPartRouteQuery extends Query {

    @ApiModelProperty(value = "料件编号")
    private String partNo;

    @ApiModelProperty(value = "途程名称")
    private String touteName;

    @ApiModelProperty(value = "管制方式")
    private String controlInformation;
    @ApiModelProperty(value = "有效否")
    private Integer enabled;

    @ApiModelProperty(value = "描述")
    private String description;

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

    public MesPartRouteQuery() {
    }

    public MesPartRouteQuery(String partNo, String touteName, String controlInformation) {
        this.partNo = partNo;
        this.touteName = touteName;
        this.controlInformation = controlInformation;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getTouteName() {
        return touteName;
    }

    public void setTouteName(String touteName) {
        this.touteName = touteName;
    }

    public String getControlInformation() {
        return controlInformation;
    }

    public void setControlInformation(String controlInformation) {
        this.controlInformation = controlInformation;
    }
}
