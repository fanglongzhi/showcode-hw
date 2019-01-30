package com.gmcc.msb.config.utils;

import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropertiesResultSetExtractor implements ResultSetExtractor<Map<String, String>> {
    @Override
    public Map<String, String> extractData(ResultSet rs) throws SQLException {
        LinkedHashMap map = new LinkedHashMap();

        while(rs.next()) {
            map.put(rs.getString(1), rs.getString(2));
        }

        return map;
    }
}