package com.kp.jsonserver.jsonserver.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.jsonserver.jsonserver.models.Database;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class DatabaseManager {

    public Database getData() throws Exception{
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            Database database = objectMapper.readValue(new File("src/main/resources/store.json"), Database.class);
            return database;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void updateData(Database db) throws Exception{
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File("src/main/resources/store.json"), db);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
