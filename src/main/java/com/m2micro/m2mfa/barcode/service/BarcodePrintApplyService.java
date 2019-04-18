package com.m2micro.m2mfa.barcode.service;

import com.m2micro.m2mfa.barcode.entity.BarcodePrintApply;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.barcode.entity.BarcodePrintResources;
import com.m2micro.m2mfa.barcode.query.PrintApplyQuery;
import com.m2micro.m2mfa.barcode.query.ScheduleQuery;
import com.m2micro.m2mfa.barcode.vo.PrintApplyObj;
import com.m2micro.m2mfa.barcode.vo.ScheduleObj;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;

/**
 * 标签打印表单 服务类
 * @author liaotao
 * @since 2019-03-27
 */
public interface BarcodePrintApplyService extends BaseService<BarcodePrintApply,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
//    PageUtil<BarcodePrintApply> list(PrintApplyQuery query);
    boolean exist(String sourceCategory,String sourceNo,String partId);

    PageUtil<ScheduleObj> list(ScheduleQuery query);

    ScheduleObj scheduleInfo(String scheduleId);

    BarcodePrintApply add(BarcodePrintApply barcodePrintApply);

    void checkList(String[] ids);

    void check(String barcodePrintApplyId);

    void deleteList(String[] ids);

    PageUtil<PrintApplyObj> printApplyList(PrintApplyQuery query);

    PageUtil<PrintApplyObj> printApplyListAfterCheckedOk(PrintApplyQuery query);

    PrintApplyObj printDetail(String applyId);
    List<BarcodePrintResources> generateLabel(String applyId, Integer num/*份数*/) ;

    void printCheckList(String[] ids,Integer flag);
}
