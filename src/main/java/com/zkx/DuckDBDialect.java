package com.zkx;

import org.hibernate.dialect.PostgreSQL10Dialect;

import java.util.List;

public class DuckDBDialect extends PostgreSQL10Dialect {
    /**
     * duckdb pg_sequences
     *
     * @return string
     */
    @Override
    public String getQuerySequencesString() {
        return "select * from pg_catalog.pg_sequences";
    }

    /**
     * duckdb 元数据 物理表标识为 BASE TABLE
     *
     * @param tableTypesList 元数据表类型
     */
    @Override
    public void augmentPhysicalTableTypes(List<String> tableTypesList) {
        tableTypesList.add("BASE TABLE");
    }
}