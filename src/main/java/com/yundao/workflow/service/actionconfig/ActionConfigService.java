package com.yundao.workflow.service.actionconfig;

import java.util.List;
import com.yundao.workflow.mapper.base.ActionConfigModelMapper;
import com.yundao.workflow.dto.BasePageDto;
import com.yundao.workflow.model.base.ActionConfigModel;
import com.yundao.core.code.Result;
import com.yundao.core.pagination.PaginationSupport;

import java.util.Map;

public interface ActionConfigService{


    public Result<Integer> insert(ActionConfigModel actionConfig) throws Exception;


    public Result<Integer> update(ActionConfigModel actionConfig) throws Exception;


    public Result<Integer> delete(Long id) throws Exception;


    public Result<ActionConfigModel> select(Long id) throws Exception;

    public Result<PaginationSupport<ActionConfigModel>> selectPage(ActionConfigModel actionConfigModel, BasePageDto pageDto) throws Exception;

}
