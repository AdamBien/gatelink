
package com.airhacks.gatelink.keymanagement.boundary;

import com.airhacks.gatelink.keymanagement.control.BCKeyGenerator;
import com.airhacks.gatelink.keymanagement.entity.BCServerKeys;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
public class KeyStore {

    private BCServerKeys keys;

    @Inject
    BCKeyGenerator generator;

    @PostConstruct
    public void initializeProvider() {
        this.keys = this.generator.generateVapidKeys();
        this.keys.logKeys();
    }

    public BCServerKeys getKeys() {
        return this.keys;
    }

}
