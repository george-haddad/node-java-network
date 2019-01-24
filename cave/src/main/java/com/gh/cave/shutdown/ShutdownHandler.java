package com.gh.cave.shutdown;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

/**
 * 
 * @author George Haddad
 *
 */
public final class ShutdownHandler implements Handler<RoutingContext> {

    private Logger logger = LoggerFactory.getLogger("main");
    private Vertx vertx = null;

    public ShutdownHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void handle(RoutingContext ctx) {
        ctx.response()
            .putHeader("content-type", "text/plain")
            .setStatusCode(200)
            .end("dying");

        vertx.close(completionHandler -> {
            logger.info("shutting down");
            System.exit(0);
        });
    }
}