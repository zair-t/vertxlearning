package space.zair.vertxlearning

import io.vertx.core.Launcher
import space.zair.vertxlearning.verticle.MainVerticle

fun main() {
  Launcher.executeCommand("run", MainVerticle::class.java.name)
}
