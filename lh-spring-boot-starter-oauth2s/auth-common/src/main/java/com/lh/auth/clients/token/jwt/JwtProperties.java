package com.lh.auth.clients.token.jwt;

import com.lh.auth.clients.token.utils.RsaUtils;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
public class JwtProperties {

    private String privateKeyPath;

    private String publicKeyPath;

    private String secret;

    private String signingKey;

    public PublicKey getPublicKey(){
        try {
            return RsaUtils.getPublicKey(publicKeyPath);
        } catch (Exception e) {
            return null;
        }
    }

    public PrivateKey getPrivateKey(){
        try {
            return RsaUtils.getPrivateKey(privateKeyPath);
        } catch (Exception e) {
            return null;
        }
    }

    public KeyPair getKeyPair() {
        try {
            PrivateKey privateKey = this.getPrivateKey();
            PublicKey publicKey = this.getPublicKey();
            if(privateKey == null || publicKey == null){
                throw new Exception();
            }else{
                return new KeyPair(this.getPublicKey(),this.getPrivateKey());
            }
        } catch (Exception e) {
            try {
                RsaUtils.generateKey(publicKeyPath,privateKeyPath,secret);
                return new KeyPair(this.getPublicKey(),this.getPrivateKey());
            } catch (Exception e1) {
                return null;
            }
        }
    }

    public boolean isKeyPair() {
        return (this.privateKeyPath != null) && (this.publicKeyPath != null);
    }

    public boolean isSigningKey() {
        return !this.isKeyPair() && (this.signingKey != null);
    }

}
