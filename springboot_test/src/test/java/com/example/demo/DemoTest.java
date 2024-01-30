package com.example.demo;

import com.example.demo.Util.RedisIDWorker;
import com.example.demo.domain.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class DemoTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisIDWorker idWorker;

    private ExecutorService es = Executors.newFixedThreadPool(500);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void TestTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);

        Runnable task = () ->{
            for (int i = 0;i<100;i++){
                long id = idWorker.nextId("order");
                System.out.println("id = " + id);
            }
            latch.countDown();
        };
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            es.submit(task);
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - begin));
    }

    @Test
    void TestGetUser() throws JsonProcessingException {
        User user = new User(2, "赵六","2","1","11","22","11");
        String json = objectMapper.writeValueAsString(user);

        stringRedisTemplate.opsForValue().set("user2", json);

        String user1 = stringRedisTemplate.opsForValue().get("user2");
        User user2 = objectMapper.readValue(user1, User.class);
        System.out.println(user2);
    }

    @Test
    void RandomCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        System.out.println("code = " + code);
    }


}
