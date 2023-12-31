package top.chenjipdc.mocks.plugins.mock.normal;

import com.alibaba.fastjson2.JSONObject;
import com.google.auto.service.AutoService;
import top.chenjipdc.mocks.config.Config;
import top.chenjipdc.mocks.config.mock.normal.IpMockConfig;
import top.chenjipdc.mocks.plugins.mock.MockPlugin;
import top.chenjipdc.mocks.plugins.mock.AbstractMockPlugin;
import top.chenjipdc.mocks.utils.IpUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@AutoService(MockPlugin.class)
public class IpMockPlugin extends AbstractMockPlugin<String, IpMockConfig> {

    @Override
    public String type() {
        return "ip";
    }

    @Override
    public void init(Config.MocksConfig config) {
        super.init(config);
        mockConfig = JSONObject.parseObject(config.getConfig(),
                IpMockConfig.class);
    }

    @Override
    public Map<String, String> value() {
        Map<String, String> map = new LinkedHashMap<>();
        for (String column : aliases.values()) {
            if (mockConfig.getType() != null) {
                switch (mockConfig.getType()) {
                    case V4:
                        map.put(column,
                                IpUtils.ipv4());
                        break;
                    case V4_WAN:
                        map.put(column,
                                IpUtils.ipv4Wan());
                        break;
                    case V4_LAN:
                        map.put(column,
                                IpUtils.ipv4Lan());
                        break;
                    case V4_LAN_A:
                        map.put(column,
                                IpUtils.ipv4LanA());
                        break;
                    case V4_LAN_B:
                        map.put(column,
                                IpUtils.ipv4LanB());
                        break;
                    case V4_LAN_C:
                        map.put(column,
                                IpUtils.ipv4LanC());
                        break;
                    default:
                        break;
                }
            }
            map.put(column,
                    IpUtils.ipv4());
        }
        return map;
    }
}
