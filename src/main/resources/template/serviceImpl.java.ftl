package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${entity}Repository;
import ${package.Service}.${table.serviceName};
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import ${package.Entity}.Q${entity};
import java.util.List;
/**
 * ${table.comment!} 服务实现类
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${table.serviceImplName} implements ${table.serviceName} {
    @Autowired
    ${entity}Repository ${entity?uncap_first}Repository;
    @Autowired
    JPAQueryFactory queryFactory;

    public ${entity}Repository getRepository() {
        return ${entity?uncap_first}Repository;
    }

    @Override
    public PageUtil<${entity}> list(Query query) {
        Q${entity} q${entity} = Q${entity}.${entity?uncap_first};
        JPAQuery<${entity}> jq = queryFactory.selectFrom(q${entity});

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<${entity}> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}