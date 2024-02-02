package com.Insurance.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        //边框设置
        properties.setProperty("kaptcha.border","yes");
        //设置颜色
        properties.setProperty("kaptcha.border.color","105,179,90");
        //字体颜色
        properties.setProperty("kaptcha.textproducer.font.color","red");
        //图片宽
        properties.setProperty("kaptcha.image.width","110");
        //图片高
        properties.setProperty("kaptcha.image.height","40");
        //字体大小
        properties.setProperty("kaptcha.textproducer.font.size","30");
        //验证码session key
        properties.setProperty("kaptcha.session.key","code");
        //验证码长度
        properties.setProperty("kaptcha.textproducer.char.length","4");
        //字体名称
        properties.setProperty("kaptcha.textproducer.font.name","宋体,黑体,微软雅黑");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
