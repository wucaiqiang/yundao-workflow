package com.yundao.workflow.service.actionconfig.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yundao.core.code.Result;
import com.yundao.core.constant.CommonConstant;
import com.yundao.core.pagination.PaginationSupport;
import com.yundao.core.utils.Limit;
import com.yundao.workflow.dto.BasePageDto;
import com.yundao.workflow.mapper.actionconfig.ActionConfigMapper;
import com.yundao.workflow.mapper.base.ActionConfigModelMapper;
import com.yundao.workflow.model.base.ActionConfigModel;
import com.yundao.workflow.model.base.ActionConfigModelExample;
import com.yundao.core.service.AbstractService;
import com.yundao.workflow.service.actionconfig.ActionConfigService;

@Service
public class ActionConfigServiceImpl extends AbstractService  implements ActionConfigService{

    @Autowired
    private ActionConfigModelMapper actionConfigModelMapper;

    @Autowired
    private ActionConfigMapper actionConfigMapper;

    public Result<Integer> insert(ActionConfigModel actionConfig) throws Exception{
        actionConfig.setCreateDate(new Date());
        actionConfig.setId(null);
        actionConfig.setCreateUserId(this.getHeaderUser().getUserId());
        int count = actionConfigModelMapper.insertSelective(actionConfig);
        return Result.newSuccessResult(count);
    }

    public Result<Integer> update(ActionConfigModel actionConfig) throws Exception{
        actionConfig.setUpdateDate(new Date());
        int count = actionConfigModelMapper.updateByPrimaryKeySelective(actionConfig);
        return Result.newSuccessResult(count);
    }

    public Result<Integer> delete(Long id) throws Exception{
        ActionConfigModel actionConfig = new ActionConfigModel();
        actionConfig.setId(id);
        actionConfig.setIsDelete(CommonConstant.ONE);
        int count = actionConfigModelMapper.updateByPrimaryKeySelective(actionConfig);
        return Result.newSuccessResult(count);
    }

    public Result<ActionConfigModel> select(Long id) throws Exception{
       ActionConfigModel model = actionConfigModelMapper.selectByPrimaryKey(id);
       return Result.newSuccessResult(model);
    }

    public Result<PaginationSupport<ActionConfigModel>> selectPage(ActionConfigModel actionConfigModel, BasePageDto pageDto) throws Exception{
		ActionConfigModelExample actionConfigModelExample = new ActionConfigModelExample().setLimit(Limit.buildLimit(pageDto.getCurrentPage(), pageDto.getPageSize()));
		ActionConfigModelExample.Criteria criteria = actionConfigModelExample.createCriteria();
		String orderByClause="create_date desc";
		if(StringUtils.isNotBlank(pageDto.getOrderColumn())){
		  StringBuilder orderBuider=new StringBuilder();
		  orderBuider.append(pageDto.getOrderColumn()).append(" ");
		  if(StringUtils.isNotBlank(pageDto.getSort())){
			orderBuider.append(pageDto.getSort());
		  }else{
			orderBuider.append("desc");
		  }
		  orderByClause=orderBuider.toString();
		}
		actionConfigModelExample.setOrderByClause(orderByClause);
		List<ActionConfigModel> list = actionConfigModelMapper.selectByExample(actionConfigModelExample);
		int totalCount = actionConfigModelMapper.countByExample(actionConfigModelExample);
		PaginationSupport<ActionConfigModel> result = pageDto.getPaginationSupport();
		result.setDatas(list);
		result.setTotalCount(totalCount);
		return Result.newSuccessResult(result);
    }

}
