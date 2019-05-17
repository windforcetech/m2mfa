package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.pad.model.StationRelationModel;
import com.m2micro.m2mfa.record.entity.MesRecordWipRec;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 在制记录表 Repository 接口
 * @author liaotao
 * @since 2019-01-02
 */
@Repository
public interface MesRecordWipRecRepository extends BaseRepository<MesRecordWipRec,String> {

    /**
     * 获取在制信息
     * @param serialNumber
     * @return
     */
    MesRecordWipRec findBySerialNumber(String serialNumber);


    /**
     * 获取在制关联信息
     * @param serialNumber
     * @return
     */
    /*@Query(value = "SELECT\n" +
            "	mms.schedule_no scheduleNo,\n" +
            "	bpt.part_no partNo,\n" +
            "	bpt.name partName,\n" +
            "	bpt.spec partSpec,\n" +
            "	bp1.process_name wipNowProcessName,\n" +
            "	mrwr.out_time outTime,\n" +
            "	mrwr.staff_id staffId,\n" +
            "	mrwr.output_qty outputQty,\n" +
            "	bit.item_name itemName,\n" +
            "	bp2.process_name wipNextProcessName\n" +
            "FROM\n" +
            "	mes_record_wip_rec mrwr,\n" +
            "	base_process bp1,\n" +
            "	base_process bp2,\n" +
            "	mes_mo_schedule mms,\n" +
            "	base_parts bpt,\n" +
            "	base_items_target bit\n" +
            "WHERE\n" +
            "	mrwr.wip_now_process = bp1.process_id \n" +
            "	AND mrwr.wip_next_process = bp2.process_id \n" +
            "	AND mrwr.schedule_id = mms.schedule_id \n" +
            "	AND mms.part_id = bpt.part_id\n" +
            "	AND bp2.collection=bit.id\n" +
            "	AND mrwr.serial_number=?1",nativeQuery = true)*/
    //StationRelationModel getStationRelationModel(String serialNumber);

    /**
     * 获取在制信息
     * @return
     */
    List<MesRecordWipRec> findByNextProcessIdAndScheduleId(String processId,String scheduleId);

    /**
     * 获取产出
     * @param scheduleId
     * @param processId
     * @return
     */
    @Query("select sum(outputQty) from MesRecordWipRec where scheduleId=?1 and wipNowProcess=?2")
    Integer getAllOutputQty(String scheduleId,String processId);


    @Query("select sum(inputQty) from MesRecordWipRec where scheduleId=?1")
    Integer getAllInputQty(String scheduleId);
}