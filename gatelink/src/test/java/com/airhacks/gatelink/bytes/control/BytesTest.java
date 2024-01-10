package com.airhacks.gatelink.bytes.control;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class BytesTest {
    @Test
    void unsignedIntToBytes() {
        byte result[] = Bytes.unsignedIntToBytes(42);
        assertThat(result).isNotNull();
        assertThat((int) result[0]).isEqualTo((int) 0);
        assertThat((int) result[1]).isEqualTo((int) 42);
        assertThat(result.length).isEqualTo(2);
    }
}
