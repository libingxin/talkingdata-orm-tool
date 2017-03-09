/*
 * The Talking Data ORM Tool Open Foundation licenses
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 *
 * @ author: bingxin.li@tendcloud.com
 */
package com.talkingdata.orm.tool;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlParser {
    private static final String SQL_TABLE = "SELECT"
            + " information_schema.`TABLES`.TABLE_NAME tableName"
            + " FROM"
            + " information_schema.`TABLES`"
            + " WHERE"
            + " information_schema.`TABLES`.TABLE_TYPE = 'BASE TABLE'"
            + " AND information_schema.`TABLES`.TABLE_SCHEMA = '%s'";
    private static final String SQL_COLUMN = "SHOW COLUMNS FROM %s.`%s`";

    private static final Map<String, String> DATA_MAP;

    static {
        DATA_MAP = new HashMap<>();

        DATA_MAP.put("CHAR", "String");
        DATA_MAP.put("VARCHAR", "String");
        DATA_MAP.put("LONGVARCHAR", "String");
        DATA_MAP.put("LONGTEXT", "String");
        DATA_MAP.put("NUMERIC", "BigDecimal");
        DATA_MAP.put("DECIMAL", "BigDecimal");
        DATA_MAP.put("BIT", "Boolean");
        DATA_MAP.put("TINYINT", "Integer");
        DATA_MAP.put("SMALLINT", "Short");
        DATA_MAP.put("INTEGER", "Integer");
        DATA_MAP.put("INT", "Integer");
        DATA_MAP.put("BIGINT", "Long");
        DATA_MAP.put("REAL", "Float");
        DATA_MAP.put("FLOAT", "Double");
        DATA_MAP.put("DOUBLE", "Double");
        DATA_MAP.put("BINARY", "byte[]");
        DATA_MAP.put("VARBINARY", "byte[]");
        DATA_MAP.put("LONGVARBINARY", "byte[]");
        DATA_MAP.put("DATE", "Date");
        DATA_MAP.put("DATETIME", "Date");
        DATA_MAP.put("TIME", "Time");
        DATA_MAP.put("TIMESTAMP", "Date");
    }

    public List<ClassDefinition> getTables(final JdbcTemplate jdbcTemplate, String db, final String packageName) {
        final List<ClassDefinition> list = new ArrayList<>();
        String sqlTable = String.format(SQL_TABLE, db);
        jdbcTemplate.query(sqlTable, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String table = rs.getString("tableName");
                final List<ClassDefinitionColumn> columns = new ArrayList<ClassDefinitionColumn>();
                ClassDefinition classDefinition = new ClassDefinition(packageName, table);
                list.add(classDefinition);
                classDefinition.setColumns(columns);

                String sqlColumn = String.format(SQL_COLUMN, db, table);
                jdbcTemplate.query(sqlColumn, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        String columnName = rs.getString("Field");
                        String columnType = rs.getString("Type").replaceAll("\\(\\d+\\)", "").toUpperCase();
                        System.out.println(columnType);
                        String primaryKey = rs.getString("Key");
                        columns.add(new ClassDefinitionColumn(columnName, DATA_MAP.get(columnType), primaryKey));
                    }
                });
            }
        });

        return list;
    }
}
