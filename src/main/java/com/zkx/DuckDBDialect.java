package com.zkx;

import org.hibernate.dialect.PostgreSQL10Dialect;

import java.util.List;

public class DuckDBDialect extends PostgreSQL10Dialect {

    /**
     * duckdb varchar 没有长度,需要把varchar注册成text
     * org.hibernate.tool.schema.internal.AbstractSchemaMigrator#migrateTable(org.hibernate.mapping.Table, org.hibernate.tool.schema.extract.spi.TableInformation, org.hibernate.dialect.Dialect, org.hibernate.boot.Metadata, org.hibernate.engine.jdbc.internal.Formatter, org.hibernate.tool.schema.spi.ExecutionOptions, org.hibernate.boot.model.relational.SqlStringGenerationContext, org.hibernate.tool.schema.internal.exec.GenerationTarget...)
     * <p>
     * [https://duckdb.org/docs/sql/data_types/text#specifying-a-length-limit]
     */
    @Override
    protected void registerColumnTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.registerColumnTypes(typeContributions, serviceRegistry);
        final DdlTypeRegistry ddlTypeRegistry = typeContributions.getTypeConfiguration().getDdlTypeRegistry();
        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(VARCHAR, "text", this));

    }
   //解决分页问题 总是避免使用 FETCH子句   在springboot3 版本中, 如果继承了 org.hibernate.dialect.PostgreSQLDialect  则会由于开启fetch子句导致 分页查询异常
    @Override
    public boolean supportsFetchClause(FetchClauseType type) {
        return false;
    }
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
