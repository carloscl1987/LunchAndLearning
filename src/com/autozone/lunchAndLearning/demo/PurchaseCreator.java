package com.autozone.lunchAndLearning.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class PurchaseCreator extends AbstractVerticle {
	
  private int numberOfPurchases = 0;
  private int port = 8080;
  
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
	 
    port = vertx.getOrCreateContext().config().getInteger("port", 8280);
		 
    vertx.setPeriodic(2000, ar -> {
      numberOfPurchases++; 
      System.out.println("Nymber of purchases: " + numberOfPurchases );
    });
      
    vertx.createHttpServer()
      .requestHandler( req -> {
        req.response().end("Number of purchases so far: " + numberOfPurchases);  
	  }).listen(port, hd -> {
		  if(hd.succeeded()) {
		    startPromise.complete();
		  } else {
	          startPromise.fail(hd.cause());
		  }
	});
  }
  
  public static void main(String[] args) {
	JsonObject conf = new JsonObject();
	conf.put("port", 8181);
	
	DeploymentOptions opts = new DeploymentOptions();
	opts.setConfig(conf);
	
	Vertx vertx = Vertx.vertx();
	vertx.deployVerticle(new PurchaseCreator(), opts , result ->{
		if(result.succeeded()) {
			System.out.println("Verticle deployed");
		} else {
			System.out.println("Error while deploying verticle: " + result.cause());
		}
	});
  }
}
