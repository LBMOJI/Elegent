package com.example.demo.Controller;

import com.example.demo.Base.Result;
import com.example.demo.Util.RedisUtil;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("getHasKey/{key}")
    public boolean getHasKey(@PathVariable(name = "key") String key) {
        return redisUtil.hasKey(key);
    }

    @GetMapping("get/{key}")
    public Object getValue(@PathVariable(name = "key")String key) {
        return redisUtil.get(key);
    }

    @RequestMapping("set/{key}")
    public boolean set(@PathVariable(name = "key")String key, String value) {
        value = "李四";
        return redisUtil.set(key, value);
    }

    @RequestMapping("set/user")
    public boolean setObject() {
        User user = new User(1,"王五","1","123","122","22","12312");
        return redisUtil.set("用户1",user);
    }

    @RequestMapping("deleteAll")
    public Result deleteAll() {
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
        return Result.success();
    }

}
