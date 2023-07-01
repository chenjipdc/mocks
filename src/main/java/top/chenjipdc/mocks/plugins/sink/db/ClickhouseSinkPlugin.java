package top.chenjipdc.mocks.plugins.sink.db;

import com.alibaba.fastjson2.JSONObject;
import com.clickhouse.jdbc.ClickHouseDataSource;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.db.ClickhourceSinkConfig;
import top.chenjipdc.mocks.plugins.SinkPlugin;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AutoService(SinkPlugin.class)
public class ClickhouseSinkPlugin extends AbstractSinkPlugin {

    private Connection connection;

    private ClickhourceSinkConfig sinkConfig;

    private PreparedStatement preparedStatement;

    private int batch = 0;

    @Override
    public String type() {
        return "clickhouse";
    }

    @SneakyThrows
    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        Class.forName("com.clickhouse.jdbc.ClickHouseDriver");

        sinkConfig = JSONObject.parseObject(config.getConfig(),
                ClickhourceSinkConfig.class);

        ClickHouseDataSource dataSource = new ClickHouseDataSource(sinkConfig.getJdbcUrl());

        connection = dataSource.getConnection(
                sinkConfig.getUsername(),
                sinkConfig.getPassword());
    }


    @Override
    public void sink(Map<String, Object> values) {
        String prepareSql = prepareSql();
        if (sinkConfig.getLogSql()) {
            log.info(prepareSql);
        }
        if (sinkConfig.getBatch() > 1) {
            batchInsert(prepareSql,
                    values);
        } else {
            insertOne(prepareSql,
                    values);
        }
    }

    @SneakyThrows
    private void preparedStatementValues(PreparedStatement stmt, Map<String, Object> values) {
        List<String> list = config.getMappings()
                .values()
                .stream()
                .map(key -> wrapValue(values.get(key)).toString())
                .collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            stmt.setObject(i + 1,
                    list.get(i));
        }
    }

    @SneakyThrows
    private void insertOne(String prepareSql, Map<String, Object> values) {
        preparedStatement = connection.prepareStatement(prepareSql);
        preparedStatementValues(preparedStatement,
                values);
        preparedStatement.execute();
    }

    @SneakyThrows
    private void batchInsert(String prepareSql, Map<String, Object> values) {
        batch++;
        if (batch == 1) {
            preparedStatement = connection.prepareStatement(prepareSql);
        }
        preparedStatementValues(preparedStatement,
                values);
        preparedStatement.addBatch();

        if (batch >= sinkConfig.getBatch()) {
            preparedStatement.executeBatch();
            batch = 0;
        }
    }

    private String prepareSql() {
        String insert = "insert into " + sinkConfig.getTable() + " (";
        insert += String.join(",",
                config.getMappings()
                        .keySet()) + ") values (";

        insert += config.getMappings()
                .values()
                .stream()
                .map(key -> "?")
                .collect(Collectors.joining(","));
        insert += ");";
        return insert;
    }

    private Object wrapValue(Object value) {
        if (value instanceof CharSequence) {
            return "'" + value + "'";
        } else if (value instanceof Date) {
            return ((Date) value).getTime();
        }
        return value;
    }

    @SneakyThrows
    @Override
    public void stop() {
        if (batch > 0) {
            preparedStatement.executeBatch();
        }

        if (!preparedStatement.isClosed()) {
            preparedStatement.close();
        }

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }

        super.stop();
    }

    @Override
    public String logPrefix() {
        return "表" + sinkConfig.getTable();
    }

}
