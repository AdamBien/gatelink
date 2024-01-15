package com.airhacks.gatelink.encryption.control;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;
import org.junit.jupiter.api.Test;

public class AESEncryptorTest {

    /**
     * values were intercepted from working environment
     */
    byte[] key = "gxm_oiYGpcxWKW4wv5iZBw==".transform(Base64.getUrlDecoder()::decode);
    byte[] content = "aGVsbG8gd29ybGQgNDI=".transform(Base64.getUrlDecoder()::decode);
    byte[] nonce = "teidS9jM2vsmW8Yy".transform(Base64.getUrlDecoder()::decode);
    byte[] encryptedContent = "IoDG2H_olI4XWi8b5JrhwCUhRZMPHjRUaZqay4mSkAw=".transform(Base64.getUrlDecoder()::decode);


    @Test
    void aesEncryption() throws Exception {
       var actualEncryptedContent =  AESEncryptor.encryptWithAES(key, content, nonce);
       assertThat(actualEncryptedContent).isEqualTo(encryptedContent);
    }
}
