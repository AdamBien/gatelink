
package com.airhacks.gatelink.keymanagement.boundary;

import com.airhacks.gatelink.keymanagement.control.ECKeyGenerator;
import com.airhacks.gatelink.keymanagement.entity.ECKeys;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
public class KeyStore {

    private ECKeys keys;


    @PostConstruct
    public void initializeProvider() {
        this.keys = ECKeyGenerator.generateVapidKeys();
        this.keys.logKeys();
    }

    public ECKeys getKeys() {
        return this.keys;
    }

}
