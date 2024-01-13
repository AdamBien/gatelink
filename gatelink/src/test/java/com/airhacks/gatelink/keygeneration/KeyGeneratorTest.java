package com.airhacks.gatelink.keygeneration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.airhacks.gatelink.keymanagement.control.ECKeyGenerator;

/**
 *
 * @author airhacks.com
 */
public class KeyGeneratorTest {



    @Test
    public void createKeys() throws Exception {
        var vapidKeys = ECKeyGenerator.generateVapidKeys();
        byte[] privateKey = vapidKeys.getPrivateKeyAsBytes();
        assertNotNull(privateKey);

        byte[] publicKey = vapidKeys.getUncompressedPublicKey();
        assertNotNull(publicKey);
        /**
         * https://tools.ietf.org/html/rfc5480 "The uncompressed form is
         * indicated by 0x04..."
         */
        assertThat(publicKey[0]).isEqualTo((byte) 0x04);
        assertThat(publicKey.length).isEqualTo(65);

        System.out.println(vapidKeys.getBase64URLEncodedPublicKeyWithoutPadding());
        System.out.println("---");
        System.out.println(vapidKeys.getBase64URLEncodedPrivateKeyWithoutPadding());

    }
}
