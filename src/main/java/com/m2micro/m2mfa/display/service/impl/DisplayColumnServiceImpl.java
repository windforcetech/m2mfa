package com.m2micro.m2mfa.display.service.impl;

import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.m2mfa.display.entity.DisplayColumn;
import com.m2micro.m2mfa.display.repository.DisplayColumnRepository;
import com.m2micro.m2mfa.display.service.DisplayColumnService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.display.entity.QDisplayColumn;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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

    @Override
    public DisplayColumn saveEntity(DisplayColumn displayColumn) {
        ValidatorUtil.validateEntity(displayColumn, AddGroup.class);
        displayColumn.setColumnId(UUIDUtil.getUUID());
        return save(displayColumn);
    }

    @Override
    public DisplayColumn updateEntity(DisplayColumn displayColumn) {
        ValidatorUtil.validateEntity(displayColumn, UpdateGroup.class);
        DisplayColumn displayColumnOld = findById(displayColumn.getColumnId()).orElse(null);
        if(displayColumnOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(displayColumn,displayColumnOld);
        return save(displayColumnOld);
    }

    @Override
    public DisplayColumn info() {
        TokenInfo tokenInfo = TokenInfo.getTokenInfo();
        String userId = tokenInfo.getUserID();
        String groupId = TokenInfo.getUserGroupId();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String moduleId = request.getHeader("requestPage");
        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(groupId)||StringUtils.isEmpty(moduleId)){
            throw new MMException("请求参数有误！");
        }
        return displayColumnRepository.findByUserIdAndModuleIdAndGroupId(userId,moduleId,groupId);
    }

}