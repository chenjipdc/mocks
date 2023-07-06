package top.chenjipdc.mocks.plugins.mock.db;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.db.JdbcMockConfig;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public abstract class JdbcMockPlugin<C extends JdbcMockConfig> extends AbstractMockPlugin<Object, C> {

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        initConfig(config.getConfig());
        init();
    }

    public abstract void initConfig(String config);

    public void initProperties(Properties props){
        props.setProperty("user",
                mockConfig.getUsername());
        props.setProperty("password",
                mockConfig.getPassword());
    }

    @SneakyThrows
    private void init() {
        Class.forName(mockConfig.getDriverClass());
        Properties props = new Properties();

        initProperties(props);
        try (Connection connection = DriverManager.getConnection(
                mockConfig.getJdbcUrl(),
                props)) {

            try (Statement stmt = connection.createStatement()) {
                String sql = querySql();
                if (mockConfig.getLogSql()) {
                    log.info(sql);
                }
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    for (String column : columns) {
                        Object object = rs.getObject(column);
                        object = valueConvert(column,
                                object);
                        map.put(aliases.get(column),
                                object);
                    }
                    values.add(map);
                }
            }
        }
    }

    public Object valueConvert(String column, Object value) {
        if (value instanceof BigInteger) {
            return Long.parseLong(value.toString());
        }
        return value;
    }

    public String querySql() {
        String sql = "select " + columns.stream()
                .map(this::columnMap)
                .collect(Collectors.joining(",")) + " from " + tableMap(mockConfig.getTable());
        if (mockConfig.getLastSql() != null) {
            sql += " " + mockConfig.getLastSql();
        }
        return sql;
    }

    public String tableMap(String table) {
        return table;
    }

    public String columnMap(String column) {
        return column;
    }

    @Override
    public Map<String, Object> value() {
        return values.get(NumericUtils.nextInt(values.size()));
    }

    @Override
    public void stop() {
        super.stop();
    }
}
