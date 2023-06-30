package top.chenjipdc.mocks.plugins.sink.db;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.sink.db.ElasticsearchSinkConfig;
import top.chenjipdc.mocks.plugins.SinkPlugin;
import top.chenjipdc.mocks.plugins.sink.AbstractSinkPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AutoService(SinkPlugin.class)
public class ElasticsearchSinkPlugin extends AbstractSinkPlugin {

    private ElasticsearchSinkConfig sinkConfig;

    private ElasticsearchClient client;

    private ElasticsearchTransport transport;

    private List<Map<String, Object>> cacheValues = new ArrayList<>();

    @Override
    public String type() {
        return "elasticsearch";
    }

    @SneakyThrows
    @Override
    public void init(Config.SinksConfig config) {
        super.init(config);

        sinkConfig = JSONObject.parseObject(config.getConfig(),
                ElasticsearchSinkConfig.class);

        RestClientBuilder builder = RestClient.builder(new HttpHost(sinkConfig.getHost(),
                sinkConfig.getPort()));

        // set username and password
        if (sinkConfig.getUsername() != null && sinkConfig.getPassword() != null) {
            builder.setHttpClientConfigCallback(clientBuilder -> {
                final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY,
                        new UsernamePasswordCredentials(sinkConfig.getUsername(),
                                sinkConfig.getPassword()));
                return clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
        }

        // build client
        RestClient restClient = builder
                .build();

        transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper());

        client = new ElasticsearchClient(transport);
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

    @SneakyThrows
    private void saveBatch() {
        if (cacheValues == null || cacheValues.size() == 0) {
            return;
        }
        final BulkRequest.Builder builder = new BulkRequest.Builder();
        builder.index(sinkConfig.getTable());
        builder.operations(cacheValues.stream()
                .map(this::mappingsConvert)
                .map(it -> {
                    IndexOperation.Builder<Object> objectBuilder = new IndexOperation.Builder<>();
                    String id = getId(it);
                    if (id != null) {
                        objectBuilder.id(id);
                    }
                    objectBuilder.document(it);
                    return new BulkOperation(objectBuilder.build());
                })
                .collect(Collectors.toList()));

        client.bulk(builder.build());

        cacheValues = new ArrayList<>();
    }

    @SneakyThrows
    private void saveOne(Map<String, Object> values) {
        client.index(index -> {
            index.index(sinkConfig.getTable());
            Map<String, Object> convert = mappingsConvert(values);
            String id = getId(convert);
            if (id != null) {
                index.id(id);
            }
            return index.document(convert);
        });
    }

    @SneakyThrows
    @Override
    public void stop() {
        super.stop();

        saveBatch();

        transport.close();
    }

    private String getId(Map<String, Object> mappingValues) {
        if (sinkConfig.getId() != null) {
            Object id = mappingValues.get(sinkConfig.getId());
            if (id == null) {
                throw new RuntimeException("es id不能为空, idField: " + sinkConfig.getId());
            } else {
                return id.toString();
            }
        }
        return null;
    }

    @Override
    public String logPrefix() {
        return "elasticsearch";
    }
}
