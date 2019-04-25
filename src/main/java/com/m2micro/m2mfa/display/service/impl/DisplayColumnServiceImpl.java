package com.m2micro.m2mfa.display.service.impl;

import com.m2micro.m2mfa.display.entity.DisplayColumn;
import com.m2micro.m2mfa.display.repository.DisplayColumnRepository;
import com.m2micro.m2mfa.display.service.DisplayColumnService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.display.entity.QDisplayColumn;
import java.util.List;
/**
 * 显示列 服务实现类
 * @author liaotao
 * @since 2019-04-25
 */
@Service
public class DisplayColumnServiceImpl implements DisplayColumnService {
    @Autowired
    DisplayColumnRepository displayColumnRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public DisplayColumnRepository getRepository() {
        return displayColumnRepository;
    }

    @Override
    public PageUtil<DisplayColumn> list(Query query) {
        QDisplayColumn qDisplayColumn = QDisplayColumn.displayColumn;
        JPAQuery<DisplayColumn> jq = queryFactory.selectFrom(qDisplayColumn);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<DisplayColumn> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

}