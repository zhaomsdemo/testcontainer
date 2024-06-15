package com.zhaomsdemo.research.testcontainer.nosql;

import com.mongodb.client.MongoClient;
import com.zhaomsdemo.research.testcontainer.model.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
public class MongoDbTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.4")
            .withExposedPorts(27017);
    @Autowired
    private MongoClient mongo;

    @DynamicPropertySource
    public static void registryProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
    }

    @Test
    public void testInsertData() {
        Result<String> result = Result.<String>builder()
                .code(404)
                .message("Not Found")
                .data("empty data")
                .build();
        mongoTemplate.insert(result);
        String id = result.getId();
        assertNotNull(id);
        Result<String> searchResult = mongoTemplate.findById(id, Result.class);
        assertNotNull(searchResult);
        
    }
}
