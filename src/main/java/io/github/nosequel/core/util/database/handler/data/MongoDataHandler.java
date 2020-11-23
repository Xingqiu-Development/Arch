package io.github.nosequel.core.util.database.handler.data;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.github.nosequel.core.util.data.DataController;
import io.github.nosequel.core.util.data.Loadable;
import io.github.nosequel.core.util.database.DatabaseController;
import io.github.nosequel.core.util.database.handler.DataHandler;
import io.github.nosequel.core.util.database.options.impl.MongoDatabaseOption;
import io.github.nosequel.core.util.database.type.mongo.MongoDataType;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.Collections;
import java.util.UUID;

@Getter
@Setter
public class MongoDataHandler implements DataHandler {

    private final DatabaseController controller;
    private final MongoDatabase database;

    /**
     * Constructor for setting up the MongoDataHandler
     */
    public MongoDataHandler(DatabaseController controller) {
        this.controller = controller;

        final MongoDatabaseOption option = (MongoDatabaseOption) this.controller.getOption();

        final MongoClient client = !option.isAuthenticate() ?
                new MongoClient(option.getHostname(), option.getPort()) :
                new MongoClient(new ServerAddress(option.getHostname(), option.getPort()), Collections.singletonList(MongoCredential.createCredential(option.getUsername(), option.getAuthenticateDatabase(), option.getPassword().toCharArray())));

        this.database = client.getDatabase(option.getAuthenticateDatabase());
    }

    @Override
    public void delete(Loadable<?> loadable, String $collection) {
        final MongoCollection<Document> collection = database.getCollection($collection);

        collection.deleteOne(Filters.eq("uuid", loadable.getUuid().toString()));
    }

    @Override
    public void save(Loadable<?> loadable, String collection) {
        final MongoDataType type = (MongoDataType) this.controller.getType();
        final Document document = database.getCollection(collection).find(Filters.eq("uuid", loadable.getUuid().toString())).first();

        type.save(database.getCollection(collection), document == null ? new Document() : document, loadable);
    }

    @Override
    public void load(DataController<?, ?> controller, Class<? extends Loadable<?>> loadableType, UUID uuid, String collectionName) {
        final MongoDataType type = (MongoDataType) this.controller.getType();
        final MongoCollection<Document> collection = database.getCollection(collectionName);
        final Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();

        if(document != null) {
            type.load(document, controller, loadableType);
        }
    }

    @Override
    public void loadAll(DataController<?, ?> controller, String collectionName, Class<? extends Loadable<?>> loadableType) {
        final MongoDataType type = (MongoDataType) this.controller.getType();
        final MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.find().forEach((Block<? super Document>) document -> type.load(document, controller, loadableType));
    }
}