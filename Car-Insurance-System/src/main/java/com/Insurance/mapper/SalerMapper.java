package com.Insurance.mapper;

import com.Insurance.entity.Saler;
import com.Insurance.entity.SalerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SalerMapper {
    long countByExample(SalerExample example);

    int deleteByExample(SalerExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Saler record);

    int insertSelective(Saler record);

    List<Saler> selectByExample(SalerExample example);

    Saler selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Saler record, @Param("example") SalerExample example);

    int updateByExample(@Param("record") Saler record, @Param("example") SalerExample example);

    int updateByPrimaryKeySelective(Saler record);

    int updateByPrimaryKey(Saler record);
}