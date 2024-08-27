package com.example.DynamicCRUD.controller;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.DynamicCRUD.services.DynamicDatabaseService;

@RestController
public class DynamicDatabaseController {
    
      @Autowired
    private DynamicDatabaseService dynamicDatabaseService;

    @PostMapping("/insert")
    public String insert(@RequestBody LinkedHashMap<String, Object> map) {
        return dynamicDatabaseService.insert(map);
        
    }
     @PutMapping("/update/{id}")
    public String updateRecord(@PathVariable("id") Long id, @RequestBody LinkedHashMap<String, Object> map) {
        String tableName = (String) map.get("Table");
        LinkedHashMap<String, Object> values = (LinkedHashMap<String, Object>) map.get("Values");
        return dynamicDatabaseService.update(tableName, values, id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRecord(@PathVariable("id") Long id, @RequestBody LinkedHashMap<String, Object> map) {
        String tableName = (String) map.get("Table");
        return dynamicDatabaseService.delete(tableName, id);
    }
}
