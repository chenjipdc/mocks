package top.chenjipdc.mocks.plugins.sink.db;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.db.MongoSinkConfig;
import top.chenjipdc.mocks.plugins.SinkPlugin;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AutoService(SinkPlugin.class)
public class MongoSinkPlugin extends AbstractSinkPlugin<MongoSinkConfig> {

    private MongoClient mongoClient;

    private MongoSinkConfig mongoConfig;

    private List<Map<String, Object>> valuesCache;

    @Override
    public String type() {
        return "mongo";
    }

    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        valuesCache = new ArrayList<>();

        mongoConfig = JSONObject.parseObject(config.getConfig(),
                MongoSinkConfig.class);
        String uri = "mongodb://";
        if (mongoConfig.getUsername() != null && mongoConfig.getPassword() != null) {
            uri += mongoConfig.getUsername() + ":" + mongoConfig.getPassword() + "@";
        }
        uri += mongoConfig.getAddress();
        if (mongoConfig.getConnectParams() != null) {
            uri += "?" + mongoConfig.getConnectParams();
        }
        mongoClient = MongoClients.create(uri);
    }

    @Override
    public void sink(Map<String, Object> values) {
        if (mongoConfig.getBatch() != null && mongoConfig.getBatch() > 1) {
            valuesCache.add(values);
            if (valuesCache.size() >= mongoConfig.getBatch()) {
                insertBatch(valuesCache.stream()
                        .map(this::document)
                        .collect(Collectors.toList()));
                valuesCache = new ArrayList<>();
            }
        } else {
            insertOne(document(values));
        }
    }

    private void insertBatch(List<Document> documents) {
        MongoDatabase database = mongoClient.getDatabase(mongoConfig.getDatabase());
        MongoCollection<Document> collection = database.getCollection(mongoConfig.getCollection());
        collection.insertMany(documents);
    }

    private void flushCache() {
        MongoDatabase database = mongoClient.getDatabase(mongoConfig.getDatabase());
        MongoCollection<Document> collection = database.getCollection(mongoConfig.getCollection());
        collection.insertMany(valuesCache.stream()
                .map(this::document)
                .collect(Collectors.toList()));
    }

    private void insertOne(Document document) {
        MongoDatabase database = mongoClient.getDatabase(mongoConfig.getDatabase());
        MongoCollection<Document> collection = database.getCollection(mongoConfig.getCollection());
        collection.insertOne(document);
    }

    private Document document(Map<String, Object> values) {
        return new Document(mappingsConvert(values));
    }

    @Override
    public void stop() {
        super.stop();
        if (valuesCache.size() > 0) {
            flushCache();
        }
        mongoClient.close();
    }

    @Override
    public String logPrefix() {
        return "mongo";
    }
}
