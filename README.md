## info

由于`duckdb` 元数据表中物理表的`table type`是 `BASE TABLE`, `jpa dialect` 默认只用了 `TABLE`,所以需要扩展

参见`org.hibernate.tool.schema.extract.internal.InformationExtractorJdbcDatabaseMetaDataImpl#processSchemaResultSet`

`org.hibernate.tool.schema.extract.internal.AbstractInformationExtractorImpl#AbstractInformationExtractorImpl`

```java

@Test
@SneakyThrows
void aaaa() {
    DuckDBConnection conn = (DuckDBConnection) DriverManager.getConnection("jdbc:duckdb:demo.duckdb");
    DatabaseMetaData metaData = conn.getMetaData();
    ResultSet resultSet = metaData
            .getTables(null,
                    "main",
                    "%", new String[]{"TABLE", "VIEW", "BASE TABLE"});
    while (resultSet.next()) {
        System.out.println(resultSet.getString(1));
        System.out.println(resultSet.getString(2));
        System.out.println(resultSet.getString(3));
    }
}
```

## question
由于duckdb varchar 的元数据没有长度,需要将 varchar注册为 text 

`Specifying the length for the VARCHAR, STRING, and TEXT types is not required and has no effect on the system. Specifying the length will not improve performance or reduce storage space of the strings in the database. These variants variant is supported for compatibility reasons with other systems that do require a length to be specified for strings.`


```
    @Override
    protected void registerColumnTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.registerColumnTypes(typeContributions, serviceRegistry);
        final DdlTypeRegistry ddlTypeRegistry = typeContributions.getTypeConfiguration().getDdlTypeRegistry();
        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(VARCHAR, "text", this));
    }

```

 但是 6.5.3.Final 版本存在bug,导致执行了额外的alter ddl,无法跳过 ,实际上不影响

org.hibernate.tool.schema.internal.ColumnDefinitions#hasMatchingLength



6.6.2.Finale 版本修复了
```
        if ( !column.getSqlType( metadata ).contains("(") ) {
			// the DDL type does not explicitly specify a length,
			// and so we do not require an exact match
			return true;
		}
```


