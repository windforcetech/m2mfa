package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.barcode.entity.BarcodePrintApply;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintApplyRepository;
import com.m2micro.m2mfa.base.entity.BasePartTemplate;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.query.BasePartTemplateQuery;
import com.m2micro.m2mfa.base.repository.BasePartTemplateRepository;
import com.m2micro.m2mfa.base.service.BasePartTemplateService;
import com.m2micro.m2mfa.base.service.BaseTemplateService;
import com.m2micro.m2mfa.base.vo.BasePartTemplateObj;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 料件模板关联表 服务实现类
 *
 * @author liaotao
 * @since 2019-03-06
 */
@Service
public class BasePartTemplateServiceImpl implements BasePartTemplateService {
    @Autowired
    BasePartTemplateRepository basePartTemplateRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    BarcodePrintApplyRepository barcodePrintApplyRepository;
    @Autowired
    BaseTemplateService baseTemplateService;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public BasePartTemplateRepository getRepository() {
        return basePartTemplateRepository;
    }

    @Override
    public PageUtil<BasePartTemplateObj> list(BasePartTemplateQuery query) {


        String sqlCount = "SELECT count(*) \n" +
                " FROM  base_part_template a,base_parts b,base_template c,base_items_target d\n" +
                "where a.part_id=b.part_id\n" +
                "and a.template_id=c.id\n" +
                "and c.category=d.id\n";
        String sql = "SELECT a.*,\n" +
                "b.part_no,b.name part_name,\n" +
                "d.item_name category,\n" +
                "c.name template_name,\n" +
                "c.version template_version\n" +
                " FROM  base_part_template a,base_parts b,base_template c,base_items_target d\n" +
                "where a.part_id=b.part_id\n" +
                "and a.template_id=c.id\n" +
                "and c.category=d.id\n";
//                "and b.part_no like '%%'\n" +
//                "and c.name like '%%'\n" +
//                "order by b.part_no \n" +
//                "limit m,n";
        if (query.getPartNumber() != null && query.getPartNumber() != "") {
            sql += "and b.part_no like '%" + query.getPartNumber() + "%'\n";
            sqlCount += "and b.part_no like '%" + query.getPartNumber() + "%'\n";
        }
        if (query.getTemplateName() != null && query.getTemplateName() != "") {
            sql += "and c.name like '%" + query.getTemplateName() + "%'\n";
            sqlCount += "and c.name like '%" + query.getTemplateName() + "%'\n";
        }

        Long totalCount = jdbcTemplate.queryForObject(sqlCount, Long.class);
        sql += "order by b.part_no \n";
        sql += "limit " + (query.getPage() - 1) * query.getSize() + "," + query.getSize() + ";";
        RowMapper<BasePartTemplateObj> rowMapper = BeanPropertyRowMapper.newInstance(BasePartTemplateObj.class);
        List<BasePartTemplateObj> list = jdbcTemplate.query(sql, rowMapper);
//        QBasePartTemplate qBasePartTemplate = QBasePartTemplate.basePartTemplate;
//        JPAQuery<BasePartTemplate> jq = queryFactory.selectFrom(qBasePartTemplate);
//
//        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
//        List<BasePartTemplate> list = jq.fetch();
//        long totalCount = jq.fetchCount();
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

    @Override
    public ResponseMessage delete(String[] ids) {

      List<String>deleids = new ArrayList<>();
      List<String>deletemsg = new ArrayList<>();
      for(String id :ids){
          BasePartTemplate basePartTemplate = findById(id).orElse(null);
          List<BarcodePrintApply> byTemplateId = barcodePrintApplyRepository.findByTemplateId(basePartTemplate.getTemplateId());
          if(!byTemplateId.isEmpty()){
              deletemsg.add(baseTemplateService.findById(basePartTemplate.getTemplateId()).orElse(null).getName());
              continue;
          }
          deleids.add(id);
      }
      deleteByIds(deleids.stream().toArray(String [] ::new));
        ResponseMessage re =   ResponseMessage.ok("操作成功");
        if(deletemsg.size()>0){
            String[] strings = deletemsg.stream().toArray(String[]::new);
            re.setMessage("模板编号【"+String.join(",", strings)+"】已产生业务,不允许删除！");
            return re;
        }else{
            return re;
        }
    }

}
