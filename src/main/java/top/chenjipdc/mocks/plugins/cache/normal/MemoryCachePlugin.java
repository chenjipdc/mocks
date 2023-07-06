package top.chenjipdc.mocks.plugins.cache.normal;

import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.plugins.cache.CachePlugin;
import top.chenjipdc.mocks.utils.NumericUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@AutoService(CachePlugin.class)
public class MemoryCachePlugin<V> implements CachePlugin<V> {

    private final List<Map<String, V>> values = new ArrayList<>();

    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public String type() {
        return "memory";
    }

    @Override
    public void cache(Map<String, V> value) {
        values.add(value);
    }

    @Override
    public void caches(Collection<Map<String, V>> value) {
        values.addAll(value);
    }

    @Override
    public Map<String, V> get(boolean random) {
        if (random) {
            return values.get(NumericUtils.nextInt(values.size()));
        } else {
            if (index.get() >= values.size()) {
                return null;
            }
            return values.get(index.getAndIncrement());
        }
    }

    @Override
    public int size() {
        return values.size();
    }
}
