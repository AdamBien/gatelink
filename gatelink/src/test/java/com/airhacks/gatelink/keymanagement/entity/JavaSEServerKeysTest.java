package com.airhacks.gatelink.keymanagement.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class JavaSEServerKeysTest {
    
    @Test
    void stripLeadingZeros() {
        var given = new byte[]{0,0,1,2,3};
        var expected = new byte[]{1,2,3};
        var actual = JCEServerKeys.stripLeadingZeros(given);
        assertThat(actual).isEqualTo(expected);
    }
}
