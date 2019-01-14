package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.starter.entity.Organization;
import com.m2micro.framework.starter.services.OrganizationService;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.entity.QBaseStaff;
import com.m2micro.m2mfa.base.query.BaseStaffQuery;
import com.m2micro.m2mfa.base.repository.BaseStaffRepository;
import com.m2micro.m2mfa.base.service.BaseStaffService;
import com.m2micro.m2mfa.base.vo.BaseStaffDetailObj;
import com.m2micro.m2mfa.base.vo.BaseStaffQueryObj;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工（职员）表 服务实现类
 *
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BaseStaffServiceImpl implements BaseStaffService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    BaseStaffRepository baseStaffRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    OrganizationService organizationService;

    public BaseStaffRepository getRepository() {
        return baseStaffRepository;
    }

    @Override
    public PageUtil<BaseStaffDetailObj> list(BaseStaffQuery query) {
        QBaseStaff qBaseStaff = QBaseStaff.baseStaff;
        JPAQuery<BaseStaff> jq = queryFactory.selectFrom(qBaseStaff);
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(qBaseStaff.deletionStateCode.isFalse());
        if (StringUtils.isNotEmpty(query.getCode())) {
            condition.and(qBaseStaff.code.like("%" + query.getCode() + "%"));
        }
        if (StringUtils.isNotEmpty(query.getName())) {
            condition.and(qBaseStaff.staffName.like("%" + query.getName() + "%"));
        }
        if (query.getDepartmentIds() != null && query.getDepartmentIds().size() > 0) {
            condition.and(qBaseStaff.departmentId.in(query.getDepartmentIds()));
        }
        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseStaff> list = jq.fetch();
        List<BaseStaffDetailObj> rs = new ArrayList<>();
        for (BaseStaff one : list) {
            BaseStaffDetailObj item = new BaseStaffDetailObj();
            Organization department = organizationService.findByUUID(one.getDepartmentId());
            //  Organization department = organizationService.findById(duty.getParentNode()).get();
            item.setId(one.getStaffId());
            item.setCode(one.getCode());
            item.setDepartment(department.getDepartmentName());
            item.setDimission(one.getDimission());
            String sql = "select item_name from base_items_target where id='" + one.getDutyId() + "'";

            item.setDuty(jdbcTemplate.queryForObject(sql, String.class));
            item.setEnabled(one.getEnabled());
            item.setGender(one.getGender());
            item.setMobile(one.getMobile());
            item.setName(one.getStaffName());
            item.setTelephone(one.getTelephone());
            rs.add(item);
        }
        long totalCount = jq.fetchCount();
        return PageUtil.of(rs, totalCount, query.getSize(), query.getPage());
    }

    @Override
    public PageUtil<BaseStaffDetailObj> productionlist(BaseStaffQueryObj baseStaffQueryObj) {
        BaseStaffQuery query = new BaseStaffQuery();
        query.setCode(baseStaffQueryObj.getCode());
        query.setName(baseStaffQueryObj.getName());
        if (StringUtils.isNotEmpty(baseStaffQueryObj.getDepartmentId())) {

            List<String> departmentIds = this.getAllIDsOfDepartmentTree(baseStaffQueryObj.getDepartmentId());
            //   List<String> obtainpost = organizationService.obtainpost(baseStaffQueryObj.getDepartmentId());
            query.setDepartmentIds(departmentIds);
//            List<String> obtainpost = organizationService.obtainpost(baseStaffQueryObj.getDepartmentId());
//            query.setDutyIds(obtainpost);
        }
        query.setSize(baseStaffQueryObj.getSize());
        query.setPage(baseStaffQueryObj.getPage());
        return this.list(query);
    }

    @Override
    public List<BaseStaff> findByCodeAndStaffIdNot(String code, String staffId) {
        return baseStaffRepository.findByCodeAndStaffIdNot(code, staffId);
    }

    @Override
    public BaseStaff finydbStaffNo(String code) {

        return baseStaffRepository.finydbStaffNo(code);
    }

    private void getDepartmentidsCore(long id, List<Organization> orgs, List<String> rs) {
        for (Organization org : orgs) {
            if (org.getParentNode() == id) {
                if (!rs.contains(org.getUuid())) {
                    rs.add(org.getUuid());
                }
                getDepartmentidsCore(org.getId(), orgs, rs);
            }
        }
    }


    public List<String> getAllIDsOfDepartmentTree(String departmentId) {
        ArrayList<String> rs = new ArrayList<>();
        List<Organization> all = organizationService.findAll();
        Organization org = organizationService.findByUUID(departmentId);
        rs.add(departmentId);
        getDepartmentidsCore(org.getId(), all, rs);

        return rs;

    }

//    @Override
//    public List<BaseStaff> findByCodeOrStaffNameOrdOrDutyIdIn(String code, String staffName, List<String> dutyIds) {
//        return baseStaffRepository.findByCodeOrStaffNameOrdOrDutyIdIn(code,staffName,dutyIds);
//    }

}