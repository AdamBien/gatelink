/*
 */
package com.airhacks.gatelink.encryption.boundary;

import com.airhacks.gatelink.encryption.boundary.EncryptionService;
import com.airhacks.gatelink.encryption.control.BCEncryptor;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author airhacks.com
 */
public class EncryptionServiceIT {

    private EncryptionService cut;

    @BeforeEach
    public void init() {
        this.cut = new EncryptionService();
        this.cut.init();
        this.cut.encryptor = new BCEncryptor();
    }

    public EncryptionService getCut() {
        this.init();
        return cut;
    }

}
