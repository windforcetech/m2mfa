package com.m2micro.m2mfa.barcode.query;
import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BarcodeQuery extends  Query {
  @ApiModelProperty("Id")
  private String applyId;
}
