package com.Insurance.mapper;

import com.Insurance.entity.Acception;
import com.Insurance.entity.AcceptionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AcceptionMapper {
    long countByExample(AcceptionExample example);

    int deleteByExample(AcceptionExample example);

    int deleteByPrimaryKey(String id);

    int insert(Acception record);

    int insertSelective(Acception record);

    List<Acception> selectByExample(AcceptionExample example);

    Acception selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Acception record, @Param("example") AcceptionExample example);

    int updateByExample(@Param("record") Acception record, @Param("example") AcceptionExample example);

    int updateByPrimaryKeySelective(Acception record);

    int updateByPrimaryKey(Acception record);
}