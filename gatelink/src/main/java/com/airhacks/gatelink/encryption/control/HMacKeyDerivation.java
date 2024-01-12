package com.airhacks.gatelink.encryption.control;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;

public interface HMacKeyDerivation {

    static byte[] derive(byte[] ikm, byte[] salt, byte[] info, int length) {
        var hkdf = new HKDFBytesGenerator(new SHA256Digest());
        hkdf.init(new HKDFParameters(ikm, salt, info));
        var okm = new byte[length];
        hkdf.generateBytes(okm, 0, length);
        return okm;
    }
}
