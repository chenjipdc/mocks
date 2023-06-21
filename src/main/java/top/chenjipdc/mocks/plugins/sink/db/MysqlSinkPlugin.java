package top.chenjipdc.mocks.plugins.sink.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.db.MysqlSinkConfig;
import top.chenjipdc.mocks.plugins.SinkPlugin;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AutoService(SinkPlugin.class)
public class MysqlSinkPlugin extends AbstractSinkPlugin {

    private Connection connection;

    private MysqlSinkConfig mysqlConfig;

    private PreparedStatement preparedStatement;

    private int batch = 0;

    @Override
    public String type() {
        return "mysql";
    }

    @SneakyThrows
    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        Class.forName("com.mysql.cj.jdbc.Driver");

        MysqlSinkConfig mysqlConfig = JSONObject.parseObject(config.getConfig(),
                MysqlSinkConfig.class);

        connection = DriverManager.getConnection(
                mysqlConfig.getJdbcUrl(),
                mysqlConfig.getUsername(),
                mysqlConfig.getPassword());
        connection.setAutoCommit(true);

        this.mysqlConfig = mysqlConfig;
    }

    @SneakyThrows
    @Override
    public void sink(Map<String, Object> values) {
        String sql = insertSql(values);
        if (mysqlConfig.getLogSql()) {
            log.info(sql);
        }
        if (mysqlConfig.getBatch() > 1) {
            batchInsert(sql);
        } else {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            stmt.close();
        }
    }

    @SneakyThrows
    @Override
    public void stop() {
        if (preparedStatement != null) {
            preparedStatement.executeBatch();
            if (!preparedStatement.isClosed()) {
                preparedStatement.close();
            }
        }

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }

        super.stop();
    }

    @Override
    public String logPrefix() {
        return "表" + mysqlConfig.getTable();
    }

    @SneakyThrows
    private void batchInsert(String sql) {
        batch++;
        if (batch == 1) {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.addBatch();
        } else {
            preparedStatement.addBatch(sql);
        }

        if (batch >= mysqlConfig.getBatch()) {
            preparedStatement.executeBatch();
            preparedStatement.close();
            preparedStatement = null;
            batch = 0;
        }
    }

    private String insertSql(Map<String, Object> values) {
        String insert = "insert into " + mysqlConfig.getTable() + " (";
        insert += String.join(",",
                config.getMappings()
                        .keySet()) + ") values (";

        insert += config.getMappings()
                .values()
                .stream()
                .map(key -> wrapValue(values.get(key)).toString())
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
}
