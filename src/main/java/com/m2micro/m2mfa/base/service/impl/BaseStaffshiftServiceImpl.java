package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.m2mfa.base.entity.BaseStaffshift;
import com.m2micro.m2mfa.base.query.BaseStaffshiftQuery;
import com.m2micro.m2mfa.base.repository.BaseStaffshiftRepository;
import com.m2micro.m2mfa.base.service.BaseStaffshiftService;
import com.m2micro.m2mfa.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseStaffshift;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    JdbcTemplate jdbcTemplate;

    public BaseStaffshiftRepository getRepository() {
        return baseStaffshiftRepository;
    }

    @Override
    public PageUtil<Map<String, Object>> list(BaseStaffshiftQuery query) {
        //QBaseStaffshift qBaseStaffshift = QBaseStaffshift.baseStaffshift;
        //JPAQuery<BaseStaffshift> jq = queryFactory.selectFrom(qBaseStaffshift);


        String sql = " SELECT zh.* , base_staff.staff_name    from     (SELECT staff_id, CAST( GROUP_CONCAT( shift_date, ';', shift_id, ';',shift_name ORDER BY shift_date ASC SEPARATOR '|' ) AS CHAR (10000) CHARACTER SET utf8 ) as keyvalue FROM  (SELECT staff_id, shift_date,base_shift.shift_id, base_shift.`name` as shift_name  from base_staffshift,base_shift where staff_id in (SELECT staff_id FROM `base_staff` where 1=1 ";
        if (StringUtils.isNotEmpty(query.getDepartmentID()))
            sql += "  and department_id='" + query.getDepartmentID() + "'";
        sql += ") AND shift_date >= '" + DateUtil.format(query.getStartTime()) + "' AND shift_date <= '" + DateUtil.format(query.getEndTime()) + "' and base_shift.shift_id =base_staffshift.shift_id) as tk  GROUP BY staff_id ) as zh , base_staff where base_staff.staff_id=zh.staff_id;";
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


}