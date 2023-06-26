package top.chenjipdc.mocks.plugins.mock.db;


import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.db.MongoMockConfig;
import top.chenjipdc.mocks.plugins.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.math.BigInteger;
import java.util.*;

@AutoService(MockPlugin.class)
public class MongoMockPlugin extends AbstractMockPlugin<Object> {

    private MongoMockConfig mongoConfig;

    private final List<Map<String, Object>> values = new ArrayList<>();

    @Override
    public String type() {
        return "mongo";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);

        mongoConfig = JSONObject.parseObject(config.getConfig(),
                MongoMockConfig.class);
        String uri = "mongodb://";
        if (mongoConfig.getUsername() != null && mongoConfig.getPassword() != null) {
            uri += mongoConfig.getUsername() + ":" + mongoConfig.getPassword() + "@";
        }
        uri += mongoConfig.getAddress();
        if (mongoConfig.getConnectParams() != null) {
            uri += "?" + mongoConfig.getConnectParams();
        }
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(mongoConfig.getDatabase());
            MongoCollection<Document> collection = database.getCollection(mongoConfig.getCollection());
            FindIterable<Document> query;
            if (mongoConfig.getFindJson() != null) {
                Document find = Document.parse(mongoConfig.getFindJson());
                query = collection.find(find);
            } else {
                query = collection.find();
            }
            query.projection(Projections.include(columns));
            if (mongoConfig.getBatch() != null) {
                query.batchSize(mongoConfig.getBatch());
            }
            if (mongoConfig.getLimit() != null) {
                query.limit(mongoConfig.getLimit());
            }
            try (MongoCursor<Document> cursor = query
                    .cursor()) {
                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    Map<String, Object> map = new LinkedHashMap<>();
                    for (String column : columns) {
                        Object object = document.get(column);
                        if (object instanceof BigInteger) {
                            object = Long.parseLong(object.toString());
                        }
                        map.put(aliases.get(column),
                                object);
                    }
                    values.add(map);
                }
            }

        }
    }

    @Override
    public Map<String, Object> value() {
        return values.get(NumericUtils.nextInt(values.size()));
    }
}
