/*
 */
package com.airhacks.gatelink.encryption.control;

import com.airhacks.gatelink.encryption.control.Encryptor;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author airhacks.com
 */
public class EncryptorTest {

    @Test
    public void intToUnsignedBytes() {
        byte result[] = Encryptor.unsignedIntToBytes(42);
        assertNotNull(result);
        assertThat((int) result[0], is((int) 0));
        assertThat((int) result[1], is((int) 42));
        assertThat(result.length, is(2));
    }



}
