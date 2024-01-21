package com.airhacks.gatelink.bytes.control;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

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


    @Test
    void parseHex(){
        var parsed = Bytes.parseHex("1");
        assertThat(parsed).hasSize(1);
        var input = "CAFEBABE";
        var result = Bytes.parseHex(input);
        assertNotNull(result);
        assertThat(result).hasSize(input.length()/2);

        var oneF = Bytes.parseHex("F");
        var decimalRepresentation = Byte.valueOf(oneF[0]).intValue();
        assertThat(decimalRepresentation).isEqualTo(15);
    }
}
