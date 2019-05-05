package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseInstruction;
import com.m2micro.m2mfa.base.entity.BasePartInstruction;
import com.m2micro.m2mfa.base.entity.QBaseInstruction;
import com.m2micro.m2mfa.base.repository.BaseInstructionRepository;
import com.m2micro.m2mfa.base.repository.BasePartInstructionRepository;
import com.m2micro.m2mfa.base.service.BaseInstructionService;
import com.m2micro.m2mfa.base.vo.BaseInstructionQueryObj;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    BaseItemsTargetServiceImpl baseItemsTargetService;

    @Override
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
        if(StringUtils.isNotEmpty(query.getCategory())){
            condition.and(qBaseInstruction.category.eq(query.getCategory()));
        }
        if(StringUtils.isNotEmpty(query.getDescription())){
            condition.and(qBaseInstruction.description.like("%"+query.getDescription()+"%"));
        }
       if(query.getEnabled()!=null){
         condition.and(qBaseInstruction.enabled.eq(query.getEnabled()));
       }
        if(query.getCheckflag()!=null){
            condition.and(qBaseInstruction.checkFlag.eq(query.getCheckflag()));
        }
        jq.where(condition);
        OrderSpecifier orderSpecifier = null;
        if(orderSpecifier==null||"createOn".equals(query.getDirect())){
            orderSpecifier = qBaseInstruction.createOn.desc();
        }
        if(StringUtils.isNotEmpty(query.getDirect())){
            if("instructionCode".equals(query.getOrder())){
                if("desc".equalsIgnoreCase(query.getDirect())){
                    orderSpecifier = qBaseInstruction.instructionCode.desc();
                }else{
                    orderSpecifier = qBaseInstruction.instructionCode.asc();
                }
            }
            if("instructionName".equals(query.getOrder())){
                if("desc".equalsIgnoreCase(query.getDirect())){
                    orderSpecifier = qBaseInstruction.instructionName.desc();
                }else{
                    orderSpecifier = qBaseInstruction.instructionName.asc();
                }
            }
            if("category".equals(query.getOrder())){
                if("desc".equalsIgnoreCase(query.getDirect())){
                    orderSpecifier = qBaseInstruction.category.desc();
                }else{
                    orderSpecifier = qBaseInstruction.category.asc();
                }
            }
            if("enabled".equals(query.getOrder())){
                if("desc".equalsIgnoreCase(query.getDirect())){
                    orderSpecifier = qBaseInstruction.enabled.desc();
                }else{
                    orderSpecifier = qBaseInstruction.enabled.asc();
                }
            }

        }
        jq.orderBy(orderSpecifier).orderBy(qBaseInstruction.createOn.desc()).offset((query.getPage() - 1) *query.getSize() ).limit(query.getSize());
        List<BaseInstruction> list = jq.fetch();
        List<BaseInstruction> collect = list.stream().filter(e -> {
            e.setCategoryName(baseItemsTargetService.findById(e.getCategory()).orElse(null).getItemName());
            return true;
        }).collect(Collectors.toList());
        long totalCount = jq.fetchCount();
        return PageUtil.of(collect,totalCount,query.getSize(),query.getPage());
    }


    @Override
    @Transactional
    public void  save(BaseInstruction baseInstruction, MultipartFile file) {
        //获取原有的版本，有的话全部为“无效”，
        if(file==null){
            throw  new MMException("请选择作业指导书文件");
        }
        List<BaseInstruction> byInstructionCode = baseInstructionRepository.findByInstructionCode(baseInstruction.getInstructionCode());
        List<BaseInstruction> collect = byInstructionCode.stream().filter(i -> {
            i.setEnabled(false);
            return true;
        }).collect(Collectors.toList());
        baseInstructionRepository.saveAll(collect);

        if(!baseInstructionRepository.findByInstructionCodeAndRevsion(baseInstruction.getInstructionCode(),baseInstruction.getRevsion()).isEmpty()){
            throw  new MMException("文件编号："+baseInstruction.getInstructionCode()+",版本："+baseInstruction.getRevsion()+" 已存在");
        }
        baseInstruction.setCheckFlag(false);
        baseInstruction.setExtension( file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
        //上次文件
        baseInstruction.setFileUrl(uploadFile(file,baseInstruction.getInstructionCode(),baseInstruction.getRevsion().toString()));
        ValidatorUtil.validateEntity(baseInstruction, AddGroup.class);
        baseInstruction.setInstructionId(UUIDUtil.getUUID());
        save(baseInstruction);
    }


    @Override
    @Transactional
    public void update(BaseInstruction baseInstruction, MultipartFile file) {
        ValidatorUtil.validateEntity(baseInstruction, UpdateGroup.class);
        BaseInstruction baseInstructionOld = findById(baseInstruction.getInstructionId()).orElse(null);
        if(baseInstructionOld==null){
            throw new MMException("数据库不存在该记录");
        }
        if(file !=null){
            try {
                File deletefile = new File(baseInstruction.getFileUrl());
                deletefile.delete();
            }catch (Exception e){

            }

            baseInstruction.setExtension( file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
            //上次文件
            baseInstruction.setFileUrl(uploadFile(file,baseInstruction.getInstructionCode(),baseInstruction.getRevsion().toString()));
        }

        PropertyUtil.copy(baseInstruction,baseInstructionOld);
        save(baseInstructionOld);
    }

    @Override
    @Transactional
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
     * @param instructionCode
     * @param version
     * @return
     */
    public String   uploadFile(MultipartFile file,String instructionCode,String version){
        String fileurl="";
        try{
            File fileDir = new File(System.getProperty("user.dir")+"src"+ File.separator+"main"+File.separator +"resources"+File.separator+"resources");
            if(!fileDir.exists())
                fileDir.mkdirs();
            //生成文件在服务器存放的名字
            String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName= instructionCode+"-"+version+fileSuffix;
            fileurl =fileDir+File.separator+fileName;
            File files = new File(fileDir+File.separator+fileName);
            //上传
            file.transferTo(files);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return  fileurl;
    }


}


