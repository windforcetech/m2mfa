package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.barcode.entity.BarcodePrintResources;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintApplyRepository;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintResourcesRepository;
import com.m2micro.m2mfa.barcode.service.BarcodePrintResourcesService;
import com.m2micro.m2mfa.base.constant.BaseItemsTargetConstant;
import com.m2micro.m2mfa.base.entity.BaseItemsTarget;
import com.m2micro.m2mfa.base.entity.BasePack;
import com.m2micro.m2mfa.base.entity.BaseProcess;
import com.m2micro.m2mfa.base.repository.BasePackRepository;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import com.m2micro.m2mfa.base.service.BaseProcessService;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.QueryGroup;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.constant.StationConstant;
import com.m2micro.m2mfa.pad.model.*;
import com.m2micro.m2mfa.pad.service.PadBottomDisplayService;
import com.m2micro.m2mfa.pad.service.PadCrossingStationService;
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
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<WipRecModel> pullIn(String processId) {
        List<MesRecordWipRec> mesRecordWipRecs = mesRecordWipRecRepository.findByNextProcessId(processId);
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
        MesPartRouteProcess nextProcessid = mesPartRouteProcessRepository.findByPartrouteidAndProcessid(partRouteId, para.getProcessId());

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
        //拷贝数据到历史记录
        MesRecordWipLog mesRecordWipRecLog = new MesRecordWipLog();
        try {
            BeanUtils.copyProperties(mesRecordWipRecLog,mesRecordWipRec);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MMException("在制记录异常");
        }
        mesRecordWipRecLog.setId(UUIDUtil.getUUID());
        mesRecordWipLogRepository.save(mesRecordWipRecLog);
        //更新已处理的不良记录状态为1.
        if(para.getIds()!=null&&para.getIds().size()>0){
            List<MesRecordFail> mesRecordFails = mesRecordFailRepository.findAllById(para.getIds());
            mesRecordFails.stream().forEach(mesRecordFail -> mesRecordFail.setRepairFlag(1));
            mesRecordFailRepository.saveAll(mesRecordFails);
        }
        //更新  mes_record_wip_rec
        mesRecordWipRec.setInputQty(mesRecordWipRec.getOutputQty());
        mesRecordWipRec.setOutputQty(para.getOutputQty());
        mesRecordWipRec.setOutTime(new Date());
        mesRecordWipRec.setWipNowProcess(para.getProcessId());
        mesRecordWipRec.setWipNextProcess(nextProcessid.getNextprocessid());
        mesRecordWipRecRepository.save(mesRecordWipRec);
        //获取产出工序
        //if(工序是否是产出工序)
            //更新工单（mes_mo_desc）的产出数（OutputQty
        MesPartRoute mesPartRoute = mesPartRouteRepository.findById(partRouteId).orElse(null);
        if(para.getProcessId().equalsIgnoreCase(mesPartRoute.getOutputProcessId())){
            MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(source).orElse(null);
            MesMoDesc mesMoDesc = mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
            Integer moQty = mesMoDesc.getOutputQty()==null?0:mesMoDesc.getOutputQty();
            mesMoDesc.setOutputQty(moQty+mesRecordWipRec.getOutputQty());
        }

    }

    /**
     * 处理没有在制信息
     * @param para
     * @return
     */
    private ResponseMessage<CrossingStationModel> handForNotMesRecordWipRec(CrossingStationPara para) {
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
        //获取结余量
        Integer surplusQty = getSurplusQty(para.getProcessId(), source);
        //投入数量，产出数量：包装量小于结余量，以包装量为准，否则以结余量为主
        Integer packQty = basePack.getQty().intValue();
        //包装量小于结余量
        if(packQty<surplusQty){
            mesRecordWipRec.setInputQty(packQty);
            mesRecordWipRec.setOutputQty(packQty);
        }else{
            mesRecordWipRec.setInputQty(surplusQty);
            mesRecordWipRec.setOutputQty(surplusQty);
        }
        //上一个工序
        MesPartRouteProcess mesPartRouteProcess = mesPartRouteProcessRepository.findByPartrouteidAndNextprocessid(partRouteId, para.getProcessId());
        mesRecordWipRec.setProcessId(mesPartRouteProcess.getProcessid());
        mesRecordWipRec.setWipNowProcess(mesPartRouteProcess.getProcessid());

        Date date = new Date();
        mesRecordWipRec.setInTime(date);
        mesRecordWipRec.setInlineTime(date);
        //职员（预留）

        //途程
        mesRecordWipRec.setRouteId(partRouteId);
        //下一工序
        mesRecordWipRec.setWipNextProcess(para.getProcessId());
        mesRecordWipRecRepository.save(mesRecordWipRec);

        CrossingStationModel crossingStationModel = new CrossingStationModel();
        //上一工序是否是机台自动扫描工序，是 算结余量
        if(BaseItemsTargetConstant.SCAN.equalsIgnoreCase(getCollection(mesPartRouteProcess.getProcessid()))){
            crossingStationModel.setSurplusQty(surplusQty);
        }
        //返回不良数0、及排产单信息
        crossingStationModel.setBarcode(para.getBarcode());
        crossingStationModel.setQty(0l);
        crossingStationModel.setOutputQty(mesRecordWipRec.getOutputQty());
        //上一工序的相关信息
        StationRelationModel stationRelationModel = mesRecordWipRecRepository.getStationRelationModel(para.getBarcode());
        crossingStationModel.setStationRelationModel(stationRelationModel);
        return ResponseMessage.ok(crossingStationModel);
    }

    /**
     * 获取结余量
     * @param processId
     * @param source
     * @return
     */
    private Integer getSurplusQty(String processId, String source) {
        //1.获取排产单的完成量
        List scheduleIds = new ArrayList();
        scheduleIds.add(source);
        Integer outPutQtys = padBottomDisplayService.getOutPutQtys(source, scheduleIds);
        //2.当前工序当前排产单的投入数之和
        Integer allInputQty = mesRecordWipLogRepository.getAllInputQty(source, processId);
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
        //当前工序当前排产单的不良记录
        List<MesRecordFail> mesRecordFails = mesRecordFailRepository.getByProcessIdAndScheduleId(source, para.getProcessId());
        //获取不良记录数
        Long qty = getQtyForFail(mesRecordFails);
        if(qty>0){
            crossingStationModel.setQty(qty);
            //获取不良记录id
            List<String> ids = mesRecordFails.stream().map(MesRecordFail::getId).collect(Collectors.toList());
            crossingStationModel.setIds(ids);
        }else{
            crossingStationModel.setQty(0l);
        }
        //上一工序的相关信息
        StationRelationModel stationRelationModel = mesRecordWipRecRepository.getStationRelationModel(para.getBarcode());
        crossingStationModel.setStationRelationModel(stationRelationModel);
        //默认为上一次的产出量
        crossingStationModel.setOutputQty(stationRelationModel.getOutputQty());

        /*//判定当前工序是否是机台自动采集
        String collection = getCollection(para);
        //获取结余量*/
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





}
