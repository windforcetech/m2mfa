package ${package.Service};

import ${package.Entity}.${entity};
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * ${table.comment!} 服务类
 * @author ${author}
 * @since ${date}
 */
public interface ${table.serviceName} extends BaseService<${entity},String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<${entity}> list(Query query);
}