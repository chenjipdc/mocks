package top.chenjipdc.mocks.json;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectReaderImplLinkHashMapString implements ObjectReader<Object> {

    @Override
    public Object readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        if (jsonReader.isJSONB()) {
            return this.readJSONBObject(jsonReader,
                    fieldType,
                    fieldName,
                    features);
        }

        boolean match = jsonReader.nextIfMatch('{');
        if (!match) {
            if (jsonReader.current() == '[') {
                jsonReader.next();
                if (jsonReader.current() == '{') {
                    Object arrayItem = readObject(jsonReader,
                            String.class,
                            fieldName,
                            features);
                    if (jsonReader.nextIfMatch(']')) {
                        jsonReader.nextIfMatch(',');
                        return arrayItem;
                    }
                }
                throw new JSONException(jsonReader.info("expect '{', but '['"));
            }

            if (jsonReader.nextIfNullOrEmptyString()) {
                return null;
            }
        }

        JSONReader.Context context = jsonReader.getContext();
        Map<String, Object> object = new LinkedHashMap<>();
        long contextFeatures = features | context.getFeatures();

        for (int i = 0; ; ++i) {
            if (jsonReader.nextIfMatch('}')) {
                break;
            }

            String name = jsonReader.readFieldName();
            String value = jsonReader.readString();
            if (i == 0
                    && (contextFeatures & JSONReader.Feature.SupportAutoType.mask) != 0
                    && name.equals(getTypeKey())) {
                continue;
            }

            Object origin = object.put(name,
                    value);
            if (origin != null) {
                if ((contextFeatures & JSONReader.Feature.DuplicateKeyValueAsArray.mask) != 0) {
                    if (origin instanceof Collection) {
                        ((Collection) origin).add(value);
                        object.put(name,
                                origin);
                    } else {
                        JSONArray array = JSONArray.of(origin,
                                value);
                        object.put(name,
                                array);
                    }
                }
            }
        }

        jsonReader.nextIfMatch(',');

        return object;
    }
}