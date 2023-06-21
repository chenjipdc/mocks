package top.chenjipdc.mocks.run;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.ConfigLoader;
import top.chenjipdc.mocks.config.PoolsConfig;
import top.chenjipdc.mocks.plugins.SinkPlugin;
import top.chenjipdc.mocks.pools.PoolsService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Runner {
    @SneakyThrows
    public static void run() {
        long start = System.currentTimeMillis();

        ConfigLoader load = new ConfigLoader();
        List<Config> configs = load.getConfigs();
        PoolsConfig poolsConfig = load.getPoolsConfig();

        for (Config config : configs) {
            PoolsService poolsService = new PoolsService(poolsConfig);
            MockService mockService = new MockService(config
                    .getMocks());

            mockService.init();

            for (Config.SinksConfig sink : config.getSinks()) {
                int loop = 1;
                // 相同配置循环多少次sink, 目的是为了多线程加速写入
                if (sink.getLoop() != null && sink.getLoop() > 1) {
                    loop = sink.getLoop();
                }
                for (int i = 0; i < loop; i++) {
                    ServiceLoader<SinkPlugin> sinkPlugins = ServiceLoader.load(SinkPlugin.class);
                    Map<String, SinkPlugin> sinkPluginMap = new HashMap<>();
                    for (SinkPlugin plugin : sinkPlugins) {
                        sinkPluginMap.put(plugin.type(),
                                plugin);
                    }

                    // 线程池加速写入
                    poolsService.async(() -> {
                        SinkService sinkservice = new SinkService(sink,
                                sinkPluginMap.get(sink
                                        .getType()));

                        Pipeline pipeline = new Pipeline(mockService,
                                sinkservice);
                        pipeline.init();
                        long size = config.getSize();
                        if (sink.getSize() != null) {
                            size = sink.getSize();
                        }
                        pipeline.run(size);
                        pipeline.stop();
                    });
                }
            }

            poolsService.waitCompleted();
            mockService.stop();
            poolsService.destroy();
        }

        log.info("所有总计耗时(秒): {}",
                (System.currentTimeMillis() - start) / 1000.00);
    }
}
