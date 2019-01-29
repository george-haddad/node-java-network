package com.gh.cave.lemmings;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * 
 * @author George Haddad
 *
 */
public class LemmingsWebSocketHandler {
    
    private Logger logger = LoggerFactory.getLogger("main");
    private Vertx vertx = null;
    private ServerWebSocket webSocket = null;
    private RedisClient redis = null;
    
    public LemmingsWebSocketHandler(Vertx vertx, ServerWebSocket webSocket) {
        setVertx(vertx);
        setServerWebSocket(webSocket);
        init();
    }
    
    private void init() {
        initRedis();
        
        vertx.eventBus().<JsonObject> consumer("io.vertx.redis.lemmings", received -> {
            JsonObject jsonBody = received.body();
            String jsonLemming = jsonBody.toString();
            webSocket.writeTextMessage(jsonLemming);
        });
        
        webSocket.textMessageHandler(new Handler<String>() {
            @Override
            public void handle(String msg) {
                JsonObject json = new JsonObject(msg);
                logger.info("ws -> "+json.toString());
            }
        });
        
        
        webSocket.binaryMessageHandler(new Handler<Buffer>() {
            @Override
            public void handle(Buffer buffer) {
                //Not using it now
            }
        });
    }
    
    private void initRedis() {
        LocalMap<String, JsonObject> redisMap = vertx.sharedData().getLocalMap("redis");
        JsonObject redisConfig = redisMap.get("config");
        String host = redisConfig.getString("host");
        int port = redisConfig.getLong("port").intValue();
        String encoding = redisConfig.getString("encoding");
        boolean tcpKeepAlive = redisConfig.getBoolean("tcpKeepAlive").booleanValue();
        boolean tcpNoDelay = redisConfig.getBoolean("tcpNoDelay").booleanValue();

        RedisOptions redisOptions = new RedisOptions()
                .setHost(host)
                .setPort(port)
                .setEncoding(encoding);

        ((NetClientOptions)redisOptions)
            .setTcpKeepAlive(tcpKeepAlive)
            .setTcpNoDelay(tcpNoDelay);
        
        RedisClient redis = RedisClient.create(vertx, redisOptions);
        redis.subscribe("lemmings", res -> {
            if(res.succeeded()) {
                logger.info("subscribed to \"lemmings\" channel");
            }
            else {
                logger.warn("unable to subscribe to \"lemmings\" channel");
            }
        });
    }
    
    public void setVertx(Vertx vertx) throws NullPointerException {
        if(vertx == null) {
            throw new NullPointerException("vertx cannot be set to null");
        }
        
        this.vertx = vertx;
    }
    
    public void setServerWebSocket(ServerWebSocket webSocket) throws NullPointerException {
        if(webSocket == null) {
            throw new NullPointerException("websocket cannot be set to null");
        }
        
        this.webSocket = webSocket;
    }
    
    public void close() {
        if(redis != null) {
            redis.close(handler -> {
                logger.info("disconnected from Redis \"lemmings\" channel");
            });
        }
    }
}
