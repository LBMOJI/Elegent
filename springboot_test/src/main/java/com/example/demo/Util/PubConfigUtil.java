package com.example.demo.Util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 启动项目后, 加载数据库公共配置数据到redis中
 */
//@Component
public class PubConfigUtil {

    private static String pre = "pub_config"; // 存储到redis中的前缀
    private static String operation = ":"; // redis冒号分组

    @Resource
    private JdbcTemplate jdbcTemplate; // springboot集成mybatis-spring-boot-starter后本身封装的工具类

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct // 是java注解，在方法上加该注解会在项目启动的时候执行该方法，也可以理解为在spring容器初始化的时候执行该方法。
    public void reload() {
        String sql = "SELECT id,name,sex,pwd,phone,email,cno FROM `user` where name != '账号已注销'"; // 数据库配置表,根据你自己的配置表定义查询语句
        List<Map<String, Object>> mapsList = jdbcTemplate.queryForList(sql);
        if (!CollectionUtils.isEmpty(mapsList)) { // 存在值
            StringBuilder sbl = new StringBuilder();
            for (Map<String, Object> map : mapsList) {
                sbl.setLength(0);
                String id = map.get("id").toString(); // 数据库配置表中的字段名称| 模块: aliyun
                String name = map.get("name").toString(); // 数据库配置表中的字段名称| 配置名: SMS_TEMPLATECODE_LOGGIN (阿里云下短信模板号 - 登录)
                String sex = map.get("sex").toString(); // 数据库配置表中的字段名称| 配置具体值: xxxxx (阿里云下短信模板号)
                String pwd = map.get("pwd").toString();
                String phone = map.get("phone").toString();
                String email = map.get("email").toString();
                String cno = map.get("cno").toString();
                String value = sbl.append(pre).append(operation).append(name).append(operation).append(sex)
                        .append(operation).append(pwd).append(operation).append(phone)
                        .append(operation).append(email).append(operation).append(cno).toString();
                redisTemplate.opsForValue().set(id, value);
            }
        }
    }

    /*
    SELECT acception,admin,baoxian,car,record,saler,user FROM ad_basic_config WHERE data_status = '1'"
     // 获取启动后加载的配置数据 - 放到自己项目的redis工具类中
       public Integer getInt(String configBlock,String configName, Integer defaultVal) {
        String string = getString(configBlock,configName, null);
        Integer value = defaultVal;
        try {
            value = Integer.valueOf(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public Long getLong(String configBlock,String configName, Long defaultVal) {
        String string = getString(configBlock,configName, null);
        Long value = defaultVal;
        try {
            value = Long.valueOf(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public Double getDouble(String configBlock,String configName, Double defaultVal) {
        String string = getString(configBlock,configName, null);
        Double value = defaultVal;
        try {
            value = Double.valueOf(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public Boolean getBoolean(String configBlock,String configName, Boolean defaultVal) {
        String string = getString(configBlock,configName, null);
        Boolean value = defaultVal;
        try {
            value = Boolean.valueOf(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
    }   */
}
