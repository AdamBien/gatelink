
package com.airhacks.gatelink.keymanagement.control;

import com.airhacks.gatelink.encryption.control.Encryptor;
import com.airhacks.gatelink.keymanagement.boundary.KeyGenerationException;
import com.airhacks.gatelink.keymanagement.entity.ServerKeys;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
public class KeyGenerator {

    @PostConstruct
    public void initializeProvider() {
        Security.addProvider(new BouncyCastleProvider());

    }
    public ServerKeys generateVapidKeys() {

        ECNamedCurveParameterSpec parameterSpec = Encryptor.getCurveParameterSpec();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);

            keyPairGenerator.initialize(parameterSpec);
            KeyPair serverKey = keyPairGenerator.generateKeyPair();
            ECPrivateKey privateKey = (ECPrivateKey) serverKey.getPrivate();
            ECPublicKey publicKey = (ECPublicKey) serverKey.getPublic();
            return new ServerKeys(privateKey, publicKey);

        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException ex) {
            throw new KeyGenerationException(ex.getMessage());
        }
    }


}
