package com.lhstack.myblog;

import com.lh.auto.limit.test.MicroserviceMyblogLimitTestApplication;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
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

    public static void main(String[] args) {
        Class<MicroserviceMyblogLimitTestApplication> testApplicationClass = MicroserviceMyblogLimitTestApplication.class;
        Method[] methods = testApplicationClass.getMethods();
        for(Method method : methods){
            System.out.println(method.getName());
        }
    }
    public void testMethodName(){

    }
}
