package com.m2micro.m2mfa.base.service;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BaseInstruction;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.vo.BaseInstructionQueryObj;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作业指导书 服务类
 * @author chengshuhong
 * @since 2019-03-04
 */
public interface BaseInstructionService extends BaseService<BaseInstruction,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseInstruction> list(BaseInstructionQueryObj query);

    /**
     * 添加作业指导
     * @param baseInstruction
     * @param file
     */
    void save(BaseInstruction baseInstruction, MultipartFile file );

    /**
     * 跟新作业指导
     * @param baseInstruction
     * @param file
     */
    void update(BaseInstruction baseInstruction, MultipartFile file );

    /**
     * 删除作业指导
     * @param ids
     * @return
     */
    ResponseMessage delete(String [] ids );

}

