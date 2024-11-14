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