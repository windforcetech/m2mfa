package ${package.Entity};


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
<#--<#list table.importPackages as pkg>
import ${pkg};
</#list>-->
<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>

/**
 * ${table.comment!}
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
    <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
    <#else>
@EqualsAndHashCode(callSuper = false)
    </#if>
@Accessors(chain = true)
</#if>
<#if table.convert>
@TableName("${table.name}")
</#if>
@Entity
<#if swagger2>
@ApiModel(value="${entity}对象", description="${table.comment!}")
</#if>
<#if superEntityClass??>
public class ${entity} extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
public class ${entity} extends Model<${entity}> {
<#else>
public class ${entity} extends BaseEntity implements Serializable {
</#if>

    private static final long serialVersionUID = 1L;
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
    <#if field.comment!?length gt 0>
        <#if swagger2>
            <#if field.propertyName == "createOn">
            <#elseif field.propertyName == "createBy">
            <#elseif field.propertyName == "modifiedOn">
            <#elseif field.propertyName == "modifiedBy">
            <#else>
    @ApiModelProperty(value = "${field.comment}")
            </#if>
        <#else>
    /**
     * ${field.comment}
     */
        </#if>
    </#if>
    <#if field.keyFlag>
    <#-- 主键 -->
    @Id
        <#if field.keyIdentityFlag>
    <#--@Id-->
        <#elseif idType??>
    @TableId(value = "${field.name}", type = IdType.${idType})
        <#elseif field.convert>
    @TableId("${field.name}")
        </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- -----   存在字段填充设置   ----->
        <#if field.convert>
    @TableField(value = "${field.name}", fill = FieldFill.${field.fill})
        <#else>
    @TableField(fill = FieldFill.${field.fill})
        </#if>
    <#elseif field.convert>
    @TableField("${field.name}")
    </#if>
<#-- 乐观锁注解 -->
    <#if (versionFieldName!"") == field.name>
    @Version
    </#if>
<#-- 逻辑删除注解 -->
    <#if (logicDeleteFieldName!"") == field.name>
    @TableLogic
    </#if>
    <#if field.propertyName == "createOn">
    <#elseif field.propertyName == "createBy">
    <#elseif field.propertyName == "modifiedOn">
    <#elseif field.propertyName == "modifiedBy">
    <#else>
    private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->

<#if !entityLombokModel>
    <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
        <#if field.propertyName == "createOn">
        <#elseif field.propertyName == "createBy">
        <#elseif field.propertyName == "modifiedOn">
        <#elseif field.propertyName == "modifiedBy">
        <#else>
    public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
    }
        </#if>
        <#if field.propertyName == "createOn">
        <#elseif field.propertyName == "createBy">
        <#elseif field.propertyName == "modifiedOn">
        <#elseif field.propertyName == "modifiedBy">
        <#else>
            <#if entityBuilderModel>
    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
            <#else>
    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
            </#if>
        this.${field.propertyName} = ${field.propertyName};
            <#if entityBuilderModel>
        return this;
            </#if>
    }

        </#if>
    </#list>
</#if>

<#if entityColumnConstant>
    <#list table.fields as field>
    public static final String ${field.name?upper_case} = "${field.name}";

    </#list>
</#if>
<#if activeRecord>
    @Override
    protected Serializable pkVal() {
    <#if keyPropertyName??>
        return this.${keyPropertyName};
    <#else>
        return null;
    </#if>
    }

</#if>

}