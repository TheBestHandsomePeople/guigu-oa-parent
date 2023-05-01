package com.atguigu.process.service.impl;

import com.atguigu.model.process.ProcessTemplate;
import com.atguigu.model.process.ProcessType;
import com.atguigu.process.mapper.OaProcessTypeMapper;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-04-20
 */
@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

    @Autowired
    OaProcessTemplateService processTemplateService;
    @Override
    public List<ProcessType> findProcessType() {
        //查询所有审批分类返回list集合
        List<ProcessType> list = baseMapper.selectList(null);
        //遍历返回所有审批分类list集合
        for (ProcessType processType : list) {
            //得到每个审批分类，根据审批分类id查询对应审批模板
            Long typeId = processType.getId();
            //根据审批分类id查询对应审批模板
            LambdaQueryWrapper<ProcessTemplate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessTemplate::getProcessTypeId, typeId);
            List<ProcessTemplate> processTemplates = processTemplateService.list(wrapper);
            //根据审批分类id查询对应审批模板数据（list）
            processType.setProcessTemplateList(processTemplates);
        }
        return list;
    }
}
