package top.chenjipdc.mocks.plugins.mock.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.db.MysqlMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

@AutoService(MockPlugin.class)
public class MysqlMockPlugin extends AbstractMockPlugin<Object, MysqlMockConfig> {

    private final List<Map<String, Object>> values = new ArrayList<>();

    @Override
    public String type() {
        return "mysql";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        mockConfig = JSONObject.parseObject(config.getConfig(),
                MysqlMockConfig.class);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    mockConfig.getJdbcUrl(),
                    mockConfig.getUsername(),
                    mockConfig.getPassword());
            Statement stmt = con.createStatement();
            String sql = "select " + String.join(",",
                    columns) + " from " + mockConfig.getTable();
            if (mockConfig.getLastSql() != null) {
                sql += " " + mockConfig.getLastSql();
            }
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (String column : columns) {
                    Object object = rs.getObject(column);
                    if (object instanceof BigInteger) {
                        object = Long.parseLong(object.toString());
                    }
                    map.put(aliases.get(column),
                            object);
                }
                values.add(map);
            }
            stmt.close();
            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> value() {
        return values.get(NumericUtils.nextInt(values.size()));
    }
}
