package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.barcode.entity.BarcodePrintResources;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintApplyRepository;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintResourcesRepository;
import com.m2micro.m2mfa.barcode.service.BarcodePrintResourcesService;
import com.m2micro.m2mfa.base.constant.BaseItemsTargetConstant;
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
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.model.ScheduleAndPartsModel;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
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
import com.m2micro.m2mfa.record.entity.MesRecordFail;
import com.m2micro.m2mfa.record.entity.MesRecordWipLog;
import com.m2micro.m2mfa.record.entity.MesRecordWipRec;
import com.m2micro.m2mfa.record.repository.MesRecordFailRepository;
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
        //获取料件途程id
        String partRouteId = mesPartRouteRepository.getPartRouteId(source);
        //获取上一工序
        //MesPartRouteProcess mesPartRouteProcess = mesPartRouteProcessRepository.findByPartrouteidAndNextprocessid(partRouteId, para.getProcessId());
        //获取下一工序
        //MesPartRouteProcess nextProcessid = mesPartRouteProcessRepository.findByPartrouteidAndProcessid(partRouteId, para.getProcessId());
        String nextProcessId = baseRouteDefRepository.getNextProcessId(partRouteId, para.getProcessId());

        //String collection = getCollection(mesPartRouteProcess.getProcessid());
        //上一工序是否是机台自动扫描工序，是 算结余量
        /*if(BaseItemsTargetConstant.AUTO.equalsIgnoreCase(getCollection(mesPartRouteProcess.getProcessid()))){
            //获取结余量
            Integer surplusQty = getSurplusQty(para.getProcessId(), source);
            //获取包装数
            BasePack basePack = basePackRepository.getBasePackByScheduleId(source);
            //获取包装数和结余量中的最小值
        }else {
            //进站数（上一次的产出）
            Integer outputQty = mesRecordWipRec.getOutputQty();
            //【产出数量】+【不良数量】 != 进站的数量
            if(para.getOutputQty()+para.getQty()!=outputQty){
                throw new MMException("此箱的产出数量加不良数量已超出上站产出数，请核对！");
            }
        }*/
        //进站数（上一次的产出）
        Integer outputQty = mesRecordWipRec.getOutputQty();
        //【产出数量】+【不良数量】 != 进站的数量
        if(para.getOutputQty()+para.getQty()!=outputQty){
            throw new MMException("此箱的产出数量加不良数量已超出上站产出数，请核对！");
        }
        /*1) Mes_Record_Wip_Log 新增一条记录  insert into mes_record_wip_log select * from mes_record_wip_rec where serialnumber = '" + SerialNumber + "' 。
        2)更新已处理的不良记录状态为1.
        3)更新  mes_record_wip_rec 。更新已处理的不良记录状态为1.
        if(工序是否是产出工序)
            更新工单（mes_mo_desc）的产出数（OutputQty）。*/
        //拷贝上一次数据到历史记录
        MesRecordWipLog mesRecordWipRecLog = new MesRecordWipLog();
        copyData(mesRecordWipRec, mesRecordWipRecLog);
        mesRecordWipRecLog.setId(UUIDUtil.getUUID());
        mesRecordWipLogRepository.save(mesRecordWipRecLog);
        //更新已处理的不良记录状态为1.
        if(para.getIds()!=null&&para.getIds().size()>0){
            List<MesRecordFail> mesRecordFails = mesRecordFailRepository.findAllById(para.getIds());
            mesRecordFails.stream().forEach(mesRecordFail -> mesRecordFail.setRepairFlag(1));
            mesRecordFailRepository.saveAll(mesRecordFails);
        }
        //获取产出工序
        MesPartRoute mesPartRoute = mesPartRouteRepository.findById(partRouteId).orElse(null);
        //工序是否是产出工序
        Boolean isOutputProcess = isOutputProcess(para.getProcessId(), mesPartRoute.getOutputProcessId());
        //更新  mes_record_wip_rec
        mesRecordWipRec.setInputQty(mesRecordWipRec.getOutputQty());
        mesRecordWipRec.setOutputQty(para.getOutputQty());
        mesRecordWipRec.setOutTime(new Date());
        mesRecordWipRec.setWipNowProcess(para.getProcessId());
        if(isOutputProcess){
            mesRecordWipRec.setWipNextProcess("");
            mesRecordWipRec.setNextProcessId("");
        }else {
            mesRecordWipRec.setWipNextProcess(nextProcessId);
            mesRecordWipRec.setNextProcessId(nextProcessId);
        }
        mesRecordWipRec.setStaffId(PadStaffUtil.getStaff().getStaffId());
        mesRecordWipRecRepository.save(mesRecordWipRec);
        //if(工序是否是产出工序)
        if(isOutputProcess){
            //更新工单（mes_mo_desc）的产出数（OutputQty
            MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(source).orElse(null);
            MesMoDesc mesMoDesc = mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
            Integer moQty = mesMoDesc.getOutputQty()==null?0:mesMoDesc.getOutputQty();
            mesMoDesc.setOutputQty(moQty+mesRecordWipRec.getOutputQty());
            //将当前工序放入日志，因为是最后一道工序
            MesRecordWipLog log = new MesRecordWipLog();
            copyData(mesRecordWipRec, log);
            log.setId(UUIDUtil.getUUID());
            mesRecordWipLogRepository.save(log);
        }

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
        //mesRecordWipRec.setOutTime(date);
        //职员（预留）

        //途程
        mesRecordWipRec.setRouteId(partRouteId);
        //下一工序
        mesRecordWipRec.setWipNextProcess(para.getProcessId());
        mesRecordWipRec.setNextProcessId(para.getProcessId());
        mesRecordWipRecRepository.save(mesRecordWipRec);

        CrossingStationModel crossingStationModel = new CrossingStationModel();
        //上一工序是否是机台自动扫描工序，是 算结余量
        if(BaseItemsTargetConstant.AUTO.equalsIgnoreCase(getCollection(beforeProcessId))){
            crossingStationModel.setSurplusQty(surplusQty);
        }
        //返回不良数0、及排产单信息
        crossingStationModel.setBarcode(para.getBarcode());
        crossingStationModel.setQty(0l);
        crossingStationModel.setOutputQty(mesRecordWipRec.getOutputQty());

        //上一工序的相关信息
        //StationRelationModel stationRelationModel = mesRecordWipRecRepository.getStationRelationModel(para.getBarcode());
        StationRelationModel stationRelationModel = getStationRelationModel(para.getBarcode(), source);
        crossingStationModel.setStationRelationModel(stationRelationModel);
        return ResponseMessage.ok(crossingStationModel);
    }

    /**
     * 获取关联信息
     * @param barcode
     * @param scheduleId
     * @return
     */
    private StationRelationModel getStationRelationModel(String barcode,String scheduleId){
        //获取排产单信息
        ScheduleAndPartsModel scheduleAndPartsModel = getScheduleAndPartsModel(scheduleId);
        //获取在制表信息
        MesRecordWipRec mesRecordWipRec = mesRecordWipRecRepository.findBySerialNumber(barcode);
        return getStationRelationModel(mesRecordWipRec,scheduleAndPartsModel);
    }

    /**
     * 获取关联信息
     * @param mesRecordWipRec
     * @param scheduleAndPartsModel
     * @return
     */
    private StationRelationModel getStationRelationModel(MesRecordWipRec mesRecordWipRec,ScheduleAndPartsModel scheduleAndPartsModel){
        StationRelationModel stationRelationModel = new StationRelationModel();
        //获取排产单信息
        try {
            BeanUtils.copyProperties(stationRelationModel,scheduleAndPartsModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置在制表信息
        //当前工序
        String wipNowProcess = mesRecordWipRec.getWipNowProcess();
        if(StringUtils.isNotEmpty(wipNowProcess)){
            BaseProcess baseProcess = baseProcessService.findById(wipNowProcess).orElse(null);
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
        Integer allInputQty = mesRecordWipLogRepository.getAllInputQty(source, processId);
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
        if(para.getProcessId().equalsIgnoreCase(mesRecordWipRec.getWipNowProcess())){
            throw new MMException("序号【"+para.getBarcode()+"】已扫描过站，不能重复扫描。");
        }
        //下一工序与本作业工序是否符合
        if(!para.getProcessId().equalsIgnoreCase(mesRecordWipRec.getWipNextProcess())){
            BaseProcess baseProcess = baseProcessService.findById(mesRecordWipRec.getWipNextProcess()).orElse(null);
            throw new MMException("工艺流程出错：该序号下一工序"+baseProcess.getProcessName());
        }
        //获取排产单id
        String source = barcodePrintApplyRepository.getSource(para.getBarcode());
        //默认不良为0
        crossingStationModel.setQty(0l);

        //上一工序的相关信息
        //StationRelationModel stationRelationModel = mesRecordWipRecRepository.getStationRelationModel(para.getBarcode());
        StationRelationModel stationRelationModel = getStationRelationModel(para.getBarcode(), source);
        crossingStationModel.setStationRelationModel(stationRelationModel);
        //默认为上一次的产出量
        crossingStationModel.setOutputQty(stationRelationModel.getOutputQty());

        //上一工序是否是机台自动扫描工序，是 算结余量
        if(BaseItemsTargetConstant.AUTO.equalsIgnoreCase(getCollection(mesRecordWipRec.getWipNowProcess()))){
            //获取结余量
            Integer surplusQty = getSurplusQty(para.getProcessId(), source,mesRecordWipRec.getWipNowProcess());
            crossingStationModel.setSurplusQty(surplusQty);
        }
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
