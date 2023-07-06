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
public class MongoMockPlugin extends AbstractMockPlugin<Object, MongoMockConfig> {

    @Override
    public String type() {
        return "mongo";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);

        mockConfig = JSONObject.parseObject(config.getConfig(),
                MongoMockConfig.class);
        String uri = "mongodb://";
        if (mockConfig.getUsername() != null && mockConfig.getPassword() != null) {
            uri += mockConfig.getUsername() + ":" + mockConfig.getPassword() + "@";
        }
        uri += mockConfig.getAddress();
        if (mockConfig.getConnectParams() != null) {
            uri += "?" + mockConfig.getConnectParams();
        }
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(mockConfig.getDatabase());
            MongoCollection<Document> collection = database.getCollection(mockConfig.getCollection());
            FindIterable<Document> query;
            if (mockConfig.getFindJson() != null) {
                Document find = Document.parse(mockConfig.getFindJson());
                query = collection.find(find);
            } else {
                query = collection.find();
            }
            query.projection(Projections.include(columns));
            if (mockConfig.getBatch() != null) {
                query.batchSize(mockConfig.getBatch());
            }
            if (mockConfig.getLimit() != null) {
                query.limit(mockConfig.getLimit());
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
                    cachePlugin.cache(map);
                }
            }

        }
    }
}
