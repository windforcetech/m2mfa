package com.m2micro.m2mfa.mo.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author liaotao
 * @since 2019-01-04
 */
@Entity
@ApiModel(value="MesMoScheduleShift对象", description="")
public class MesMoScheduleShift extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @ApiModelProperty(value = "排产单Id")
    private String scheduleId;
    @ApiModelProperty(value = "班别ID")
    private String shiftId;
    @Transient
    @ApiModelProperty(value = "班别名称")
    private String shiftName;

    @Transient
    @ApiModelProperty(value = "班别有效时间")
    private long ffectiveTime;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public long getFfectiveTime() {
        return ffectiveTime;
    }

    public void setFfectiveTime(long ffectiveTime) {
        this.ffectiveTime = ffectiveTime;
    }
}