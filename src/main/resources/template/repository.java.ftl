package ${package.Mapper};

import ${package.Entity}.${entity};
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * ${table.comment!} Repository 接口
 * @author ${author}
 * @since ${date}
 */
@Repository
public interface ${entity}Repository extends BaseRepository<${entity},Long> {

}