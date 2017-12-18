package com.yundao.workflow.controller.actionconfig;
import com.yundao.workflow.dto.BasePageDto;

import com.yundao.workflow.service.actionconfig.ActionConfigService;
import com.yundao.workflow.model.base.ActionConfigModel;
import com.yundao.workflow.dto.actionconfig.ActionConfigReqDto;
import com.yundao.core.code.Result;
import com.yundao.core.validator.group.Update;
import com.yundao.core.pagination.PaginationSupport;
import com.yundao.core.validator.spring.BindingResultHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/actionconfig/")
@ResponseBody
@Api("动作属性配置")
public class ActionConfigController{

    @Autowired
    private ActionConfigService actionConfigService;

    @RequestMapping(value = "get_page",method=RequestMethod.GET)
    @ApiOperation(value="分页查询")
    public Result<PaginationSupport<ActionConfigModel>> getPage(@ModelAttribute ActionConfigReqDto dto, @ModelAttribute BasePageDto pageDto) throws Exception{
        ActionConfigModel model = new ActionConfigModel();
        BeanUtils.copyProperties(dto,model);
		return actionConfigService.selectPage(model, pageDto);
    }

    @RequestMapping(value="add", method=RequestMethod.POST)
    @ApiOperation(value="新增", notes="根据ActionConfig对象创建")
    public Result<Integer> add(@Validated @ModelAttribute ActionConfigReqDto dto, BindingResult bindingResult) throws Exception{
        BindingResultHandler.handleByException(bindingResult);
        ActionConfigModel model = new ActionConfigModel();
        BeanUtils.copyProperties(dto,model);
        return actionConfigService.insert(model);
    }

    @RequestMapping(value = "update", method=RequestMethod.POST)
    @ApiOperation(value="编辑信息")
    public Result<Integer> update(@Validated(value = {Update.class}) @ModelAttribute ActionConfigReqDto dto, BindingResult bindingResult) throws Exception{
        BindingResultHandler.handleByException(bindingResult);
        ActionConfigModel model = new ActionConfigModel();
        BeanUtils.copyProperties(dto,model);
        return actionConfigService.update(model);
    }

    @RequestMapping(value = "get", method=RequestMethod.GET)
    @ApiOperation(value="获取详细信息")
    public Result<ActionConfigModel> get(@RequestParam Long id) throws Exception{
       return actionConfigService.select(id);
    }

    @RequestMapping(value = "delete", method=RequestMethod.POST)
    @ApiOperation(value="删除信息")
    public Result<Integer> delete(@RequestParam Long id) throws Exception{
        return actionConfigService.delete(id);
    }

}
