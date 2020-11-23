package io.github.nosequel.core.util.synchronize.type.impl;

import com.google.gson.JsonObject;
import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.util.database.options.DatabaseOption;
import io.github.nosequel.core.util.json.JsonUtils;
import io.github.nosequel.core.util.synchronize.SynchronizationHandler;
import io.github.nosequel.core.util.synchronize.SynchronizeController;
import io.github.nosequel.core.util.synchronize.options.RedisDatabaseOption;
import io.github.nosequel.core.util.synchronize.type.SynchronizationType;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.function.Consumer;
import java.util.stream.Collectors;

public class JedisSynchronizationType implements SynchronizationType {

    private JedisPool jedisPool;

    private RedisDatabaseOption option;
    private SynchronizeController controller;

    @Override
    public void setup() {
        this.controller = CorePlugin.getInstance().getHandler().find(SynchronizeController.class);
        final DatabaseOption databaseOption = controller.getOption();

        if (!(databaseOption instanceof RedisDatabaseOption)) {
            throw new RuntimeException("DatabaseOption must be instance of a RedisDatabaseOption");
        }

        this.option = (RedisDatabaseOption) databaseOption;
        this.jedisPool = new JedisPool(new GenericObjectPoolConfig(), option.getHostname(), option.getPort());

        this.runCommand(jedis -> jedis.subscribe(this.getPubSub(), controller.getHandlers().stream()
                .map(SynchronizationHandler::getType)
                .collect(Collectors.toList())
                .toArray(new String[0])));
    }

    @Override
    public void publish(JsonObject object) {
        this.runCommand(jedis -> jedis.publish(object.get("channel").getAsString(), object.toString()));
    }


    @Override
    public void incoming(String message) {
        final JsonObject object = JsonUtils.getParser().parse(message).getAsJsonObject();

        this.controller.getHandlers().stream()
                .filter(handler -> handler.getType().equals(object.get("channel").getAsString()))
                .forEach(handler -> handler.handle(object));
    }

    /**
     * Runs a command using the existing JedisPool field
     *
     * @param consumer the command
     */
    private void runCommand(Consumer<Jedis> consumer) {
        new Thread(() -> {
            final Jedis jedis = this.jedisPool.getResource();

            if(jedis != null) {
                if (this.option.isAuthenticate()) {
                    jedis.auth(this.option.getPassword());
                }

                consumer.accept(jedis);
                jedis.close();
            }
        }).start();
    }

    private JedisPubSub getPubSub() {
        return new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                JedisSynchronizationType.this.incoming(message);
            }
        };
    }

}