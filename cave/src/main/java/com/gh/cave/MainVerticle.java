package com.gh.cave;

import com.gh.cave.handlers.WebSocketHandler;
import com.gh.cave.shutdown.ShutdownHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

    private Logger logger = LoggerFactory.getLogger("main");
    
    @Override
    public void start() {
        final String name = config().getString("name");
        
        init();
        
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");
            response.end("hello from "+name+" running on Vert.x");
        });
        
        router.route("/shutdown").handler(new ShutdownHandler(vertx));
        server.requestHandler(router).listen(8080, handler -> {
            logger.info("HTTP server v2 started on port 8080");
        });
        
        HttpServerOptions options = new HttpServerOptions();
        options.setMaxWebsocketFrameSize(100000);  //100 kb frame size
        options.setLogActivity(false);
        
        HttpServer wsServer = vertx.createHttpServer(options);
        wsServer.websocketHandler(new WebSocketHandler(vertx)).listen(8888, handler -> {
            logger.info("webSocket server started on port 8888");
        });
    }
    
    private void init() {
        SharedData sd = vertx.sharedData();
        JsonObject redisObj = config().getJsonObject("redis");
        LocalMap<String, JsonObject> redisMap = sd.getLocalMap("redis");
        redisMap.put("config", redisObj);
    }
}
