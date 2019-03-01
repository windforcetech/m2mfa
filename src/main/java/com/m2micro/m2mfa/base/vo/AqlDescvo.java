package com.m2micro.m2mfa.base.vo;

import com.m2micro.m2mfa.base.entity.BaseAqlDef;
import com.m2micro.m2mfa.base.entity.BaseAqlDesc;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AqlDescvo {
  BaseAqlDesc baseAqlDesc;
  List<BaseAqlDef> baseAqlDefs;
}
