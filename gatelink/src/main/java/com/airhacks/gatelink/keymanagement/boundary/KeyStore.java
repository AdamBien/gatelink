
package com.airhacks.gatelink.keymanagement.boundary;

import com.airhacks.gatelink.keymanagement.control.KeyGenerator;
import com.airhacks.gatelink.keymanagement.entity.ServerKeys;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
public class KeyStore {

    private ServerKeys keys;

    @Inject
    KeyGenerator generator;

    @PostConstruct
    public void initializeProvider() {
        this.keys = this.generator.generateVapidKeys();
        this.keys.logKeys();
    }

    public ServerKeys getKeys() {
        return this.keys;
    }

}
