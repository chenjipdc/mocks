package top.chenjipdc.mocks.plugins.sink.db;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.SinkConfig;
import top.chenjipdc.mocks.config.sink.db.JdbcSinkConfig;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;
import top.chenjipdc.mocks.utils.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;


@Slf4j
public abstract class JdbcSinkPlugin<T extends JdbcSinkConfig> extends AbstractSinkPlugin<T> {

    protected Connection connection;

    protected PreparedStatement preparedStatement;

    protected int batch = 0;

    public void init(Config.SinksConfig config) {
        super.init(config);

        initConfig(config.getConfig());

        init();
    }

    public abstract void initConfig(String config);

    public void initProperties(Properties props) {

    }

    @SneakyThrows
    private void init() {
        Class.forName(sinkConfig.getDriverClass());

        Properties props = new Properties();
        props.setProperty("user",
                sinkConfig.getUsername());
        props.setProperty("password",
                sinkConfig.getPassword());

        initProperties(props);

        connection = DriverManager.getConnection(
                sinkConfig.getJdbcUrl(),
                props);

        if (sinkConfig.getInitSql() != null) {
            logSql(sinkConfig.getInitSql());
            try (PreparedStatement statement = connection.prepareStatement(sinkConfig.getInitSql())) {
                statement.execute();
            }
        }
    }

    @Override
    public void sink(Map<String, Object> values) {
        String prepareSql = prepareSql(sinkConfig.getTable());
        logSql(prepareSql);
        if (sinkConfig.getBatch() > 1) {
            batchInsert(prepareSql,
                    values);
        } else {
            insertOne(prepareSql,
                    values);
        }
    }

    @SneakyThrows
    protected void preparedStatementValues(PreparedStatement stmt, Map<String, Object> values) {
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
    protected void insertOne(String prepareSql, Map<String, Object> values) {
        preparedStatement = connection.prepareStatement(prepareSql);
        preparedStatementValues(preparedStatement,
                values);
        preparedStatement.execute();
    }

    @SneakyThrows
    protected void batchInsert(String prepareSql, Map<String, Object> values) {
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

    @SneakyThrows
    public void stop() {
        super.stop();

        if (batch > 0) {
            preparedStatement.executeBatch();
        }

        if (!preparedStatement.isClosed()) {
            preparedStatement.close();
        }

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * @param tableName table name
     * @return insert into table (id, name) values (?, ?)
     */
    public String prepareSql(String tableName) {
        return prepareSql(tableName,
                false);
    }

    /**
     * @param tableName   table name
     * @param doubleQuote true:"table" and "column" / false: table and column
     * @return insert into "table" ("id", "name") values (?, ?)
     */
    public String prepareSql(String tableName, boolean doubleQuote) {
        String insert = "insert into " + (doubleQuote ? StringUtils.doubleQuote(tableName) : tableName) + " (";
        insert += config.getMappings()
                .keySet()
                .stream()
                .map(it -> doubleQuote ? StringUtils.doubleQuote(it) : it)
                .collect(Collectors.joining((CharSequence) ",")) + ") values (";

        insert += config.getMappings()
                .values()
                .stream()
                .map(key -> "?")
                .collect(Collectors.joining(","));
        insert += ")";
        return insert;
    }

    public Object wrapValue(Object value) {
        return value;
    }

    private void logSql(String sql) {
        if (sinkConfig.getLogSql()) {
            log.info(sql);
        }
    }

    @Override
    public String logPrefix() {
        return "表" + sinkConfig.getTable();
    }

}
