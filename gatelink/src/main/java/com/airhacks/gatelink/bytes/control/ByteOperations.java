package com.airhacks.gatelink.bytes.control;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HexFormat;

public interface ByteOperations {

    static byte[] concat(byte[]... arrays) {
        var capacity = 0;
        for (var array : arrays) {
            if (array != null)
                capacity += array.length;
        }
        var buffer = ByteBuffer.allocate(capacity);
        for (var array : arrays) {
            if (array != null)
                buffer.put(array);
        }
        return buffer.array();
    }

    static ByteBuffer convert(byte[] buffer) {
        return ByteBuffer.wrap(buffer);
    }

    static byte[] parseHex(String input) {
        if (input.length() % 2 == 1) {
            input = "0" + input;
        }
        return HexFormat
                .of()
                .parseHex(input);
    }

    static byte[] unsignedIntToBytes(int number) {
        byte result[] = new byte[2];
        result[1] = (byte) (number & 0xFF);
        result[0] = (byte) (number >> 8);
        return result;
    }

    public static BigInteger fromUnsignedByteArray(byte[] buf, int off, int length) {
        byte[] magnitude = buf;
        if (off != 0 || length != buf.length) {
            magnitude = new byte[length];
            System.arraycopy(buf, off, magnitude, 0, length);
        }
        return new BigInteger(1, magnitude);
    }

    static byte[] stripLeadingZeros(byte[] data) {
        int nonZeroIndex = 0;
        while (nonZeroIndex < data.length && data[nonZeroIndex] == 0)
            nonZeroIndex++;
        return Arrays.copyOfRange(data, nonZeroIndex, data.length);
    }

}
