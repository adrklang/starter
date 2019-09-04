package com.lh.auto.limit;

import com.lh.auto.limit.annotation.EnableRateLimit;
import com.lh.auto.limit.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestService.class)
@EnableRateLimit
public class MicroserviceMyblogApplicationTest {
    @Autowired
    private TestService testService;
    @Test
    public void testHelloLimit() throws InterruptedException {
        long timeMillis = System.currentTimeMillis();
        for(int i = 0;i < 10000;i++){
            List<String> strings = testService.helloList();
            System.out.println(strings);
        }
        System.out.println(System.currentTimeMillis() - timeMillis);
    }
    @Test
    public void testMapLimit() throws InterruptedException {
        for(int i = 0;i < 50;i++){
            Map<String, String> stringStringMap = testService.helloMap("str sts");
            System.out.println(stringStringMap);
        }
    }
}
