package ${package.Controller};

import ${package.Service}.${table.serviceName};
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
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
    public ResponseMessage<PageUtil<${entity}>> list(Query query){
        PageUtil<${entity}> page = ${table.serviceName?uncap_first}.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="${table.comment!}详情")
    public ResponseMessage<${entity}> info(@PathVariable("id") Long id){
        ${entity} ${entity?uncap_first} = ${table.serviceName?uncap_first}.findById(id).orElse(null);
        return ResponseMessage.ok(${entity?uncap_first});
    }

    /**
     * 保存或更新
     */
    @RequestMapping("/saveOrUpdate")
    @ApiOperation(value="保存或更新${table.comment!}")
    public ResponseMessage<${entity}> saveOrUpdate(@RequestBody ${entity} ${entity?uncap_first}){
        return ResponseMessage.ok(${table.serviceName?uncap_first}.save(${entity?uncap_first}));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除${table.comment!}")
    public ResponseMessage delete(@RequestBody Long[] ids){
        ${table.serviceName?uncap_first}.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}