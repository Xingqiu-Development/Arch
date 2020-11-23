package io.github.nosequel.core.util.database.type.mongo;

import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import io.github.nosequel.core.util.data.Data;
import io.github.nosequel.core.util.data.DataController;
import io.github.nosequel.core.util.data.Loadable;
import io.github.nosequel.core.util.data.impl.SaveableData;
import io.github.nosequel.core.util.database.type.DataType;
import io.github.nosequel.core.util.json.JsonUtils;
import org.bson.Document;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class MongoDataType implements DataType<Document, MongoCollection<Document>> {

    @Override
    public void load(Document document, DataController<?, ?> controller, Class<? extends Loadable<?>> loadableClass) {
        final List<Data> data = new ArrayList<>();
        final AtomicReference<UUID> uuid = new AtomicReference<>();

        document.forEach((key, string) -> {
            if (key.equals("uuid")) {
                uuid.set(UUID.fromString((String) string));
            } else if (string instanceof String) {
                data.add(controller.getRegisteredData().stream()
                        .filter($data -> $data instanceof SaveableData && ((SaveableData) $data).getSavePath().equals(key))
                        .map(current -> getSaveableData((SaveableData) current, (String) string))
                        .findFirst().orElse(null));
            }
        });

        Constructor<?> constructor;
        Loadable<?> loadable = null;

        try {
            constructor = loadableClass.getConstructor(UUID.class);
            loadable = (Loadable<?>) constructor.newInstance(uuid.get());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignored) {
            try {
                constructor = loadableClass.getConstructor(UUID.class, List.class);
                loadable = (Loadable<?>) constructor.newInstance(uuid.get(), data);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        } finally {
            if(loadable != null && (loadable.getData() == null || loadable.getData().isEmpty())) {
                loadable.setData(new ArrayList<>());
                data.forEach(loadable::addDataNormal);
            }
        }
    }


    @Override
    public void save(MongoCollection<Document> collection, Document document, Loadable<?> loadable) {
        loadable.getData().stream()
                .filter(data -> data instanceof SaveableData)
                .map(SaveableData.class::cast)
                .forEach(saveableData -> document.put(saveableData.getSavePath(), saveableData.toJson().toString()));

        document.put("uuid", loadable.getUuid().toString());

        collection.replaceOne(Filters.eq("uuid", loadable.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

    private SaveableData getSaveableData(SaveableData data, String string) {
        try {
            final Constructor<?> constructor = data.getClass().getConstructor(JsonObject.class);

            return (SaveableData) constructor.newInstance(JsonUtils.getParser().parse(string).getAsJsonObject());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();

            return null;
        }
    }

}