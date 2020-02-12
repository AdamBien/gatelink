package com.airhacks.gatelink.keygeneration;

import com.airhacks.gatelink.keymanagement.control.KeyGenerator;
import com.airhacks.gatelink.keymanagement.entity.ServerKeys;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author airhacks.com
 */
public class KeyGeneratorTest {

    private KeyGenerator cut;

    @BeforeEach
    public void init() {
        this.cut = new KeyGenerator();
        this.cut.initializeProvider();
    }

    @Test
    public void createKeys() throws Exception {
        ServerKeys vapidKeys = this.cut.generateVapidKeys();
        byte[] privateKey = vapidKeys.getPrivateKeyAsBytes();
        assertNotNull(privateKey);

        byte[] publicKey = vapidKeys.getUncompressedPublicKey();
        assertNotNull(publicKey);
        /**
         * https://tools.ietf.org/html/rfc5480 "The uncompressed form is
         * indicated by 0x04..."
         */
        assertThat(publicKey[0], is((byte) 0x04));
        assertThat(publicKey.length, is(65));

        System.out.println(vapidKeys.getBase64URLEncodedPublicKeyWithoutPadding());
        System.out.println("---");
        System.out.println(vapidKeys.getBase64URLEncodedPrivateKeyWithoutPadding());

    }
}
