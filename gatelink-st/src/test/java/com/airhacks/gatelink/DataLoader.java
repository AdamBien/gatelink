
package com.airhacks.gatelink;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

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
