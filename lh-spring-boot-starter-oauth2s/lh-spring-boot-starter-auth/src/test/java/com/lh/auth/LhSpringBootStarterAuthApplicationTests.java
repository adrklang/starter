package com.lh.auth;

import com.lh.auth.clients.token.utils.RsaUtils;
import org.junit.Test;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class LhSpringBootStarterAuthApplicationTests {

    @Test
    public void contextLoads() throws Exception {
        RsaUtils.generateKey("classpath:publicKey.pub","classpath:privateKey.pri","123456");
    }

}
