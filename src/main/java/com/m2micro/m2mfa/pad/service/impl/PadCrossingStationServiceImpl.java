package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.barcode.entity.BarcodePrintResources;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintApplyRepository;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintResourcesRepository;
import com.m2micro.m2mfa.barcode.service.BarcodePrintResourcesService;
import com.m2micro.m2mfa.base.constant.BaseItemsTargetConstant;
import com.m2micro.m2mfa.base.constant.ProcessConstant;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.repository.BaseDefectRepository;
import com.m2micro.m2mfa.base.repository.BasePackRepository;
import com.m2micro.m2mfa.base.repository.BaseRouteDefRepository;
import com.m2micro.m2mfa.base.repository.BaseStaffRepository;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import com.m2micro.m2mfa.base.service.BaseProcessService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.QueryGroup;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.m2mfa.mo.model.ScheduleAndPartsModel;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleStaffRepository;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleProcessService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.constant.StationConstant;
import com.m2micro.m2mfa.pad.model.*;
import com.m2micro.m2mfa.pad.service.PadBottomDisplayService;
import com.m2micro.m2mfa.pad.service.PadCrossingStationService;
import com.m2micro.m2mfa.pad.util.PadStaffUtil;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.entity.MesPartRouteProcess;
import com.m2micro.m2mfa.pr.repository.MesPartRouteProcessRepository;
import com.m2micro.m2mfa.pr.repository.MesPartRouteRepository;
import com.m2micro.m2mfa.record.entity.*;
import com.m2micro.m2mfa.record.repository.MesRecordFailRepository;
import com.m2micro.m2mfa.record.repository.MesRecordWipFailRepository;
import com.m2micro.m2mfa.record.repository.MesRecordWipLogRepository;
import com.m2micro.m2mfa.record.repository.MesRecordWipRecRepository;
import com.m2micro.m2mfa.record.service.MesRecordWipRecService;
import javafx.scene.media.MediaMarkerEvent;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("padCrossingStationService")
public class PadCrossingStationServiceImpl implements PadCrossingStationService {
    @Autowired
    BarcodePrintResourcesService barcodePrintResourcesService;
    @Autowired
    BarcodePrintResourcesRepository barcodePrintResourcesRepository;
    @Autowired
    MesRecordWipRecRepository mesRecordWipRecRepository;
    @Autowired
    BaseProcessService baseProcessService;
    @Autowired
    BarcodePrintApplyRepository barcodePrintApplyRepository;
    @Autowired
    MesRecordFailRepository mesRecordFailRepository;
    @Autowired
    BaseItemsTargetService baseItemsTargetService;
    @Autowired
    MesPartRouteRepository mesPartRouteRepository;
    @Autowired
    MesPartRouteProcessRepository mesPartRouteProcessRepository;
    @Autowired
    BasePackRepository basePackRepository;
    @Autowired
    MesMoScheduleService mesMoScheduleService;
    @Autowired
    MesMoDescService mesMoDescService;
    @Autowired
    PadBottomDisplayService padBottomDisplayService;
    @Autowired
    MesRecordWipLogRepository mesRecordWipLogRepository;
    @Autowired
    BaseRouteDefRepository baseRouteDefRepository;
    @Autowired
    MesMoScheduleRepository mesMoScheduleRepository;
    @Autowired
    BaseStaffRepository baseStaffRepository;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseDefectRepository baseDefectRepository;
    @Autowired
    MesRecordWipFailRepository mesRecordWipFailRepository;
    @Autowired
    MesMoScheduleProcessService mesMoScheduleProcessService;
    @Autowired
    ProcessConstant processConstant;
    @Autowired
    MesMoScheduleStaffRepository mesMoScheduleStaffRepository;



    @Override
    @Transactional
    public ResponseMessage<CrossingStationModel> getCrossingStationInfo(CrossingStationPara para) {
        ValidatorUtil.validateEntity(para, QueryGroup.class);
        //获取标签打印信息
        BarcodePrintResources barcodePrintResources = barcodePrintResourcesRepository.findByBarcode(para.getBarcode());
        if(barcodePrintResources==null){
            throw new MMException("扫描产品序号/箱号/周转箱号非法，没有打印记录!");
        }
        //获取标签在制信息
        MesRecordWipRec mesRecordWipRec = mesRecordWipRecRepository.findBySerialNumber(para.getBarcode());
        if(mesRecordWipRec==null){
            //处理没有在制信息
            return handForNotMesRecordWipRec(para);
        }
        //处理有在制信息
        return handForMesRecordWipRec(para,mesRecordWipRec);
    }

    @Override
    public List<WipRecModel> pullIn(String processId,String barcode) {
        if(StringUtils.isEmpty(processId)){
            throw new MMException("工序id不能为空！");
        }
        if(StringUtils.isEmpty(barcode)){
            throw new MMException("条码标签不能为空！");
        }
        //获取排产单id
        String source = barcodePrintApplyRepository.getSource(barcode);
        List<MesRecordWipRec> mesRecordWipRecs = mesRecordWipRecRepository.findByNextProcessIdAndScheduleId(processId,source);
        if(mesRecordWipRecs==null){
            return null;
        }
        List<WipRecModel> list = new ArrayList<>();
        mesRecordWipRecs.stream().forEach(mesRecordWipRec -> {
            WipRecModel wipRecModel = new WipRecModel();
            wipRecModel.setSerialNumber(mesRecordWipRec.getSerialNumber());
            wipRecModel.setOutputQty(mesRecordWipRec.getOutputQty());
            wipRecModel.setOutTime(mesRecordWipRec.getOutTime());
            list.add(wipRecModel);
        });
        return list;
    }

    @Override
    @Transactional
    public void pullOut(OutStationModel para) {
        ValidatorUtil.validateEntity(para,QueryGroup.class);
        MesRecordWipRec mesRecordWipRec = mesRecordWipRecRepository.findBySerialNumber(para.getBarcode());
        //获取排产单id
        String source = barcodePrintApplyRepository.getSource(para.getBarcode());
        MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(source).orElse(null);
        //获取料件途程id
        String partRouteId = mesPartRouteRepository.getPartRouteId(source);
        //获取上一工序
        String beforeProcessId = baseRouteDefRepository.getBeforeProcessId(partRouteId, para.getProcessId());
        //获取下一工序
        String nextProcessId = baseRouteDefRepository.getNextProcessId(partRouteId, para.getProcessId());
        //进站数（上一次的产出）
        Integer outputQty = mesRecordWipRec.getOutputQty();
        //获取不良总数
        ArrayList<CrossStationFail> crossStationFails = para.getCrossStationFails();
        para.setQty(getQty(crossStationFails));
        //【产出数量】+【不良数量】 != 进站的数量
        if(para.getOutputQty()+para.getQty()!=outputQty){
            throw new MMException("不良数量加产出数不等于进站数，请核对！");
        }
        if(para.getOutputQty()<0){
            throw new MMException("不良数量不能大于产出数量！");
        }
        /*
        1) 保存上一次在制信息到日志
        2）更新mes_record_wip_rec在制信息
        3）保存不良
        4）是否结束工序
        5）更新工单（mes_mo_desc）的产出数（当前产出工序是产出工序）
        6）保存最后一次在制信息到日志（当前产出工序是产出工序）
        7）是否结束排产单
        8）是否结束工单
        */

        //获取产出工序
        MesPartRoute mesPartRoute = mesPartRouteRepository.findById(partRouteId).orElse(null);
        //工序是否是产出工序
        Boolean isOutputProcess = isOutputProcess(para.getProcessId(), mesPartRoute.getOutputProcessId());
        //如果是注塑成型，保存上一次（第一次）在制信息到日志
        /*Boolean firstProcess = isFirstProcess(beforeProcessId);
        if(firstProcess){
            copyDataToLog(mesRecordWipRec);
        }*/
        //更新mes_record_wip_rec
        updateMesRecordWipRec(para, mesRecordWipRec, nextProcessId, isOutputProcess);
        //更新工序产量（产出，不良）
        updateProcessOutputQty(crossStationFails,source,para);
        //保存当前在制信息到日志
        copyDataToLog(mesRecordWipRec);
        //保存不良
        saveMesRecordWipFail(para.getCrossStationFails(),source,para.getProcessId(),para.getStationId(),para.getBarcode());
        //是否结束工序
        Boolean endCrossStationProcess = isEndCrossStationProcess(source, beforeProcessId, para.getProcessId());
        //是否结束第一个工序
        Boolean endFirstProcess = isEndFirstProcess(para, source, beforeProcessId);

        if(endCrossStationProcess){
            //上一工序是注塑成型
            if(isEndFirstProcess(para,source,beforeProcessId)){
                //结束工序
                mesMoScheduleProcessService.endProcess(source,para.getProcessId());
            }

        }
        //if(工序是否是产出工序)
        if(isOutputProcess){
            //更新工单（mes_mo_desc）的产出数（OutputQty
            updateMoDescForOutputQty(mesRecordWipRec, source,mesMoSchedule.getMoId());
            //将当前工序放入日志，因为是最后一道工序
            //copyDataToLog(mesRecordWipRec);
            //产出工序已结束
            if(endCrossStationProcess){
                //结束排产单
                endSchedule(source, mesMoSchedule.getScheduleQty());
                //结束工单操作：工单下所有排产单已结束且工单的可排数量为0就结束排产单，否则不做任何操作
                mesMoDescService.endMoDesc(mesMoSchedule.getMoId());

            }
        }

    }

    @Transactional
    public void updateProcessOutputQty(ArrayList<CrossStationFail> crossStationFails,String source,OutStationModel para){
        //获取注塑成型所在工序的不良
        Long fail = 0l;
        String processId=null;
        for(CrossStationFail crossStationFail:crossStationFails){
            if(isFirstProcess(crossStationFail.getProcessId())){
                fail=fail+crossStationFail.getQty();
                processId=crossStationFail.getProcessId();
            }
        }
        if(processId!=null){
            //将注塑成型工序不良数减去
            mesMoScheduleProcessService.updateOutputQtyForFail(source,processId,fail.intValue());
        }
        //更新当前过站工序的产出
        mesMoScheduleProcessService.updateOutputQtyForAdd(source,para.getProcessId(),para.getOutputQty());
    }

    private void updateInlineTimeAndNowProcessId(MesRecordWipRec mesRecordWipRec) {
        MesRecordWipLog mesRecordWipRecLog = new MesRecordWipLog();
        copyData(mesRecordWipRec, mesRecordWipRecLog);
        mesRecordWipRecLog.setOutlineTime(new Date());
        mesRecordWipLogRepository.save(mesRecordWipRecLog);
    }

    private Boolean isFirstProcess(String processId){
        BaseProcess baseProcess = baseProcessService.findById(processId).orElse(null);
        if(processConstant.getProcessCode().equals(baseProcess.getProcessCode())){
            return true;
        }
        return false;
    }

    /**
     * 是否结束第一个工序
     * @param para
     * @param source
     * @param beforeProcessId
     * @return
     */
    private Boolean isEndFirstProcess(OutStationModel para, String source, String beforeProcessId) {
        BaseProcess baseProcess = baseProcessService.findById(beforeProcessId).orElse(null);
        //上一工序是注塑成型
        if(processConstant.getProcessCode().equals(baseProcess.getProcessCode())){
            //获取结余量
            Integer surplusQty = getSurplusQty(para.getProcessId(), source,beforeProcessId);
            if(surplusQty!=0){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取不良总数
     * @param crossStationFails
     * @return
     */
    private Long getQty(ArrayList<CrossStationFail> crossStationFails) {
        Long qty=0l;
        if(crossStationFails!=null&&crossStationFails.size()>0){
            for(CrossStationFail crossStationFail:crossStationFails){
                if(crossStationFail.getQty()<0){
                    throw new MMException("不良数不能小于0，请核对！");
                }
                qty=qty+crossStationFail.getQty();
            }
        }
        return qty;
    }


    /**
     * 结束排产单
     * @param source
     *          排产单id
     * @param scheduleQty
     */
    private void endSchedule(String source, Integer scheduleQty) {
        Integer outPutQtys = padBottomDisplayService.getOutPutQtys(source);
        if(outPutQtys.compareTo(scheduleQty)>0){
            //已超量
            mesMoScheduleRepository.setFlagFor(MoScheduleStatus.EXCEEDED.getKey(),source);
        }else {
            //已完成
            mesMoScheduleRepository.setFlagFor(MoScheduleStatus.CLOSE.getKey(),source);
        }
    }

    private void updateMoDescForOutputQty(MesRecordWipRec mesRecordWipRec, String source,String moId) {
        MesMoDesc mesMoDesc = mesMoDescService.findById(moId).orElse(null);
        Integer moQty = mesMoDesc.getOutputQty()==null?0:mesMoDesc.getOutputQty();
        mesMoDesc.setOutputQty(moQty+mesRecordWipRec.getOutputQty());
        mesMoDescService.save(mesMoDesc);
    }

    /**
     * 是否结束过站的工序
     * @param scheduleId
     *          排产单
     * @param beforeProcessId
     *          上一工序
     * @param processId
     *          当前工序
     * @return
     */
    private Boolean isEndCrossStationProcess(String scheduleId,String beforeProcessId,String processId){
        //上一工序是否结束
        Boolean endProcess = mesMoScheduleProcessService.isEndProcess(scheduleId, beforeProcessId);
        //待进站是否已处理完毕
        Boolean completedForPullIn = isCompletedForPullIn(scheduleId,processId);
        //上工序如果是注塑成型工序:三个条件：结余量==0，上工序已结束，待进站已处理
        BaseProcess baseProcess = baseProcessService.findById(beforeProcessId).orElse(null);
        if(processConstant.getProcessCode().equals(baseProcess.getProcessCode())){
            //获取结余量
            Integer surplusQty = getSurplusQty(processId, scheduleId,beforeProcessId);
            if(surplusQty==0&&endProcess&&completedForPullIn){
                return true;
            }else {
                return false;
            }
        }
        //上一工序已结束并且待进站已处理完毕
        if(endProcess&&completedForPullIn){
            return true;
        }
        return false;
    }

    /**
     * 待进站是否已处理完
     * @param scheduleId
     * @param processId
     * @return
     */
    private Boolean isCompletedForPullIn(String scheduleId,String processId){
        //待进站是否已处理完毕
        List<MesRecordWipRec> mesRecordWipRecs = mesRecordWipRecRepository.findByNextProcessIdAndScheduleId(processId,scheduleId);
        if(mesRecordWipRecs!=null&&mesRecordWipRecs.size()>0){
            return false;//还未处理完
        }
        return true;
    }


    /**
     * 保存到在制不良记录表
     * @return
     */
    @Transactional
    public void saveMesRecordWipFail(List<CrossStationFail> crossStationFails,String scheduleId,String processId,String stationId,String barcode) {
        if(crossStationFails==null||crossStationFails.size()==0){
            return;
        }
        List<MesRecordWipFail> mesRecordWipFails = new ArrayList<>();
        for(CrossStationFail crossStationFail:crossStationFails){
            MesRecordWipFail mesRecordWipFail = new MesRecordWipFail();
            mesRecordWipFail.setId(UUIDUtil.getUUID());
            mesRecordWipFail.setSerialNumber(barcode);
            MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(scheduleId).orElse(null);
            mesRecordWipFail.setMoId(mesMoSchedule.getMoId());
            mesRecordWipFail.setScheduleId(mesMoSchedule.getScheduleId());
            mesRecordWipFail.setPartId(mesMoSchedule.getPartId());
            mesRecordWipFail.setTargetProcessId(crossStationFail.getProcessId());
            mesRecordWipFail.setNowProcessId(processId);
            mesRecordWipFail.setNowStationId(stationId);
            mesRecordWipFail.setStaffId(PadStaffUtil.getStaff().getStaffId());
            mesRecordWipFail.setDefectCode(crossStationFail.getDefectCode());
            mesRecordWipFail.setFailQty(crossStationFail.getQty()==null?null:crossStationFail.getQty().intValue());
            mesRecordWipFails.add(mesRecordWipFail);
        }
        mesRecordWipFailRepository.saveAll(mesRecordWipFails);
    }

    private void copyDataToLog(MesRecordWipRec mesRecordWipRec) {
        MesRecordWipLog mesRecordWipRecLog = new MesRecordWipLog();
        copyData(mesRecordWipRec, mesRecordWipRecLog);
        mesRecordWipRecLog.setId(UUIDUtil.getUUID());
        mesRecordWipLogRepository.save(mesRecordWipRecLog);
    }

    private void updateMesRecordWipRec(OutStationModel para, MesRecordWipRec mesRecordWipRec, String nextProcessId, Boolean isOutputProcess) {
        mesRecordWipRec.setInputQty(mesRecordWipRec.getOutputQty());
        mesRecordWipRec.setOutputQty(para.getOutputQty());
        Date date = new Date();
        mesRecordWipRec.setOutTime(date);
        mesRecordWipRec.setOutlineTime(date);
        mesRecordWipRec.setWipNowProcess(para.getProcessId());
        mesRecordWipRec.setProcessId(para.getProcessId());
        if(isOutputProcess){
            mesRecordWipRec.setWipNextProcess("");
            mesRecordWipRec.setNextProcessId("");
        }else {
            mesRecordWipRec.setWipNextProcess(nextProcessId);
            mesRecordWipRec.setNextProcessId(nextProcessId);
        }
        mesRecordWipRec.setStaffId(PadStaffUtil.getStaff().getStaffId());
        mesRecordWipRecRepository.save(mesRecordWipRec);
    }


    private void copyData(MesRecordWipRec mesRecordWipRec, MesRecordWipLog log) {
        try {
            BeanUtils.copyProperties(log, mesRecordWipRec);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MMException("在制记录异常");
        }
    }

    /**
     * 是否是产出工序
     * @param processId
     * @param outputProcessId
     * @return
     */
    private Boolean isOutputProcess(String processId,String outputProcessId){
        if(processId.equalsIgnoreCase(outputProcessId)){
            return true;
        }
        return false;
    }

    /**
     * 处理没有在制信息
     * @param para
     * @return
     */
    @Transactional
    public ResponseMessage<CrossingStationModel> handForNotMesRecordWipRec(CrossingStationPara para) {
        //获取排产单id
        String source = barcodePrintApplyRepository.getSource(para.getBarcode());
        //获取料件途程id
        String partRouteId = mesPartRouteRepository.getPartRouteId(source);
        //获取第一个扫描过站的工序
        String firstScanProcessId = mesPartRouteProcessRepository.getFirstScanProcessId(BaseItemsTargetConstant.SCAN, partRouteId);
        //判定当前工序是否是第一个扫描过站的工序
        if(!para.getProcessId().equalsIgnoreCase(firstScanProcessId)){
            throw new MMException("该工序不是第一个扫描过程工序，不能创建在制信息!");
        }
        //获取包装数
        BasePack basePack = basePackRepository.getBasePackByScheduleId(source);
        if(basePack==null){
            throw new MMException("无包装数量，请补全!");
        }
        //创建在制信息
        MesRecordWipRec mesRecordWipRec = new MesRecordWipRec();
        mesRecordWipRec.setId(UUIDUtil.getUUID());
        mesRecordWipRec.setRwId(para.getRwId());
        mesRecordWipRec.setScheduleId(source);
        mesRecordWipRec.setSerialNumber(para.getBarcode());
        MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(source).orElse(null);
        mesRecordWipRec.setMoId(mesMoSchedule.getMoId());
        MesMoDesc mesMoDesc = mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
        mesRecordWipRec.setPartId(mesMoDesc.getPartId());
        //上一个工序
        String beforeProcessId = baseRouteDefRepository.getBeforeProcessId(partRouteId, para.getProcessId());
        //获取结余量
        Integer surplusQty = getSurplusQty(para.getProcessId(), source,beforeProcessId);
        //投入数量，产出数量：包装量小于结余量，以包装量为准，否则以结余量为主
        Integer packQty = basePack.getQty()==null?0:basePack.getQty().intValue();
        //包装量小于结余量
        if(packQty<surplusQty){
            mesRecordWipRec.setInputQty(packQty);
            mesRecordWipRec.setOutputQty(packQty);
        }else{
            mesRecordWipRec.setInputQty(surplusQty);
            mesRecordWipRec.setOutputQty(surplusQty);
        }

        //MesPartRouteProcess mesPartRouteProcess = mesPartRouteProcessRepository.findByPartrouteidAndNextprocessid(partRouteId, para.getProcessId());
        //上一个工序
        mesRecordWipRec.setProcessId(beforeProcessId);
        mesRecordWipRec.setWipNowProcess(beforeProcessId);

        Date date = new Date();
        mesRecordWipRec.setInTime(date);
        mesRecordWipRec.setInlineTime(date);
        mesRecordWipRec.setOutTime(date);
        mesRecordWipRec.setOutlineTime(date);
        //职员（预留）
        MesMoScheduleStaff mesMoScheduleStaff = mesMoScheduleStaffRepository.getMesMoScheduleStaffForBoot(source, StationConstant.BOOT.getKey());
        if(mesMoScheduleStaff!=null){
            mesRecordWipRec.setStaffId(mesMoScheduleStaff.getStaffId());
        }
        //途程
        mesRecordWipRec.setRouteId(partRouteId);
        //下一工序
        mesRecordWipRec.setWipNextProcess(para.getProcessId());
        mesRecordWipRec.setNextProcessId(para.getProcessId());
        mesRecordWipRecRepository.save(mesRecordWipRec);

        //如果是注塑成型，保存上一次（第一次）在制信息到日志
        Boolean firstProcess = isFirstProcess(beforeProcessId);
        if(firstProcess){
            copyDataToLog(mesRecordWipRec);
        }

        CrossingStationModel crossingStationModel = new CrossingStationModel();
        //上一工序是否是机台自动扫描工序，是 算结余量
        if(BaseItemsTargetConstant.AUTO.equalsIgnoreCase(getCollection(beforeProcessId))){
            crossingStationModel.setSurplusQty(getSurplusQty(para.getProcessId(), source,beforeProcessId));
        }
        //返回不良数0、及排产单信息
        crossingStationModel.setBarcode(para.getBarcode());
        crossingStationModel.setQty(0l);
        crossingStationModel.setOutputQty(mesRecordWipRec.getOutputQty());

        //上一工序的相关信息
        //StationRelationModel stationRelationModel = mesRecordWipRecRepository.getStationRelationModel(para.getBarcode());
        StationRelationModel stationRelationModel = getStationRelationModel(para.getBarcode(), source,beforeProcessId);
        crossingStationModel.setStationRelationModel(stationRelationModel);
        return ResponseMessage.ok(crossingStationModel);
    }

    /**
     * 获取关联信息
     * @param barcode
     * @param scheduleId
     * @return
     */
    private StationRelationModel getStationRelationModel(String barcode,String scheduleId,String beforeProcessId){
        //获取排产单信息
        ScheduleAndPartsModel scheduleAndPartsModel = getScheduleAndPartsModel(scheduleId);
        //获取在制表信息
        MesRecordWipRec mesRecordWipRec = mesRecordWipRecRepository.findBySerialNumber(barcode);
        //在制信息表此时有两种：未进站（上一个工序是真实的上一个工序），进站未出站（上一个工序为当前工序），不管哪种都更改为上一工序
        return getStationRelationModel(mesRecordWipRec,scheduleAndPartsModel,beforeProcessId);
    }

    /**
     * 获取关联信息
     * @param mesRecordWipRec
     * @param scheduleAndPartsModel
     * @return
     */
    private StationRelationModel getStationRelationModel(MesRecordWipRec mesRecordWipRec,ScheduleAndPartsModel scheduleAndPartsModel,String beforeProcessId){
        StationRelationModel stationRelationModel = new StationRelationModel();
        //获取排产单信息
        try {
            BeanUtils.copyProperties(stationRelationModel,scheduleAndPartsModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置在制表信息
        //上一工序
        if(StringUtils.isNotEmpty(beforeProcessId)){
            BaseProcess baseProcess = baseProcessService.findById(beforeProcessId).orElse(null);
            stationRelationModel.setWipNowProcessName(baseProcess.getProcessName());
        }
        //职员
        String staffId = mesRecordWipRec.getStaffId();
        if(StringUtils.isNotEmpty(staffId)){
            BaseStaff baseStaff = baseStaffRepository.findById(staffId).orElse(null);
            stationRelationModel.setStaffId(staffId);
            stationRelationModel.setStaffName(baseStaff.getStaffName());
        }
        //产出时间
        stationRelationModel.setOutTime(mesRecordWipRec.getOutTime());
        //产出数量
        stationRelationModel.setOutputQty(mesRecordWipRec.getOutputQty());
        //下一工序
        String wipNextProcess = mesRecordWipRec.getWipNextProcess();
        if(StringUtils.isNotEmpty(wipNextProcess)){
            BaseProcess baseProcess = baseProcessService.findById(wipNextProcess).orElse(null);
            stationRelationModel.setWipNextProcessName(baseProcess.getProcessName());
            //采集方式
            BaseItemsTarget baseItemsTarget = baseItemsTargetService.findById(baseProcess.getCollection()).orElse(null);
            stationRelationModel.setCollectionName(baseItemsTarget.getItemName());
        }
        return stationRelationModel;
    }

    /**
     * 获取排产单料件信息
     * @param scheduleId
     * @return
     */
    private ScheduleAndPartsModel getScheduleAndPartsModel(String scheduleId){
        String sql = "SELECT\n" +
                "	mms.schedule_id,\n" +
                "	mms.schedule_no scheduleNo,\n" +
                "	bpt.part_no partNo,\n" +
                "	bpt.name partName,\n" +
                "	bpt.spec partSpec \n" +
                "FROM\n" +
                "	mes_mo_schedule mms,\n" +
                "	base_parts bpt \n" +
                "WHERE\n" +
                "	mms.part_id = bpt.part_id \n" +
                "	AND mms.schedule_id ='"+scheduleId+"'\n";
        RowMapper<ScheduleAndPartsModel> rowMapper = BeanPropertyRowMapper.newInstance(ScheduleAndPartsModel.class);
        List<ScheduleAndPartsModel> list = jdbcTemplate.query(sql, rowMapper);
        if(list==null||list.size()==0){
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取结余量
     * @param processId
     * @param source
     * @return
     */
    private Integer getSurplusQty(String processId, String source,String beforeProcessId) {
        //1.获取排产单的完成量
        List scheduleIds = new ArrayList();
        scheduleIds.add(source);
        Integer outPutQtys = padBottomDisplayService.getMachineOutputQty(scheduleIds,beforeProcessId);
        //2.当前工序当前排产单的投入数之和
        Integer allInputQty = mesRecordWipRecRepository.getAllInputQty(source);
        allInputQty = allInputQty==null?0:allInputQty;
        //3.结余量：排产单的完成量-投入数之和
        if(outPutQtys<allInputQty){
            throw new MMException("排产单的完成量小于投入数总和。");
        }
        Integer surplusQty = outPutQtys-allInputQty;
        return surplusQty;
    }

    /**
     * 处理有在制信息
     * @param para
     * @return
     */
    private ResponseMessage<CrossingStationModel> handForMesRecordWipRec(CrossingStationPara para,MesRecordWipRec mesRecordWipRec) {

        CrossingStationModel crossingStationModel = new CrossingStationModel();
        crossingStationModel.setBarcode(para.getBarcode());
        //判断是否已使用
        /*if(para.getProcessId().equalsIgnoreCase(mesRecordWipRec.getWipNowProcess())){
            throw new MMException("序号【"+para.getBarcode()+"】已扫描过站，不能重复扫描。");
        }*/
        //下一工序与本作业工序是否符合
        if(!para.getProcessId().equalsIgnoreCase(mesRecordWipRec.getWipNextProcess())){
            BaseProcess baseProcess = baseProcessService.findById(mesRecordWipRec.getWipNextProcess()).orElse(null);
            if(baseProcess==null){
                throw new MMException("工艺流程出错,没有下一个工序！");
            }
            throw new MMException("工艺流程出错：该序号下一工序"+baseProcess.getProcessName());
        }
        //获取排产单id
        String source = barcodePrintApplyRepository.getSource(para.getBarcode());
        //默认不良为0
        crossingStationModel.setQty(0l);

        //获取上一个工序
        String beforeProcessId = mesRecordWipRec.getWipNowProcess();
        //如果相等表示已进站未出站
        if(mesRecordWipRec.getWipNowProcess().equals(mesRecordWipRec.getWipNextProcess())){
            //获取料件途程id
            String partRouteId = mesPartRouteRepository.getPartRouteId(source);
            //获取上一工序
            beforeProcessId = baseRouteDefRepository.getBeforeProcessId(partRouteId, mesRecordWipRec.getWipNowProcess());
        }

        //上一工序的相关信息
        //StationRelationModel stationRelationModel = mesRecordWipRecRepository.getStationRelationModel(para.getBarcode());
        StationRelationModel stationRelationModel = getStationRelationModel(para.getBarcode(), source,beforeProcessId);
        crossingStationModel.setStationRelationModel(stationRelationModel);
        //默认为上一次的产出量
        crossingStationModel.setOutputQty(stationRelationModel.getOutputQty());

        //上一工序是否是机台自动扫描工序，是 算结余量
        if(BaseItemsTargetConstant.AUTO.equalsIgnoreCase(getCollection(beforeProcessId))){
            //获取结余量
            Integer surplusQty = getSurplusQty(para.getProcessId(), source,beforeProcessId);
            crossingStationModel.setSurplusQty(surplusQty);
        }

        //更新进站工序和进站时间
        //MesRecordWipLog mesRecordWipRecLog = new MesRecordWipLog();
        //copyData(mesRecordWipRec, mesRecordWipRecLog);
        //mesRecordWipRec.setWipNowProcess(para.getProcessId());
       // mesRecordWipRec.setProcessId(para.getProcessId());
        Date date = new Date();
        mesRecordWipRec.setWipNowProcess(para.getProcessId());
        mesRecordWipRec.setProcessId(para.getProcessId());
        mesRecordWipRec.setInlineTime(date);
        mesRecordWipRec.setInTime(date);
        mesRecordWipRecRepository.save(mesRecordWipRec);

        return ResponseMessage.ok(crossingStationModel);
    }

    private String getCollection(String processId) {
        BaseProcess baseProcess = baseProcessService.findById(processId).orElse(null);
        BaseItemsTarget baseItemsTarget = baseItemsTargetService.findById(baseProcess.getCollection()).orElse(null);
        return baseItemsTarget.getItemValue();
    }

    private Long getQtyForFail(List<MesRecordFail> mesRecordFails) {
        if(mesRecordFails!=null&&mesRecordFails.size()>0){
            return mesRecordFails.stream().mapToLong(MesRecordFail::getQty).sum();
        }
        return 0l;
    }

    @Override
    public CrossStationDefectModel getCrossStationDefectModel(CrossStationDefectPara crossStationDefectPara) {
        ValidatorUtil.validateEntity(crossStationDefectPara,QueryGroup.class);
        CrossStationDefectModel crossStationDefectModel = new CrossStationDefectModel();
        //获取所有不良工序
        List<CrossStationProcess> allDefectProcess = getAllDefectProcess(crossStationDefectPara);
        crossStationDefectModel.setCrossStationProcesss(allDefectProcess);
        //获取所有有效的不良现象
        List<BaseDefect> baseDefects = findAllBaseDefect();
        crossStationDefectModel.setBaseDefects(baseDefects);
        return crossStationDefectModel;
    }

    @Override
    public List<CrossStationProcess>  getAllDefectProcess(CrossStationDefectPara crossStationDefectPara) {
        //获取排产单id
        String source = barcodePrintApplyRepository.getSource(crossStationDefectPara.getBarcode());
        //获取料件途程id
        String partRouteId = mesPartRouteRepository.getPartRouteId(source);
        //获取当前工序的顺序序号
        MesPartRouteProcess mesPartRouteProcess = mesPartRouteProcessRepository.findByPartrouteidAndProcessid(partRouteId, crossStationDefectPara.getProcessId());
        if(mesPartRouteProcess==null){
            throw new MMException("当前工序和条码关联工序不匹配！");
        }
        //获取小于当前工序顺序序号的所有工序
        return getAllCrossStationProcess(partRouteId, mesPartRouteProcess.getSetp());
    }

    @Override
    public List<BaseDefect> findAllBaseDefect() {
        return baseDefectRepository.findAllByEnabled(true);
    }


    /**
     * 获取小于当前工序顺序序号的所有工序
     * @param partRouteId
     * @param step
     * @return
     */
    public List<CrossStationProcess> getAllCrossStationProcess(String partRouteId,Integer step){
        String sql = "SELECT\n" +
                    "	bp.process_id,\n" +
                    "	bp.process_name\n" +
                    "FROM\n" +
                    "	mes_part_route_process mprp,\n" +
                    "	base_process bp \n" +
                    "WHERE\n" +
                    "	mprp.processid = bp.process_id \n" +
                    "AND mprp.partrouteid='" +partRouteId+"'\n" +
                    "AND mprp.setp<='" +step+"'\n";
        RowMapper<CrossStationProcess> rowMapper = BeanPropertyRowMapper.newInstance(CrossStationProcess.class);
        return jdbcTemplate.query(sql, rowMapper);
    }

}
