package com.test.vertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

public class MyHttpServer {
	
	private static final Logger logger = LoggerFactory.getLogger(MyHttpServer.class);
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new VertxHttpServerVerticle());
		vertx.deployVerticle(new SecondVerticle());
		logger.info("main  end...");
	}

}


class VertxHttpServerVerticle extends AbstractVerticle {
	private static final Logger logger = LoggerFactory.getLogger(VertxHttpServerVerticle.class);
	HttpServer server;

	@Override
	public void start() throws Exception {
		logger.info("start.....");
		HttpServer server = vertx.createHttpServer();
		server.requestHandler(request -> {
			logger.info("incoming httprequest!");
			HttpServerResponse response = request.response();
			response.setStatusCode(200);
//			response.headers().add("Content-Type", "text/html") .add("Content-Length", String.valueOf(16));
			response.setChunked(true);
			response.headers().add("Content-Type", "text/html");
			response.write("Vert.x is alive!");
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.end();
		});
		server.listen(9999);
	}

	@Override
	public void stop() throws Exception {
		if (server != null) {
			server.close();
		}
	}
	
}

class SecondVerticle extends AbstractVerticle {
	private static final Logger logger = LoggerFactory.getLogger(SecondVerticle.class);
	@Override
	public void start() throws Exception {
		logger.info("second start.....");
	}
	
}
