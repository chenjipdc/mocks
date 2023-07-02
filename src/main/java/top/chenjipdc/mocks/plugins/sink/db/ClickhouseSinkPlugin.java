package top.chenjipdc.mocks.plugins.sink.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.db.ClickhourceSinkConfig;
import top.chenjipdc.mocks.plugins.SinkPlugin;

import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

@Slf4j
@AutoService(SinkPlugin.class)
public class ClickhouseSinkPlugin extends JdbcSinkPlugin<ClickhourceSinkConfig> {

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

        Properties props = new Properties();
        props.setProperty("user",
                sinkConfig.getUsername());
        props.setProperty("password",
                sinkConfig.getPassword());

        connection = DriverManager.getConnection(
                sinkConfig.getJdbcUrl(),
                props);
    }

    @Override
    public void sink(Map<String, Object> values) {
        String prepareSql = prepareSql(sinkConfig.getTable());
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

    @Override
    public String logPrefix() {
        return "表" + sinkConfig.getTable();
    }

}
