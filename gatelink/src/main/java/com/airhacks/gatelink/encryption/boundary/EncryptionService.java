
package com.airhacks.gatelink.encryption.boundary;

import com.airhacks.gatelink.encryption.control.Encryptor;
import com.airhacks.gatelink.encryption.entity.EncryptedContent;
import com.airhacks.gatelink.keymanagement.entity.ServerKeys;
import com.airhacks.gatelink.notifications.boundary.Notification;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import jakarta.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import jakarta.inject.Inject;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.eclipse.microprofile.metrics.annotation.Metered;

/**
 * https://docs.oracle.com/en/java/javase/21/security/oracle-providers.html#GUID-091BF58C-82AB-4C9C-850F-1660824D5254
 * @author airhacks.com
 */
public class EncryptionService {

    @Inject
    Encryptor encryptor;

    private SecureRandom random;

    @PostConstruct
    public void init() {
        this.random = new SecureRandom();

    }

    @Metered
    public byte[] getNextSalt() {
        /*
        *  https://www.rfc-editor.org/rfc/rfc8291.html
        *   3.4.  Encryption Summary
        *   salt = random(16)
         */
        byte[] salt = new byte[16];
        this.random.nextBytes(salt);
        return salt;
    }


    public EncryptedContent encrypt(Notification notification, ServerKeys keys) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        KeyPair ephemeralLocalKeys = this.generateEphemeralKeys();
        ECPublicKey serverEphemeralPublic = (ECPublicKey) ephemeralLocalKeys.getPublic();
        ECPrivateKey ephemeralPrivateKey = (ECPrivateKey) ephemeralLocalKeys.getPrivate();
        byte[] salt = this.getNextSalt();
        byte[] encryptedContent = this.encryptor.encrypt(notification, keys, serverEphemeralPublic, ephemeralPrivateKey, salt);
        return new EncryptedContent(encryptedContent, salt, serverEphemeralPublic);

    }

    public KeyPair generateEphemeralKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        ECNamedCurveParameterSpec parameterSpec = Encryptor.getCurveParameterSpec();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDH", "BC");
        keyPairGenerator.initialize(parameterSpec);

        return keyPairGenerator.generateKeyPair();
    }


}
