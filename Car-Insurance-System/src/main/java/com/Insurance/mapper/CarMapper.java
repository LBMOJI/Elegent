package com.Insurance.mapper;

import com.Insurance.entity.Car;
import com.Insurance.entity.CarExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CarMapper {
    long countByExample(CarExample example);

    int deleteByExample(CarExample example);

    int deleteByPrimaryKey(@Param("cph") String cph, @Param("ctype") String ctype);

    int insert(Car record);

    int insertSelective(Car record);

    List<Car> selectByExampleWithBLOBs(CarExample example);

    List<Car> selectByExample(CarExample example);

    Car selectByPrimaryKey(@Param("cph") String cph, @Param("ctype") String ctype);

    int updateByExampleSelective(@Param("record") Car record, @Param("example") CarExample example);

    int updateByExampleWithBLOBs(@Param("record") Car record, @Param("example") CarExample example);

    int updateByExample(@Param("record") Car record, @Param("example") CarExample example);

    int updateByPrimaryKeySelective(Car record);

    int updateByPrimaryKeyWithBLOBs(Car record);

    int updateByPrimaryKey(Car record);

    List<Car> selectCarAndUser();

    List<Car> selectCarAndUserByCph(String cph);

    List<Car> selectAll();

    List<Car> selectByStatus(String status);

    List<Car> selectByCph(String cph);
}