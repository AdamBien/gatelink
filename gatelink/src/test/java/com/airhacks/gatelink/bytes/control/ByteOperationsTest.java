package com.airhacks.gatelink.bytes.control;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class ByteOperationsTest {
    @Test
    void unsignedIntToBytes() {
        byte result[] = ByteOperations.unsignedIntToBytes(42);
        assertThat(result).isNotNull();
        assertThat((int) result[0]).isEqualTo((int) 0);
        assertThat((int) result[1]).isEqualTo((int) 42);
        assertThat(result.length).isEqualTo(2);
    }


    @Test
    void parseHex(){
        var parsed = ByteOperations.parseHex("1");
        assertThat(parsed).hasSize(1);
        var input = "CAFEBABE";
        var result = ByteOperations.parseHex(input);
        assertNotNull(result);
        assertThat(result).hasSize(input.length()/2);

        var oneF = ByteOperations.parseHex("F");
        var decimalRepresentation = Byte.valueOf(oneF[0]).intValue();
        assertThat(decimalRepresentation).isEqualTo(15);
    }

    @Test
    void concat(){
        byte first[] = {0,1};
        byte second[] = {2,3};
        var result = ByteOperations.concat(first,second);
        assertThat(result).hasSize(4);
        assertThat(result[0]).isEqualTo((byte)0);
        assertThat(result[1]).isEqualTo((byte)1);
        assertThat(result[2]).isEqualTo((byte)2);
        assertThat(result[3]).isEqualTo((byte)3);
    }

    @Test
    void concatEmpty(){
        byte first[] = new byte[0];
        byte second[] = null;
        var result = ByteOperations.concat(first,second);
        assertThat(result).hasSize(0);
    }

    @Test
    void stripLeadingZeros() {
        var given = new byte[]{0,0,1,2,3};
        var expected = new byte[]{1,2,3};
        var actual = ByteOperations.stripLeadingZeros(given);
        assertThat(actual).isEqualTo(expected);
    }
}
