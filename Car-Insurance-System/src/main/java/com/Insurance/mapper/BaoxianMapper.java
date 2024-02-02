package com.Insurance.mapper;

import com.Insurance.entity.Baoxian;
import com.Insurance.entity.BaoxianExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaoxianMapper {
    long countByExample(BaoxianExample example);

    int deleteByExample(BaoxianExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Baoxian record);

    int insertSelective(Baoxian record);

    List<Baoxian> selectByExample(BaoxianExample example);

    Baoxian selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Baoxian record, @Param("example") BaoxianExample example);

    int updateByExample(@Param("record") Baoxian record, @Param("example") BaoxianExample example);

    int updateByPrimaryKeySelective(Baoxian record);

    int updateByPrimaryKey(Baoxian record);
}