package space.zair.vertxlearning.verticle

import io.vertx.core.Promise
import io.vertx.core.json.JsonObject
import io.vertx.rxjava3.core.AbstractVerticle
import io.vertx.rxjava3.ext.web.Router
import io.vertx.rxjava3.ext.web.RoutingContext
import io.vertx.rxjava3.ext.web.handler.BodyHandler

class HttpServerVerticle : AbstractVerticle() {
  private val articles = JsonObject().put(
    "articles",
    JsonObject().put(
      "math",
      JsonObject().apply {
        put("article_id", "geometry1")
        put("article_title", "Eulers formula")
        put("article_author", "L. Euler")
        put("publisher", "Math Journal")
      })
  )

  override fun start(promise: Promise<Void>) {
    val router = Router.router(vertx).apply {
      get("/api/articles").handler(this@HttpServerVerticle::getArticles)
      post("/api/articles").handler(BodyHandler.create()).handler(this@HttpServerVerticle::setArticle)
      put("/api/articles").handler(BodyHandler.create()).handler(this@HttpServerVerticle::updateArticle)
      delete("/api/articles").handler(this@HttpServerVerticle::deleteArticle)
    }

    vertx
      .createHttpServer()
      .requestHandler(router)
      .rxListen(8282)
      .subscribe(
        { promise.complete() },
        { failure -> promise.fail(failure.cause) })
  }

  private fun getArticles(context: RoutingContext) {
    context.response().statusCode = 200

    context.response().putHeader("Content-Type", "application/json")
    context.response().end(articles.encode())
  }

  private fun setArticle(context: RoutingContext) {
    val articleId = context.request().getParam("article_id")
    val articleTitle = context.request().getParam("article_title")
    val articleAuthor = context.request().getParam("article_author")
    val publisher = context.request().getParam("publisher")

    articles.getJsonObject("articles").put(
      articleId,
      JsonObject().apply {
        put("article_id", articleId)
        put("article_name", articleTitle)
        put("name_alias", articleAuthor)
        put("company", publisher)
      })

    val response = JsonObject().apply {
      put("success", true)
      put("action", "insert")
      put("current_rows", articles)
    }

    context.response().statusCode = 200

    context.response().putHeader("Content-Type", "application/json")
    context.response().end(response.encode())
  }

  private fun updateArticle(context: RoutingContext) {
    val articleId = context.request().getParam("article_id")
    val articleTitle = context.request().getParam("article_title")
    val articleAuthor = context.request().getParam("article_author")
    val publisher = context.request().getParam("publisher")

    articles.apply {
      getJsonObject("articles").getJsonObject(articleId).apply {
        put("article_title", articleTitle)
        put("article_author", articleAuthor)
        put("publisher", publisher)
      }
    }

    val response = JsonObject().apply {
      put("success", true)
      put("action", "update")
      put("current_rows", articles)
    }

    context.response().statusCode = 200

    context.response().putHeader("Content-Type", "application/json")
    context.response().end(response.encode())
  }

  private fun deleteArticle(context: RoutingContext) {
    val articleId = context.request().getParam("article_id")

    articles.getJsonObject("articles").remove(articleId)

    val response = JsonObject().apply {
      put("success", true)
      put("action", "delete")
      put("current_rows", articles)
    }

    context.response().statusCode = 200

    context.response().putHeader("Content-Type", "application/json")
    context.response().end(response.encode())
  }
}
