package top.chenjipdc.mocks.plugins.sink.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.db.HBaseSinkConfig;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;
import top.chenjipdc.mocks.plugins.sink.SinkPlugin;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AutoService(SinkPlugin.class)
public class HBaseSinkPlugin extends AbstractSinkPlugin<HBaseSinkConfig> {

    private HBaseSinkConfig sinkConfig;

    private List<Map<String, Object>> cacheValues = new ArrayList<>();

    private Connection connection;

    private Table table;

    @Override
    public String type() {
        return "hbase";
    }

    @SneakyThrows
    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        sinkConfig = JSONObject.parseObject(config.getConfig(),
                HBaseSinkConfig.class);

        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.property.clientPort",
                String.valueOf(sinkConfig.getPort()));
        // 如果是集群 则主机名用逗号分隔
        conf.set("hbase.zookeeper.quorum",
                sinkConfig.getQuorum());

        connection = ConnectionFactory.createConnection(conf);

        TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(sinkConfig.getTable()))
                .setColumnFamilies(sinkConfig.getFamilies()
                        .keySet()
                        .stream()
                        .map(ColumnFamilyDescriptorBuilder::of)
                        .collect(Collectors.toList()))
                .build();
        Admin admin = connection.getAdmin();
        // 自动创建表
        if (!admin.tableExists(TableName.valueOf(sinkConfig.getTable()))) {
            admin.createTable(tableDescriptor);
        }

        table = connection.getTable(TableName.valueOf(sinkConfig.getTable()));
    }

    @Override
    public void sink(Map<String, Object> values) {
        if (sinkConfig.getBatch() > 1) {
            cacheValues.add(values);
            if (cacheValues.size() >= sinkConfig.getBatch()) {
                saveBatch();
            }
        } else {
            saveOne(values);
        }
    }

    private void saveBatch() {
        if (cacheValues == null || cacheValues.size() == 0) {
            return;
        }
        puts(cacheValues);
        cacheValues = new ArrayList<>();
    }

    @SneakyThrows
    private void saveOne(Map<String, Object> values) {
        puts(Collections.singletonList(values));
    }

    @SneakyThrows
    private void puts(List<Map<String, Object>> values) {
        final List<Put> putList = new ArrayList<>();
        values.forEach(value -> {
            Map<String, Object> map = mappingsConvert(value);
            Put put = new Put(map.get(sinkConfig.getRowKey())
                    .toString()
                    .getBytes(StandardCharsets.UTF_8));
            sinkConfig.getFamilies()
                    .forEach((k, v) -> {
                        for (String column : v) {
                            put.addColumn(k.getBytes(StandardCharsets.UTF_8),
                                    column.getBytes(StandardCharsets.UTF_8),
                                    map.get(column)
                                            .toString()
                                            .getBytes(StandardCharsets.UTF_8));
                        }
                    });
            putList.add(put);
        });

        table.put(putList);
    }

    @SneakyThrows
    @Override
    public void stop() {

        saveBatch();

        connection.close();

        super.stop();
    }
}
