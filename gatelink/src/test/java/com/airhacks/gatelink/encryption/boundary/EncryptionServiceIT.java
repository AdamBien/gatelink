/*
 */
package com.airhacks.gatelink.encryption.boundary;

import org.junit.jupiter.api.BeforeEach;

import com.airhacks.gatelink.encryption.control.EncryptionFlow;

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
    }

    public EncryptionService getCut() {
        this.init();
        return cut;
    }

}
