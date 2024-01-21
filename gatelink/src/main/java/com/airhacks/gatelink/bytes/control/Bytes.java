package com.airhacks.gatelink.bytes.control;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.HexFormat;

public interface Bytes {

    static byte[] concat(byte[]... arrays) {
        try (var stream = new ByteArrayOutputStream()) {
            for (var array : arrays) {
                if (array == null) {
                    continue;
                }
                stream.write(array);
            }
            return stream.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Array addition failed", ex);

        }
    }

    static byte[] getBytes(String content) {
        try {
            return content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Unsupported encoding", ex);
        }
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

}
