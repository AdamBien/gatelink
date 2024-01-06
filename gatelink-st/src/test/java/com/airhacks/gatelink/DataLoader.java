
package com.airhacks.gatelink;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

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

    public static JsonArray readDocuments(String fileName) throws FileNotFoundException {
        try ( JsonReader reader = Json.createReader(new FileInputStream("src/test/resources/" + fileName))) {
            return reader.readArray();
        }
    }

}
