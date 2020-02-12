
package com.airhacks.gatelink.subscriptions.control;

import com.airhacks.gatelink.subscriptions.entity.Subscription;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.metrics.annotation.Gauge;

/**
 *
 * @author airhacks.com
 */
@ApplicationScoped
public class SubscriptionsStore {

    private ConcurrentMap<String, Subscription> store;

    @PostConstruct
    public void initialize() {
        this.store = new ConcurrentHashMap<>();
    }

    @Gauge(unit = "count")
    public int numberOfSubscriptions() {
        return this.store.size();
    }

    public void addSubscription(Subscription subscription) {
        this.store.put(subscription.endpoint, subscription);
    }

    public List<Subscription> all() {
        return new ArrayList<>(this.store.values());
    }

    public void removeAll() {
        this.store.clear();
    }

    public void remove(String endpoint) {
        this.store.remove(endpoint);
    }

}
