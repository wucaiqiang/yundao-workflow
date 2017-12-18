
package com.yundao.workflow.dto.actionconfig;

import java.util.Date;
import com.yundao.core.code.config.CommonCode;
import com.yundao.core.validator.group.Update;
import io.swagger.annotations.ApiModelProperty;
import com.yundao.core.validator.number.Number;

public class ActionConfigReqDto{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "新增不需要传")
    @Number(isBlank = false, message = "{" + CommonCode.COMMON_1079+ "}", groups = {Update.class})
    private Long id;

    @ApiModelProperty(value = "动作值")
    private Integer value;

    @ApiModelProperty(value = "动作中文名")
    private String name;


    public Long getId (){
        return id;
    }

    public void setId (Long id){
        this.id = id;
    }

    public Integer getValue (){
        return value;
    }

    public void setValue (Integer value){
        this.value = value;
    }

    public String getName (){
        return name;
    }

    public void setName (String name){
        this.name = name;
    }

}
