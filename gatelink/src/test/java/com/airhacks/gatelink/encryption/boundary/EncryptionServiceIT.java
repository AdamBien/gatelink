/*
 */
package com.airhacks.gatelink.encryption.boundary;

import org.junit.jupiter.api.BeforeEach;

import com.airhacks.gatelink.encryption.control.BCEncryptor;
import com.airhacks.gatelink.encryption.control.JCEEncryptor;

/**
 *
 * @author airhacks.com
 */
public class EncryptionServiceIT {

    private JCEEncryptionService cut;

    @BeforeEach
    public void init() {
        this.cut = new JCEEncryptionService();
        this.cut.init();
        this.cut.encryptor = new JCEEncryptor();
    }

    public JCEEncryptionService getCut() {
        this.init();
        return cut;
    }

}
