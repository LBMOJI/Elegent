package com.example.demo.Service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.Base.Result;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Service.UserService;
import com.example.demo.Util.CacheClient;
import com.example.demo.Util.RedisData;
import com.example.demo.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

//    @Autowired
//    private MailService mailService;

    @Resource
    private UserMapper mapper;

    @Resource
    private CacheClient cacheClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Result sendCode(String to, HttpSession session) {
//        Random random = new Random();
//        String code = String.valueOf(random.nextInt(900000) + 100000);
        String code = "000000";
//        session.setAttribute("code", code);
        redisTemplate.opsForValue().set("login:code:" + to, code, 60, TimeUnit.SECONDS);
//        mailService.sendTextMessage(to, "验证码", "验证码为" + code);
        return Result.success();
    }

    @Override
    public Result login(String phone, String code, HttpSession session) {

//        Object cacheCode = session.getAttribute("code");
        String cacheCode = redisTemplate.opsForValue().get("login:code:" + phone);
        if (cacheCode == null || !cacheCode.equals(code)) {
            return Result.ERROR("验证码错误");
        }
//        查询手机号
//        不在，创建用户并保存DTO到session
//        在，保存DTO到session
//        session.setAttribute("user", new UserDTO(1, "莫靖彬", ""));
        UserDTO userDTO = new UserDTO(1, "莫靖彬", "bin");
        Map<String, Object> map = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .ignoreNullValue()
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));

//        String token = UUID.randomUUID().toString();
        String token = "111";

        redisTemplate.opsForHash().putAll("login:token:" + token, map);
        redisTemplate.expire("login:token:" + token, 30, TimeUnit.MINUTES);
        return Result.success(token);
    }

    @Override
    public Result SelectById(int id) {
//        封装解决缓存穿透
//        User user = cacheClient.PassThrough("cache:user:", id, User.class, this::queryById, 5L, TimeUnit.MINUTES);
//        缓存穿透
//        User user = PassThrough(id);
//        互斥锁解决缓存击穿
//        User user = Mutex(id);
//        逻辑过期解决缓存击穿
//        User user = LogicalExpire(id);
        User user = cacheClient.LogicalExpire("cache:user:",id,User.class,this::queryById,5L,TimeUnit.MINUTES);

        if (user == null) {
            return Result.ERROR("失败");
        }
        return Result.success(user);
    }

    //    逻辑过期解决缓存穿透
    public User LogicalExpire(Integer id) {
        String userJson = redisTemplate.opsForValue().get("cache:user:" + id);
        if (StrUtil.isBlank(userJson)) {
            return null;
        }
//        反序列化
        RedisData redisData = JSONUtil.toBean(userJson, RedisData.class);
        User user = JSONUtil.toBean((JSONObject) redisData.getData(), User.class);
        LocalDateTime expireTime = redisData.getExpireTime();
//        判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            return user;
        }
//        获取互斥锁
        String lockKey = "lock:user:" + id;
        boolean isLock = tryLock(lockKey);
        if (isLock) {
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    saveUser2Redis(id, 30L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unLock(lockKey);
                }
            });
        }
        return user;
    }

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    public void saveUser2Redis(Integer id, Long expireTimeSeconds) {
        User user = mapper.selectById(id);
//        封装逻辑时间
        RedisData redisData = new RedisData();
        redisData.setData(user);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireTimeSeconds));
//        写入redis
        redisTemplate.opsForValue().set("cache:user:" + id, JSONUtil.toJsonStr(redisData));
    }

    //    互斥锁解决缓存击穿
    public User Mutex(Integer id) {
        String userJson = redisTemplate.opsForValue().get("cache:user:" + id);
        if (StrUtil.isNotBlank(userJson)) {
            return JSONUtil.toBean(userJson, User.class);
        }
        if (userJson != null) {
            return null;
        }
//        实现缓存重建
//        获取互斥锁
        String lockKey = null;
        User user = null;
        try {
            lockKey = "lock:user:" + id;
            boolean isLock = tryLock(lockKey);
//        判断是否获取成功
            if (!isLock) {
                Thread.sleep(50);
                return Mutex(id);
            }
//        不存在，查询数据库
            user = mapper.selectById(id);
//        判断数据库中是否存在，不存在则返回
            if (user == null) {
                redisTemplate.opsForValue().set("cache:user:" + id, "", 3, TimeUnit.MINUTES);
                return null;
            }
//        若存在则写入redis
            redisTemplate.opsForValue().set("cache:user:" + id, JSONUtil.toJsonStr(user), 30, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unLock(lockKey);
        }
//        释放互斥锁

        return user;
    }

    //    缓存穿透代码
    public User PassThrough(Integer id) {
        String userJson = redisTemplate.opsForValue().get("cache:user:" + id);
        if (StrUtil.isNotBlank(userJson)) {
            return JSONUtil.toBean(userJson, User.class);
        }
        if (userJson != null) {
            return null;
        }
//        不存在，查询数据库
        User user = mapper.selectById(id);
//        判断数据库中是否存在，不存在则返回
        if (user == null) {
            redisTemplate.opsForValue().set("cache:user:" + id, "", 3, TimeUnit.MINUTES);
            return null;
        }
//        若存在则写入redis
        redisTemplate.opsForValue().set("cache:user:" + id, JSONUtil.toJsonStr(user), 30, TimeUnit.DAYS);
        return user;
    }

    @Override
    @Transactional
    public Result updateById(User user) {
        if (user == null) {
            return Result.ERROR("不存在");
        }
        mapper.updateById(user);
        redisTemplate.delete("cache:user:" + user.getId());
        return Result.success();
    }

    private boolean tryLock(String key) {
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unLock(String key) {
        redisTemplate.delete(key);
    }

    public User queryById(Integer id) {
        return mapper.selectById(id);
    }
}
