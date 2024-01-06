/*
 */
package com.airhacks.gatelink.health;

import com.airhacks.gatelink.SystemTest;
import java.util.List;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
/**
 *
 * @author airhacks.com
 */
public class HealthCheckIT {

    private WebTarget tut;

    @BeforeEach
    public void init() {
        this.tut = SystemTest.context("health");
    }

    /**
     * {"checks":[{"data":{},"name":"pushserver","state":"UP"}],"outcome":"UP"}
     */
    @Test
    public void health() {
        Response response = this.tut.request().get();
        assertThat(response.getStatus()).isEqualTo(200);
        JsonObject health = response.readEntity(JsonObject.class);
        JsonArray checks = health.getJsonArray("checks");
        List<JsonObject> checkList = checks.getValuesAs(JsonObject.class);
        long checkCount = checkList.
                stream().
                filter(check -> "pushserver".equals(check.getString("name"))).count();
        assertThat(checkCount).isEqualTo(1l);
    }


}
