package com.yundao.workflow.mapper.base;

import com.yundao.workflow.model.base.ActionConfigModel;
import com.yundao.workflow.model.base.ActionConfigModelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActionConfigModelMapper {
    int countByExample(ActionConfigModelExample example);

    int deleteByExample(ActionConfigModelExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ActionConfigModel record);

    int insertSelective(ActionConfigModel record);

    List<ActionConfigModel> selectByExample(ActionConfigModelExample example);

    ActionConfigModel selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ActionConfigModel record, @Param("example") ActionConfigModelExample example);

    ActionConfigModel selectOne(ActionConfigModelExample example);

    int updateByExample(@Param("record") ActionConfigModel record, @Param("example") ActionConfigModelExample example);

    int updateByPrimaryKeySelective(ActionConfigModel record);

    int updateByPrimaryKey(ActionConfigModel record);
}