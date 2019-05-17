package com.m2micro.m2mfa.base.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.Query;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.core.types.dsl.StringPath;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BaseController<T, ID, M extends Query> {
    @GetMapping
    @ApiOperation(value = "分页查询", responseReference = "get")
    default ResponseMessage<Page<T>> findAll(M model) throws IOException {

        List<Predicate> predicates = Lists.newArrayList();
        T entity = getService().createEntity();
        String[] ignoreProperties = getIgnoreFieldName(entity);

        PageRequest pageable = PageRequest.of(model.getPage(), model.getSize(),
                Sort.Direction.fromString(model.getDirect()), model.getOrder());

        modelToEntity(model, entity, ignoreProperties);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(entity);
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
        };
        Map<String, Object> map = objectMapper.readValue(json, typeReference);

        map.forEach((k, v) -> {
            if (v == null) {
                return;
            }
            if (v instanceof String) {
                StringPath stringPath = Expressions.stringPath(k);
                predicates.add(stringPath.containsIgnoreCase((String) v));
            } else {
                SimplePath<?> path = Expressions.path(v.getClass(), k);
                Expression<?> expression = ExpressionUtils.toExpression(v);
                predicates.add(Expressions.booleanOperation(Ops.EQ, path, expression));
            }
        });
        Predicate customPredicate = customPredicate(model);
        if (customPredicate != null) {
            predicates.add(customPredicate);
        }
        Predicate predicate = ExpressionUtils.allOf(predicates);
        if (predicate == null) {
            return ResponseMessage.ok(getService().findAll(pageable));
        } else {
            return ResponseMessage.ok(getService().findAll(predicate, pageable));
        }

    }

    default String[] getIgnoreFieldName(T entity) {
        return new String[0];
    }

    default T modelToEntity(M model, T entity, String... ignoreProperties) {
        BeanUtils.copyProperties(model, entity, ignoreProperties);
        return entity;
    }

    default Predicate customPredicate(M model) {
        return null;
    }

    BaseService<T, ID> getService();
}
