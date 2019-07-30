package com.lhstack.myblog;

import com.lh.auto.limit.test.MicroserviceMyblogLimitTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MicroserviceMyblogLimitTestApplicationTests {
    @Autowired
    private MicroserviceMyblogLimitTestApplication microserviceMyblogLimitTestApplication;
    @Test
    public void contextLoads() throws InterruptedException {
        for(int i = 0;i < 20;i++){
            String hello = microserviceMyblogLimitTestApplication.message("test",null);
            System.out.println(hello);
        }
    }

}
