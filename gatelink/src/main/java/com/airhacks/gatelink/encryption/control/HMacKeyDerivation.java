package com.airhacks.gatelink.encryption.control;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.airhacks.gatelink.bytes.control.ByteOperations;

/**
 * Implementation is based on: RFC 5869
 * https://www.rfc-editor.org/rfc/rfc5869
 */
public interface HMacKeyDerivation {

    int BLOCKSIZE = 256 / 8;
    /**
     * https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html#mac-algorithms
     */
    String MAC_ALGORITHM = "HmacSHA256";


    /**
     * Step 1: Extract
     * 
     * HKDF-Extract(salt, IKM) -> PRK
     * 
     * Options:
     * Hash a hash function; HashLen denotes the length of the
     * hash function output in octets
     * 
     * Inputs:
     * @param salt optional salt value (a non-secret random value);
     * if not provided, it is set to a string of HashLen zeros.
     * @param IKM input keying material
     * 
     * Output:
     * PRK a pseudorandom key (of HashLen octets)
     * 
     * The output PRK is calculated as follows:
     * 
     * PRK = HMAC-Hash(salt, IKM)
     * 
     * @return  PRK a pseudorandom key (of HashLen octets)
     */
    static byte[] extract(byte[] salt, byte[] ikm) {
        var mac = mac(salt);
        return process(mac, ikm);
    }

    /**
     * HKDF-Expand(PRK, info, L) -> OKM
     * 
     * Options:
     * Hash a hash function; HashLen denotes the length of the
     * hash function output in octets
     * 
     * 
     * Krawczyk & Eronen Informational [Page 3]
     * 
     * RFC 5869 Extract-and-Expand HKDF May 2010
     * 
     * 
     * Inputs:
     * @param prk (PRK) a pseudorandom key of at least HashLen octets
     * (usually, the output from the extract step)
     * @param info optional context and application specific information
     * (can be a zero-length string)
     * @param len (L) length of output keying material in octets (<= 255*HashLen)
     * 
     * Output:
     * @return OKM output keying material (of L octets)
     */
    static byte[] expand(byte[] prk, byte[] info, int len) {

        var mac = mac(prk);

        //T(0) = empty string (zero length)
        //T(1) = HMAC-Hash(PRK, T(0) | info | 0x01)
        //T(2) = HMAC-Hash(PRK, T(1) | info | 0x02)
        byte[] T = {};
        
        //T = T(1) | T(2) | T(3) | ... | T(N)
        byte[] Tn = {};

        // N = ceil(L/HashLen)
        int N = (int) Math.ceil(((double) len) / ((double) BLOCKSIZE));
        for (int i = 0; i < N; i++) {
            //T(1) = HMAC-Hash(PRK, T(0) | info | 0x01)
            var iteration = ByteOperations.parseHex(Integer.toHexString(i + 1));
            var bytes = ByteOperations.concat(Tn, info, iteration);
            Tn = process(mac, bytes);
            T = ByteOperations.concat(T, Tn);
        }

        return Arrays.copyOfRange(T, 0, len);
    }

    public static byte[] derive(byte[] ikm, byte[] salt, byte[] info, int length) {
        var pseudoRandomKey = extract(salt, ikm);
        return expand(pseudoRandomKey, info, length);
    }


    /**
    * A MAC provides a way to check the integrity of information transmitted over or stored in an unreliable medium, based on a secret key. 
    Typically, message authentication codes are used between two parties that share a secret key in order to validate information transmitted between these parties.     
    * @see {javax.crypto.Mac}
    */
    public static Mac mac(byte[] key) {
        try {
            var mac = Mac.getInstance(MAC_ALGORITHM);
            var secretKey = secretKey(key);
            mac.init(secretKey);
            return mac;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("Mac initialization failed", e);
        }

    }

    static byte[] process(Mac hasher, byte[] message) {
        hasher.update(message);
        return hasher.doFinal();
    }

    static Key secretKey(byte[] key) {
        if (key.length == 0) {
            key = new byte[BLOCKSIZE];
        }
        return new SecretKeySpec(key, MAC_ALGORITHM);
    }


}
