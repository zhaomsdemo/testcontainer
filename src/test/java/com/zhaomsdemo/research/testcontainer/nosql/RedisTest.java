package com.zhaomsdemo.research.testcontainer.nosql;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class RedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    static RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:7.0-alpine3.20"))
                .withExposedPorts(6379);

    @DynamicPropertySource
    public static void registryProperties(DynamicPropertyRegistry registry) {
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", redis.getMappedPort(6379).toString());
    }

    @BeforeAll
    public static void setup() {
    }

    @AfterAll
    public static void teardown() {
        redis.stop();
    }

    @Test
    public void testRedisOperations() {
        redisTemplate.opsForValue().set("name", "zjh");
        String value = redisTemplate.opsForValue().get("name");
        assertEquals("zjh", value);
    }
}
