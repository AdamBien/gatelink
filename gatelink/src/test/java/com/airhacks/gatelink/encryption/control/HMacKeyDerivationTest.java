package com.airhacks.gatelink.encryption.control;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HexFormat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.airhacks.gatelink.bytes.control.ByteOperations;

/**
 * Testcases adopted from:
 * from https://www.rfc-editor.org/rfc/rfc5869 / Appendix A
 */
public class HMacKeyDerivationTest {
    @Test
    @DisplayName("A.1.  Test Case 1 / Basic test case with SHA-256")
    void deriveTestCase1() {

        var IKM = ByteOperations.parseHex("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b"); // (22 octets)
        var salt = ByteOperations.parseHex("000102030405060708090a0b0c"); // (13 octets)
        var info = ByteOperations.parseHex("f0f1f2f3f4f5f6f7f8f9"); // (10 octets)
        var L = 42;

        var expectedOKM = "3cb25f25faacd57a90434f64d0362f2a2d2d0a90cf1a5a4c5db02d56ecc4c5bf34007208d5b887185865";

        var OKM = HMacKeyDerivation.derive(IKM, salt, info, L);
        var OKMString = HexFormat.of().formatHex(OKM);
        assertThat(OKMString).isEqualTo(expectedOKM);
    }

    @Test
    @DisplayName("A.2.  Test Case 2 /  Test with SHA-256 and longer inputs/outputs")
    void deriveTestCase2() {

        var IKM = ByteOperations.parseHex("""
                000102030405060708090a0b0c0d0e0f\
                101112131415161718191a1b1c1d1e1f\
                202122232425262728292a2b2c2d2e2f\
                303132333435363738393a3b3c3d3e3f\
                404142434445464748494a4b4c4d4e4f\
                """); // (80 octets)
        var salt = ByteOperations.parseHex("""
                606162636465666768696a6b6c6d6e6f\
                707172737475767778797a7b7c7d7e7f\
                808182838485868788898a8b8c8d8e8f\
                909192939495969798999a9b9c9d9e9f\
                a0a1a2a3a4a5a6a7a8a9aaabacadaeaf\
                """); // (80 octets)
        var info = ByteOperations.parseHex("""
                b0b1b2b3b4b5b6b7b8b9babbbcbdbebf\
                c0c1c2c3c4c5c6c7c8c9cacbcccdcecf\
                d0d1d2d3d4d5d6d7d8d9dadbdcdddedf\
                e0e1e2e3e4e5e6e7e8e9eaebecedeeef\
                f0f1f2f3f4f5f6f7f8f9fafbfcfdfeff\
                """); // (80 octets)
        var L = 82;

        var expectedOKM = """
                b11e398dc80327a1c8e7f78c596a4934\
                4f012eda2d4efad8a050cc4c19afa97c\
                59045a99cac7827271cb41c65e590e09\
                da3275600c2f09b8367793a9aca3db71\
                cc30c58179ec3e87c14c01d5c1f3434f\
                1d87\
                """;

        var OKM = HMacKeyDerivation.derive(IKM, salt, info, L);
        var OKMString = HexFormat.of().formatHex(OKM);
        assertThat(OKMString).isEqualTo(expectedOKM);
    }

    @Test
    @DisplayName("A.3.  Test Case 3 /  Test with SHA-256 and zero-length salt/info")
    void deriveTestCase3() {

        var IKM = ByteOperations.parseHex("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b");
        var salt = ByteOperations.parseHex(""); // (22 octets)
        var info = ByteOperations.parseHex(""); // (0 octets)
        var L = 42;

        var expectedOKM = """
                8da4e775a563c18f715f802a063c5a31\
                b8a11f5c5ee1879ec3454e5f3c738d2d\
                9d201395faa4b61a96c8\
                """; //(42 octets)

        var OKM = HMacKeyDerivation.derive(IKM, salt, info, L);
        var OKMString = HexFormat.of().formatHex(OKM);
        assertThat(OKMString).isEqualTo(expectedOKM);
    }
}
