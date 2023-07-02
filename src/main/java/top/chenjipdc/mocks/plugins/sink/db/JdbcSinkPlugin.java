package top.chenjipdc.mocks.plugins.sink.db;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.SinkConfig;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
public abstract class JdbcSinkPlugin<T extends SinkConfig> extends AbstractSinkPlugin<T> {

    protected Connection connection;

    protected PreparedStatement preparedStatement;

    protected int batch = 0;

    public void init(Config.SinksConfig config) {
        super.init(config);

        this.config = config;
        startTiming();
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
        String insert = "insert into " + (doubleQuote ? doubleQuote(tableName) : tableName) + " (";
        insert += config.getMappings()
                .keySet()
                .stream()
                .map(it -> doubleQuote ? doubleQuote(it) : it)
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

    public String doubleQuote(String str) {
        return "\"" + str + "\"";
    }

    public String quote(String str) {
        return "'" + str + "'";
    }
}
