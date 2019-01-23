package factory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import factory.lemming.Lemming;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;

public class Factory {

    private final LemmingGenerator lemmingGenerator = new LemmingGenerator();
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private ObjectMapper mapper = new ObjectMapper();
    
    public Factory() {
        RedisClient client = RedisClient.create("redis://127.0.0.1");
        StatefulRedisConnection<String, String> connection = client.connect();
        StatefulRedisPubSubConnection<String, String> connPubSub = client.connectPubSub();
        RedisPubSubAsyncCommands<String, String> pubSubAsync = connPubSub.async();
        
        Runnable publishLemmings = new Runnable() {
            @Override
            public void run() {
                Lemming[] lemmings = lemmingGenerator.generate(10);
                if(lemmings != null) {
                    for(int i = 0; i < lemmings.length; i++) {
                        String json;
                        try {
                            json = mapper.writeValueAsString(lemmings[i]);
                            pubSubAsync.publish("lemmings", json);
                        }
                        catch(JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(publishLemmings, 0, 10, TimeUnit.MILLISECONDS);
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Shutting down");
                connPubSub.close();
                connection.close();
                client.shutdown();
                executorService.shutdown();
            }
        }, 5, TimeUnit.MINUTES);
    }
    
    public static void main(String ...args) {
        new Factory();
    }
}
