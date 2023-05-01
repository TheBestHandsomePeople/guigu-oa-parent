package com.atguigu.process.service;

import com.atguigu.model.process.ProcessTemplate;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批模板 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-04-20
 */
public interface OaProcessTemplateService extends IService<ProcessTemplate> {

    Page<ProcessTemplate> selectPageProcessTempate(Page<ProcessTemplate> pageParam);

    void publish(Long id);
}