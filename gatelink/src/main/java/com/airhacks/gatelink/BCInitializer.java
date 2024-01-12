package com.airhacks.gatelink;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.airhacks.gatelink.log.boundary.Tracer;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Startup
@ApplicationScoped
public class BCInitializer {

    @Inject
    Tracer tracer;
    
    @PostConstruct
    void registerBouncyCastle(){
        tracer.log("registering BouncyCastle");
        Security.addProvider(new BouncyCastleProvider());
    }
}
