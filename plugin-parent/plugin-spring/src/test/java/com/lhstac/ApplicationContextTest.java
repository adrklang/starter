package com.lhstac;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.lhstack.spring")
public class ApplicationContextTest {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ApplicationContextTest.class);
        annotationConfigApplicationContext.start();
        Thread.sleep(1000000);
    }
}
