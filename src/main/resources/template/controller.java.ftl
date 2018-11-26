package ${package.Controller};

import ${package.Service}.${table.serviceName};
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import ${package.Entity}.${entity};
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>

/**
 * ${table.comment!} 前端控制器
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}/${table.entityPath}</#if>")
@Api(value="${table.comment!} 前端控制器")
public class ${table.controllerName} {
    @Autowired
    ${table.serviceName} ${table.serviceName?uncap_first};

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="${table.comment!}列表")
    @UserOperationLog("${table.comment!}列表")
    public ResponseMessage<PageUtil<${entity}>> list(Query query){
        PageUtil<${entity}> page = ${table.serviceName?uncap_first}.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="${table.comment!}详情")
    @UserOperationLog("${table.comment!}详情")
    public ResponseMessage<${entity}> info(@PathVariable("id") String id){
        ${entity} ${entity?uncap_first} = ${table.serviceName?uncap_first}.findById(id).orElse(null);
        return ResponseMessage.ok(${entity?uncap_first});
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存${table.comment!}")
    @UserOperationLog("保存${table.comment!}")
    public ResponseMessage<${entity}> save(@RequestBody ${entity} ${entity?uncap_first}){
        ValidatorUtil.validateEntity(${entity?uncap_first}, AddGroup.class);
    <#list table.fields as field>
        <#if field.keyFlag>
            <#assign keyPropertyName="${field.propertyName}"/>
        </#if>
    </#list>
        <#if keyPropertyName??>
        ${entity?uncap_first}.set${keyPropertyName?cap_first}(UUIDUtil.getUUID());
        </#if>
        return ResponseMessage.ok(${table.serviceName?uncap_first}.save(${entity?uncap_first}));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新${table.comment!}")
    @UserOperationLog("更新${table.comment!}")
    public ResponseMessage<${entity}> update(@RequestBody ${entity} ${entity?uncap_first}){
        ValidatorUtil.validateEntity(${entity?uncap_first}, UpdateGroup.class);
        ${entity} ${entity?uncap_first}Old = ${table.serviceName?uncap_first}.findById(${entity?uncap_first}.get${keyPropertyName?cap_first}()).orElse(null);
        if(${entity?uncap_first}Old==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(${entity?uncap_first},${entity?uncap_first}Old);
        return ResponseMessage.ok(${table.serviceName?uncap_first}.save(${entity?uncap_first}Old));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除${table.comment!}")
    @UserOperationLog("删除${table.comment!}")
    public ResponseMessage delete(@RequestBody String[] ids){
        ${table.serviceName?uncap_first}.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}