
package com.airhacks.gatelink;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

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
