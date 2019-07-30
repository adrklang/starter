package com.lh.auto.validcode;

import com.lh.auto.validcode.service.ValidCodeService;
import com.lh.auto.validcode.service.impl.MyblogKaptcha;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MicroserviceMyblogValidcodeApplicationTests {

    @Test
    public void contextLoads() throws IOException {
        ValidCodeService kaptcha = new MyblogKaptcha();
        String validCode = kaptcha.createValidCode(new FileOutputStream("C:\\Users\\10057\\Desktop\\新建文件夹\\img.jpg"));
        System.out.println(validCode);
    }

}
