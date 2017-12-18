package com.yundao.workflow.mapper.base;

import com.yundao.workflow.model.base.ProDefTaskActionModel;
import com.yundao.workflow.model.base.ProDefTaskActionModelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProDefTaskActionModelMapper {
    int countByExample(ProDefTaskActionModelExample example);

    int deleteByExample(ProDefTaskActionModelExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ProDefTaskActionModel record);

    int insertSelective(ProDefTaskActionModel record);

    List<ProDefTaskActionModel> selectByExample(ProDefTaskActionModelExample example);

    ProDefTaskActionModel selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProDefTaskActionModel record, @Param("example") ProDefTaskActionModelExample example);

    ProDefTaskActionModel selectOne(ProDefTaskActionModelExample example);

    int updateByExample(@Param("record") ProDefTaskActionModel record, @Param("example") ProDefTaskActionModelExample example);

    int updateByPrimaryKeySelective(ProDefTaskActionModel record);

    int updateByPrimaryKey(ProDefTaskActionModel record);
}