package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.entity.BaseShift;
import com.m2micro.m2mfa.base.repository.BaseShiftRepository;
import com.m2micro.m2mfa.base.service.BaseShiftService;
import com.m2micro.m2mfa.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseShift;

import java.util.Date;
import java.util.List;
/**
 * 班别基本资料 服务实现类
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BaseShiftServiceImpl implements BaseShiftService {
    @Autowired
    BaseShiftRepository baseShiftRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public BaseShiftRepository getRepository() {
        return baseShiftRepository;
    }

    /*@Override
    public PageUtil<BaseShift> list(Query query) {
        QBaseShift qBaseShift = QBaseShift.baseShift;
        JPAQuery<BaseShift> jq = queryFactory.selectFrom(qBaseShift);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseShift> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }*/

    @Override
    public PageUtil<BaseShift> list(Query query) {
        String sql = "SELECT\n" +
                        "	bs.shift_id shiftId,\n" +
                        "	bs.code code,\n" +
                        "	bs.name name,\n" +
                        "	bs.category category,\n" +
                        "	bs.on_time1 onTime1,\n" +
                        "	bs.off_time1 offTime1,\n" +
                        "	bs.rest_time1 restTime1,\n" +
                        "	bs.time_category1 timeCategory1,\n" +
                        "	bs.on_time2 onTime2,\n" +
                        "	bs.off_time2 offTime2,\n" +
                        "	bs.rest_time2 restTime2,\n" +
                        "	bs.time_category2 timeCategory2,\n" +
                        "	bs.on_time3 onTime3,\n" +
                        "	bs.off_time3 offTime3,\n" +
                        "	bs.rest_time3 restTime3,\n" +
                        "	bs.time_category3 timeCategory3,\n" +
                        "	bs.on_time4 onTime4,\n" +
                        "	bs.off_time4 offTime4,\n" +
                        "	bs.rest_time4 restTime4,\n" +
                        "	bs.time_category4 timeCategory4,\n" +
                        "	bs.enabled enabled,\n" +
                        "	bs.description description,\n" +
                        "	bs.create_on createOn,\n" +
                        "	bs.create_by createBy,\n" +
                        "	bs.modified_on modifiedOn,\n" +
                        "	bs.modified_by modifiedBy,\n" +
                        "	bi.item_name categoryName\n" +
                        "FROM\n" +
                        "	base_shift bs\n" +
                        "LEFT JOIN base_items_target bi ON bi.id = bs.category\n" +
                        "WHERE\n" +
                        "	1 = 1";
        sql = sql + " order by bs.modified_on desc";
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseShift.class);
        List<BaseShift> list = jdbcTemplate.query(sql,rm);
        String countSql = "select count(*) from base_shift";
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);

        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public List<BaseShift> findByCodeAndShiftIdNot(String code, String shiftId) {
        return baseShiftRepository.findByCodeAndShiftIdNot(code, shiftId);
    }

    @Override
    public long findbhours(String shiftId) {
        BaseShift baseShift = this.findById(shiftId).orElse(null);
        Date OnTime1 = DateUtil.stringToDate( baseShift.getOnTime1(), DateUtil.TIME_PATTERN);
        Date OnTime2 = DateUtil.stringToDate( baseShift.getOnTime2(), DateUtil.TIME_PATTERN);
        Date OnTime3 = DateUtil.stringToDate( baseShift.getOnTime3(), DateUtil.TIME_PATTERN);
        Date OnTime4 = DateUtil.stringToDate( baseShift.getOnTime4(), DateUtil.TIME_PATTERN);
        Date OffTime1 = DateUtil.stringToDate( baseShift.getOffTime1(), DateUtil.TIME_PATTERN);
        Date OffTime2 = DateUtil.stringToDate( baseShift.getOffTime2(), DateUtil.TIME_PATTERN);
        Date OffTime3 = DateUtil.stringToDate( baseShift.getOffTime3(), DateUtil.TIME_PATTERN);
        Date OffTime4 = DateUtil.stringToDate( baseShift.getOffTime4(), DateUtil.TIME_PATTERN);
        long time1 =  subtraction(OffTime1.getTime(),OnTime1.getTime());
        long time2 =  subtraction(OffTime2.getTime(),OnTime2.getTime());
        long time3 =  subtraction(OffTime3.getTime(),OnTime3.getTime());
        long time4 =  subtraction(OffTime4.getTime(),OnTime4.getTime());
        long time = (time1+time2+time3+time4);
        return time==0 ? time : time/1000;
    }

    /**
     * 时间戳相减
     * @param onTime
     * @param offTime
     * @return
     */
    public long subtraction(long onTime ,long offTime){

        return onTime-offTime;
    }



}
