package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BaseInstruction;
import com.m2micro.m2mfa.base.entity.BasePartInstruction;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.entity.QBaseInstruction;
import com.m2micro.m2mfa.base.repository.BaseInstructionRepository;
import com.m2micro.m2mfa.base.repository.BasePartInstructionRepository;
import com.m2micro.m2mfa.base.service.BaseInstructionService;
import com.m2micro.m2mfa.base.vo.BaseInstructionQueryObj;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作业指导书 服务实现类
 * @author chengshuhong
 * @since 2019-03-04
 */
@Service
public class BaseInstructionServiceImpl implements BaseInstructionService {
    @Autowired
    BaseInstructionRepository baseInstructionRepository;
    @Autowired
    BasePartInstructionRepository basePartInstructionRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    public BaseInstructionRepository getRepository() {
        return baseInstructionRepository;
    }

    @Override
    public PageUtil<BaseInstruction> list(BaseInstructionQueryObj query) {
        QBaseInstruction qBaseInstruction = QBaseInstruction.baseInstruction;
        JPAQuery<BaseInstruction> jq = queryFactory.selectFrom(qBaseInstruction);
        BooleanBuilder condition = new BooleanBuilder();
        if(StringUtils.isNotEmpty(query.getInstructionCode())){
            condition.and(qBaseInstruction.instructionCode.like("%"+query.getInstructionCode()+"%"));
        }
        if(StringUtils.isNotEmpty(query.getInstructionName())){
            condition.and(qBaseInstruction.instructionName.like("%"+query.getInstructionName()+"%"));
        }
        jq.where(condition).offset((query.getPage() - 1) *query.getSize() ).limit(query.getSize());
        List<BaseInstruction> list = jq.fetch();

        long totalCount = jq.fetchCount();
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    @Transactional
    public void  save(BaseInstruction baseInstruction, MultipartFile file, HttpServletRequest request) {



        if(!baseInstructionRepository.findByInstructionCodeAndRevsion(baseInstruction.getInstructionCode(),baseInstruction.getRevsion()).isEmpty()){
            throw  new MMException("文件编号："+baseInstruction.getInstructionCode()+",版本："+baseInstruction.getRevsion()+" 已存在");
        }
        baseInstruction.setCheckFlag(false);
        baseInstruction.setExtension( file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
        //上次文件
        baseInstruction.setFileUrl(uploadFile(file,request,baseInstruction.getInstructionCode(),baseInstruction.getRevsion().toString()));
        ValidatorUtil.validateEntity(baseInstruction, AddGroup.class);
        baseInstruction.setInstructionId(UUIDUtil.getUUID());
        save(baseInstruction);
    }

    @Override
    @Transactional
    public void update(BaseInstruction baseInstruction, MultipartFile file, HttpServletRequest request) {
        BaseInstruction baseInstructionOld = findById(baseInstruction.getInstructionId()).orElse(null);
        if(baseInstructionOld==null){
            throw new MMException("数据库不存在该记录");
        }
        File deletefile = new File(baseInstruction.getFileUrl());
        deletefile.delete();
        baseInstruction.setExtension( file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
        //上次文件
        baseInstruction.setFileUrl(uploadFile(file,request,baseInstruction.getInstructionCode(),baseInstruction.getRevsion().toString()));
        PropertyUtil.copy(baseInstruction,baseInstructionOld);
        save(baseInstructionOld);
    }

    @Override
    public ResponseMessage delete(String[] ids) {
        List<BaseInstruction> enableDelete = new ArrayList<>();
        List<BaseInstruction> disableDelete = new ArrayList<>();
        for (String id:ids) {
            BaseInstruction bp = findById(id).orElse(null);
            if (bp == null) {
                throw new MMException("数据库不存在数据！");
            }
            List<BasePartInstruction> list = basePartInstructionRepository.findByInstructionId(bp.getInstructionId());

            if (list != null && list.size() > 0) {
                disableDelete.add(bp);
                continue;
            }
            File file = new File(bp.getFileUrl());
            file.delete();
            enableDelete.add(bp);
        }

        deleteAll(enableDelete);
        ResponseMessage re =   ResponseMessage.ok("操作成功");
        if(disableDelete.size()>0){
            String[] strings = disableDelete.stream().map(BaseInstruction::getInstructionCode).toArray(String[]::new);
            re.setMessage("作业指导编号【"+String.join(",", strings)+"】已产生业务,不允许删除！");
            return re;
        }else{
            return re;
        }
    }

    /**
     * 文件上传
     * @param file
     * @param request
     * @param instructionCode
     * @param version
     * @return
     */
    public String   uploadFile(MultipartFile file, HttpServletRequest request,String instructionCode,String version){
        String fileurl="";
        try{
            //创建文件在服务器端的存在路径
            String dir = request.getServletContext().getRealPath("/upload");
            File fileDir = new File(dir);
            if(!fileDir.exists())
                fileDir.mkdirs();
            //生成文件在服务器存放的名字
            String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName= instructionCode+version+fileSuffix;
            fileurl =fileDir+"/"+fileName;
            File files = new File(fileDir+"/"+fileName);
            //上传
            file.transferTo(files);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return  fileurl;
    }

}
