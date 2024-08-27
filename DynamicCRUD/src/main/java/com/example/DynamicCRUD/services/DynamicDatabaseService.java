package com.example.DynamicCRUD.services;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DynamicDatabaseService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String insert(LinkedHashMap<String, Object> map) {
        String tableName = (String) map.get("Table");

        if (tableExists(tableName)) {
            LinkedHashMap<String, Object> values = (LinkedHashMap<String, Object>) map.get("Values");
            insertIntoTable(tableName, values);
            return "Record inserted into " + tableName;
        } else {
            return "Table " + tableName + " does not exist. Please create the table first.";
        }
    }

    private boolean tableExists(String tableName) {
        String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{tableName.toUpperCase()}, Integer.class);
        return count != null && count > 0;
    }

    private void insertIntoTable(String tableName, LinkedHashMap<String, Object> values) {
        String columns = String.join(", ", values.keySet());
        String placeholders = values.keySet().stream().map(k -> "?").collect(Collectors.joining(", "));
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);

        jdbcTemplate.update(sql, values.values().toArray());
    }

    // update

    public String update(String tableName, LinkedHashMap<String, Object> values, Object id) {
        if (tableExists(tableName)) {
            updateTable(tableName, values, id);
            return "Record updated in " + tableName;
        } else {
            return "Table " + tableName + " does not exist. Update failed.";
        }
    }

    private void updateTable(String tableName, LinkedHashMap<String, Object> values, Object id) {
        String setClause = values.entrySet().stream()
                .map(entry -> entry.getKey() + " = ?")
                .collect(Collectors.joining(", "));

        String sql = String.format("UPDATE %s SET %s WHERE ID = ?", tableName, setClause);

        Object[] args = concat(values.values().toArray(), new Object[]{id});
        jdbcTemplate.update(sql, args);
    }

    private Object[] concat(Object[] first, Object[] second) {
        Object[] result = new Object[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    // delete

    public String delete(String tableName, Object id) {
        if (tableExists(tableName)) {
            deleteFromTable(tableName, id);
            return "Record deleted from " + tableName;
        } else {
            return "Table " + tableName + " does not exist. Delete failed.";
        }
    }

    private void deleteFromTable(String tableName, Object id) {
       
        String sql = String.format("DELETE FROM %s WHERE ID = ?", tableName);
        jdbcTemplate.update(sql, id);
    }
}
