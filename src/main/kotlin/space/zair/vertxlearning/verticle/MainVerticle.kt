package space.zair.vertxlearning.verticle

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.rxjava3.core.RxHelper
import io.vertx.rxjava3.core.Vertx

class MainVerticle : AbstractVerticle() {
  override fun start(promise: Promise<Void>) {
    RxHelper.deployVerticle(Vertx.vertx(), HttpServerVerticle())
      .subscribe(
        { promise.complete() },
        promise::fail)
  }
}


