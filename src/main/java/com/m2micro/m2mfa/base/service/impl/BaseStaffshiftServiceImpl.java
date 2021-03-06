package com.m2micro.m2mfa.base.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.m2mfa.base.entity.BaseShift;
import com.m2micro.m2mfa.base.entity.BaseStaffshift;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.query.BaseStaffshiftQuery;
import com.m2micro.m2mfa.base.repository.BaseStaffshiftRepository;
import com.m2micro.m2mfa.base.service.BaseStaffshiftService;
import com.m2micro.m2mfa.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseStaffshift;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工排班表 服务实现类
 *
 * @author liaotao
 * @since 2019-01-04
 */
@Service
public class BaseStaffshiftServiceImpl implements BaseStaffshiftService {
    @Autowired
    BaseStaffshiftRepository baseStaffshiftRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public BaseStaffshiftRepository getRepository() {
        return baseStaffshiftRepository;
    }

    @Override
    public PageUtil<Map<String, Object>> list(BaseStaffshiftQuery query) {
        //QBaseStaffshift qBaseStaffshift = QBaseStaffshift.baseStaffshift;
        //JPAQuery<BaseStaffshift> jq = queryFactory.selectFrom(qBaseStaffshift);

        String groupId = TokenInfo.getUserGroupId();
        String sql = " SELECT zh.* , base_staff.staff_name AS staffName , base_staff.code   from     (SELECT staff_id, CAST( GROUP_CONCAT( shift_date, ';', id, ';',shift_code ORDER BY shift_date ASC SEPARATOR '|' ) AS CHAR (30000) CHARACTER SET utf8 ) as keyvalue FROM  (SELECT base_staffshift.id,staff_id, shift_date,base_shift.shift_id, base_shift.`name` as shift_name,base_shift.`code` As shift_code  from base_staffshift,base_shift where staff_id in (SELECT staff_id FROM `base_staff` where 1=1 ";
        if (StringUtils.isNotEmpty(query.getDepartmentID())){
            sql += "  and department_id='" + query.getDepartmentID() + "'";
        }
        if (StringUtils.isNotEmpty(query.getStaffId())){
            sql += " and staff_id='" + query.getStaffId() + "'";
        }
        sql += "AND group_id='" + groupId + "') AND shift_date >= '" + DateUtil.format(query.getStartTime()) + "' AND shift_date <= '" + DateUtil.format(query.getEndTime()) + "' and base_shift.shift_id =base_staffshift.shift_id and base_shift.group_id='" + groupId + "' and base_staffshift.group_id='" + groupId + "'";
        if(StringUtils.isNotEmpty(query.getShiftId())){
        sql +=" AND base_shift.shift_id='"+query.getShiftId()+"'";
        }
        sql +=") as tk  GROUP BY staff_id ) as zh , base_staff where base_staff.staff_id=zh.staff_id and base_staff.group_id='" + groupId + "'";
        if(StringUtils.isNotEmpty(query.getStaffName())){
            sql +=" AND base_staff.staff_name LIKE '%"+query.getStaffName()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getCode())){
            sql +=" AND base_staff.`code` LIKE '%"+query.getCode()+"%'";
        }
        //排序字段
        String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder())?"modified_on":query.getOrder());
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect())?"desc":query.getDirect();
        sql = sql + " order by base_staff."+order+" "+direct+",base_staff.modified_on desc ";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);



        List<Map<String, Object>> collect = list.stream()
                .skip((query.getPage() - 1) * query.getSize()).limit(query.getSize())
                .map(item -> {
                    Object value = item.get("keyvalue");
                    String[] banbie = String.valueOf(value).split("\\|");
                    List<Map<String, String>> mapList = Arrays.stream(banbie)
                            .map(item1 -> {
                                String[] ev = item1.split(";");
                                Map<String, String> stringStringHashMap = new HashMap<>();
                                stringStringHashMap.put("time", ev[0]);
                                stringStringHashMap.put("id", ev[1]);
                                stringStringHashMap.put("name", ev[2]);
                                return stringStringHashMap;
                            }).collect(Collectors.toList());
                    item.put("keyvalue", mapList);
                    return item;
                })
                .collect(Collectors.toList());


        //jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        //List<BaseStaffshift> list = jq.fetch();
        //long totalCount = jq.fetchCount();

//        String countSql = "select count(*) from base_staff where 1=1 ";
//        if (StringUtils.isNotEmpty(query.getDepartmentID()))
//            countSql += "  and department_id='" + query.getDepartmentID() + "'";
//        long totalCount = jdbcTemplate.queryForObject(countSql, long.class);
        return PageUtil.of(collect, list.size(), query.getSize(), query.getPage());


    }

    @Override
    public List<BaseStaffshift> saveSome(List<BaseStaffshift> entities) {
        return null;
    }

    @Override
    public List<BaseShift> findByStaffIdAndShiftDate(String staffId, Date shiftDate) {

        if(shiftDate==null||StringUtils.isEmpty(staffId)){
            return null;
        }
        String format = DateUtil.format(shiftDate, DateUtil.DATE_PATTERN);
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
                "	bs.modified_by modifiedBy\n" +
                "FROM\n" +
                "	base_staffshift bss,\n" +
                "	base_shift bs\n" +
                "WHERE\n" +
                "	bss.shift_id = bs.shift_id\n" +
                "AND bss.staff_id = '" + staffId + "'\n" +
                "AND bss.shift_date = '" + format + "'\n" ;
        RowMapper<BaseShift> rm = BeanPropertyRowMapper.newInstance(BaseShift.class);
        return jdbcTemplate.query(sql, rm);
    }

    @Override
    public String[] findCanDelete(String[] ids) {
        String groupId = TokenInfo.getUserGroupId();
        // String sql = "SELECT b.id, b.staff_id, b.shift_id, b.shift_date, m.plan_start_time, m.plan_end_time FROM base_staffshift b, mes_mo_schedule_staff m WHERE b.shift_date BETWEEN m.plan_start_time AND m.plan_end_time AND m.shift_id = b.shift_id AND m.staff_id = b.staff_id AND b.id IN (:ids)";
        String sql = "SELECT b.id FROM base_staffshift b, mes_mo_schedule_staff m, mes_mo_schedule mms WHERE  b.shift_date BETWEEN mms.plan_start_time AND mms.plan_end_time AND m.shift_id = b.shift_id AND m.staff_id = b.staff_id AND mms.schedule_id = m.schedule_id AND b.id IN (:ids) ";
        HashMap<String, Object> args = new HashMap<>();
//        ArrayList<String> mids = new ArrayList<>();
//        for (String one : ids) {
//            mids.add(one);
//        }
//        args.put("ids", mids);
        args.put("ids", Arrays.asList(ids));
        sql +=" AND b.group_id='"+groupId+"' AND m.group_id='"+groupId+"' AND mms.group_id='"+groupId+"' GROUP BY id";
        NamedParameterJdbcTemplate givenParamJdbcTemp = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> list = givenParamJdbcTemp.queryForList(sql, args);
        String[] ids1 = list.stream()
                .map(item -> String.valueOf(item.get("id")))
                .toArray(String[]::new);

        return ids1;
    }


}
