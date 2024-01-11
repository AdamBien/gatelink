
package com.airhacks.gatelink.keymanagement.boundary;

import com.airhacks.gatelink.keymanagement.control.BCKeyGenerator;
import com.airhacks.gatelink.keymanagement.control.JCEKeyGenerator;
import com.airhacks.gatelink.keymanagement.entity.BCServerKeys;
import com.airhacks.gatelink.keymanagement.entity.JCEServerKeys;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
public class KeyStore {

    private JCEServerKeys keys;


    @PostConstruct
    public void initializeProvider() {
        this.keys = JCEKeyGenerator.generateVapidKeys();
        this.keys.logKeys();
    }

    public JCEServerKeys getKeys() {
        return this.keys;
    }

}
