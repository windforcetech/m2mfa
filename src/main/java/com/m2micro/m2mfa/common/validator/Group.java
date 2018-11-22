package com.m2micro.m2mfa.common.validator;

import javax.validation.GroupSequence;

/**
 * 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 * @author liaotao
 * @date 2018-11-21
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}
