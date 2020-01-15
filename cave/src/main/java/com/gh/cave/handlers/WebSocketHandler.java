package com.gh.cave.handlers;

import java.util.HashMap;
import com.gh.cave.lemmings.LemmingsWebSocketHandler;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 *
 * @author George Haddad
 *
 */
public final class WebSocketHandler implements Handler<ServerWebSocket> {

    private final Logger logger = LoggerFactory.getLogger("main");
    private Vertx vertx = null;
    private HashMap<String, LemmingsWebSocketHandler> socketMap = new HashMap<>(31);

    public WebSocketHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void handle(ServerWebSocket webSocket) {
        String key = "cave:" + webSocket.remoteAddress().host() + ":" + webSocket.remoteAddress().port();
        String path = webSocket.path();

        if(path.startsWith("/")) {
            webSocket.closeHandler(new Handler<Void>() {
                @Override
                public void handle(Void event) {
                    LemmingsWebSocketHandler handler = socketMap.remove(key);
                    handler.close();
                    handler = null;
                    logger.info("ws <-> closing connection for \"" + key + "\"");
                }
            });
        }

        if("/lemmings/cave".equals(path)) {
            LemmingsWebSocketHandler lemmingWebSocketHandler = new LemmingsWebSocketHandler(vertx, webSocket);
            socketMap.put(key, lemmingWebSocketHandler);
            logger.info("ws -> client (" + key + ") connected on " + path);
        }
        else {
            webSocket.reject(404);
        }
    }
}
