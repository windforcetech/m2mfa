package com.m2micro.m2mfa.base.service.impl;

import com.google.common.base.CaseFormat;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.starter.entity.Organization;
import com.m2micro.framework.starter.services.OrganizationService;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.entity.BaseStaffshift;
import com.m2micro.m2mfa.base.entity.QBaseStaff;
import com.m2micro.m2mfa.base.query.BaseStaffQuery;
import com.m2micro.m2mfa.base.repository.BaseStaffRepository;
import com.m2micro.m2mfa.base.repository.BaseStaffshiftRepository;
import com.m2micro.m2mfa.base.service.BaseStaffService;
import com.m2micro.m2mfa.base.vo.BaseStaffDetailObj;
import com.m2micro.m2mfa.base.vo.MesMoscheduleQueryObj;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工（职员）表 服务实现类
 *
 * @author liaotao
 * @since 2018-11-26
 */
@Service
public class BaseStaffServiceImpl implements BaseStaffService {
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    BaseStaffRepository baseStaffRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    OrganizationService organizationService;


    @Autowired
    BaseStaffshiftRepository baseStaffshiftRepository;
    public BaseStaffRepository getRepository() {
        return baseStaffRepository;
    }

//    @Override
//    public PageUtil<BaseStaffDetailObj> list1(BaseStaffQuery query) {
//        QBaseStaff qBaseStaff = QBaseStaff.baseStaff;
//        JPAQuery<BaseStaff> jq = queryFactory.selectFrom(qBaseStaff);
//        BooleanBuilder condition = new BooleanBuilder();
//        condition.and(qBaseStaff.deletionStateCode.isFalse());
//        if (StringUtils.isNotEmpty(query.getCode())) {
//            condition.and(qBaseStaff.code.like("%" + query.getCode() + "%"));
//        }
//        if (StringUtils.isNotEmpty(query.getName())) {
//            condition.and(qBaseStaff.staffName.like("%" + query.getName() + "%"));
//        }
//        if (query.getEnabled()==true) {
//            condition.and(qBaseStaff.enabled.eq(true));
//        }
//        if (query.getDepartmentIds() != null && query.getDepartmentIds().size() > 0) {
//            condition.and(qBaseStaff.departmentId.in(query.getDepartmentIds()));
//        }
//        jq.where(condition).offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
//        List<BaseStaff> list = jq.fetch();
//        List<BaseStaffDetailObj> rs = new ArrayList<>();
//        for (BaseStaff one : list) {
//            BaseStaffDetailObj item = new BaseStaffDetailObj();
//            Organization department = organizationService.findByUUID(one.getDepartmentId());
//            //  Organization department = organizationService.findById(duty.getParentNode()).get();
//            item.setId(one.getStaffId());
//            item.setCode(one.getCode());
//            if(department!=null){
//                item.setDepartment(department.getDepartmentName());
//            }
//                    item.setDimission(one.getDimission());
//            String sql = "select item_name from base_items_target where id='" + one.getDutyId() + "'";
//
//            item.setDuty(jdbcTemplate.queryForObject(sql, String.class));
//            item.setEnabled(one.getEnabled());
//            item.setGender(one.getGender());
//            item.setMobile(one.getMobile());
//            item.setName(one.getStaffName());
//            item.setTelephone(one.getTelephone());
//            rs.add(item);
//        }
//        long totalCount = jq.fetchCount();
//        return PageUtil.of(rs, totalCount, query.getSize(), query.getPage());
//    }
    @Override
    public PageUtil<BaseStaffDetailObj> list(BaseStaffQuery query) {
        String groupId = TokenInfo.getUserGroupId();
        String sql = "SELECT\n" +
                " sf.staff_id AS id,\n" +
                " sf.code AS code,\n" +
                " sf.staff_name AS name,\n" +
                " sf.gender AS gender,\n" +
                " sf.is_dimission AS isDimission,\n" +
                " sf.enabled AS enabled,\n" +
                " sf.mobile AS mobile,\n" +
                " sf.telephone AS telephone,\n" +
                " sf.id_card AS idCard,\n" +
                " it.item_name AS duty,\n" +
                " o.department_name AS department\n" +
                "FROM\n" +
                " base_staff sf\n" +
                "LEFT JOIN base_items_target it ON it.id = sf.duty_id\n" +
                "LEFT JOIN organization o ON (sf.department_id = o.uuid AND o.group_id = '"+groupId+"')"+
                "WHERE 1 = 1";
        sql = addSqlCondition(sql,query);
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseStaffDetailObj.class);
        List<BaseStaffDetailObj> list = jdbcTemplate.query(sql,rm);
        String countSql = "select count(sf.staff_id) from base_staff sf where 1=1 ";
        countSql = addSqlCondition(countSql,query);
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return  PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    private String addSqlCondition(String sql, BaseStaffQuery query) {
        if(StringUtils.isNotEmpty(query.getCode())){
            sql = sql+" and sf.code like '%"+query.getCode()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getName())){
            sql = sql+" and sf.staff_name like '%"+query.getName()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getDutyId())){
            sql = sql+" and sf.duty_id = '"+query.getDutyId()+"'";
        }
        if(StringUtils.isNotEmpty(query.getIdCard())){
            sql = sql+" and sf.id_card like '%"+query.getIdCard()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getEmail())){
            sql = sql+" and sf.email like '%"+query.getEmail()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getMobile())){
            sql = sql+" and sf.mobile like '%"+query.getMobile()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getTelephone())){
            sql = sql+" and sf.telephone like '%"+query.getTelephone()+"%'";
        }
        if(query.getIsDimission()!=null){
            sql = sql+" and sf.is_dimission = "+query.getIsDimission()+"";
        }
        if(StringUtils.isNotEmpty(query.getIcCard())){
            sql = sql+" and sf.ic_card = '"+query.getIcCard()+"'";
        }
        if (query.getDepartmentIds() != null && query.getDepartmentIds().size() > 0) {
            String collect = query.getDepartmentIds().stream().collect(Collectors.joining("','", "'", "'"));
            sql = sql+" and sf.department_id IN ("+collect+")";
        }
        if(query.getEnabled()!=null){
            sql = sql+" and sf.enabled = "+query.getEnabled()+"";
        }
        String groupId = TokenInfo.getUserGroupId();
        sql = sql+" and sf.group_id = '"+groupId+"'";
        //排序字段(驼峰转换)
        String order = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, StringUtils.isEmpty(query.getOrder())?"modified_on":query.getOrder());
        //特殊排序处理（非主表的字段）
        switch (order) {
        	case "name":
                order="staff_name";
        		break;
            case "department":
                order="department_id";
                break;
            case "duty":
                order="duty_id";
                break;
        	default:
        		break;
        	}
        //排序方向
        String direct = StringUtils.isEmpty(query.getDirect())?"desc":query.getDirect();
        sql = sql + " order by sf."+order+" "+direct+",sf.modified_on desc";
        return sql;
    }


    @Override
    public List<BaseStaff> productionlist(MesMoscheduleQueryObj baseStaffQueryObj) {
        String groupId = TokenInfo.getUserGroupId();
        RowMapper rm = BeanPropertyRowMapper.newInstance(BaseStaff.class);
        String sql = "SELECT " +
                " bs.*  " +
                " FROM base_staffshift bss " +
                " LEFT JOIN base_staff bs ON (bss.staff_id = bs.staff_id AND bs.group_id = '" +groupId+"')"+
                " WHERE bss.shift_id = '" + baseStaffQueryObj.getShiftId() + "'"+
                " AND bss.shift_date = '" + baseStaffQueryObj.getShiftDate() + "'"+
                " AND bss.group_id = '"+groupId+"'";
        return  jdbcTemplate.query(sql,rm);
    }

    @Override
    public List<BaseStaff> findByCodeAndStaffIdNot(String code, String staffId) {
        return baseStaffRepository.findByCodeAndGroupIdAndStaffIdNot(code,TokenInfo.getUserGroupId(),staffId);
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
        if(org!=null){
            getDepartmentidsCore(org.getId(), all, rs);
        }
        return rs;
    }

    //    @Override
//    public List<BaseStaff> findByCodeOrStaffNameOrdOrDutyIdIn(String code, String staffName, List<String> dutyIds) {
//        return baseStaffRepository.findByCodeOrStaffNameOrdOrDutyIdIn(code,staffName,dutyIds);
//    }
    @Override
    public Boolean isUsedForStaff(String[] ids) {
        String sql = "SELECT count(*) FROM mes_record_staff where staff_id in(:ids);";
        HashMap<String, Object> args = new HashMap<>();
        ArrayList<String> mids = new ArrayList<>();
        for (String one : ids) {
            mids.add(one);
        }
        args.put("ids", args);
        NamedParameterJdbcTemplate givenParamJdbcTemp = new NamedParameterJdbcTemplate(jdbcTemplate);
        Long count = givenParamJdbcTemp.queryForObject(sql, args, Long.class);
        int shift=baseStaffshiftRepository.countByStaffIdIn(ids);
        return count > 0 ||shift>0;
    }

    @Override
    public BaseStaff findByCode(String code) {
        return baseStaffRepository.findByCode(code);
    }

    @Override
    public boolean existByIcCard(String icCard) {

        return baseStaffRepository.countByIcCardAndGroupId(icCard,TokenInfo.getUserGroupId())>0;
    }

    @Override
    public boolean existByIcCardAndIdNot(String icCard, String id) {
        return baseStaffRepository.countByIcCardAndGroupIdAndStaffIdNot(icCard,TokenInfo.getUserGroupId(),id)>0;
    }

    @Transactional
    @Override
    public ResponseMessage deleteByStaffId(String[] ids) {

        List<BaseStaff> enableDelete = new ArrayList<>();
        List<BaseStaff> disableDelete = new ArrayList<>();
        for( String id :  ids ){
            BaseStaff baseStaff = baseStaffRepository.findById(id).orElse(null);
            List<BaseStaffshift> list = baseStaffshiftRepository.findByStaffId(id);
            if(list!=null&&list.size()>0){
                disableDelete.add(baseStaff);
                continue;
                //throw new MMException("物料编号【"+bp.getPartNo()+"】已产生业务,不允许删除！");
            }
            enableDelete.add(baseStaff);
        }

        deleteAll(enableDelete);
        ResponseMessage re =   ResponseMessage.ok("操作成功");
        if(disableDelete.size()>0){
            String[] strings = disableDelete.stream().map(BaseStaff::getCode).toArray(String[]::new);
            re.setMessage("职员编号【"+String.join(",", strings)+"】已产生业务,不允许删除！");
            return re;
        }else{
            return re;
        }
    }
}
