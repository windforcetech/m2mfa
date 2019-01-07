package com.m2micro.m2mfa.pr.vo;

import com.m2micro.framework.starter.entity.Organization;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Transient;

public class OrganizationalStation {
  private  Organization organization;
    @Transient
    @ApiModelProperty("所属工位ID")
    private String stationId;
    @Transient
    @ApiModelProperty("工位名称")
    private String stationName;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
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
}
