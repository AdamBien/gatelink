
package com.airhacks.gatelink;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

/**
 *
 * @author airhacks.com
 */
public interface DataLoader {

    public static JsonObject readDocument(String fileName) throws FileNotFoundException {
        try ( JsonReader reader = Json.createReader(new FileInputStream("src/test/resources/" + fileName))) {
            return reader.readObject();
        }
    }

    public static <T> T fromJson(String fileName, Class<T> type) throws FileNotFoundException, IOException {
        try ( Reader reader = new FileReader("src/test/resources/" + fileName)) {
            Jsonb build = JsonbBuilder.newBuilder().build();
            return build.fromJson(reader, type);
        }
    }

}
