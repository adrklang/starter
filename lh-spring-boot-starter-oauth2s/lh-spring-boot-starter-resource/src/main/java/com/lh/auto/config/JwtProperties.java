package com.lh.auto.config;
import com.lh.auto.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "lh.auto.auth.jwttoken")
@Data
public class JwtProperties {

    private String privateKeyPath;

    private String publicKeyPath;

    private String secret;

    public PublicKey getPublicKey(){
        try {
            return RsaUtils.getPublicKey(publicKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PrivateKey getPrivateKey(){
        try {
            return RsaUtils.getPrivateKey(privateKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeyPair getKeyPair(){
        try {
            PrivateKey privateKey = this.getPrivateKey();
            PublicKey publicKey = this.getPublicKey();
            if(privateKey == null || publicKey == null){
                RsaUtils.generateKey(publicKeyPath,privateKeyPath,secret);
            }
            return new KeyPair(this.getPublicKey(),this.getPrivateKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
