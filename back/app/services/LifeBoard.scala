package services

import javax.inject._
import util.control.Breaks._

/**
 * This trait demonstrates how to create a component that is injected
 * into a controller. The trait represents a counter that returns a
 * incremented number each time it is called.
 */
trait LifeBoard {
  def nextMove(): String
}

/**
 * This class is a concrete implementation of the [[Counter]] trait.
 * It is configured for Guice dependency injection in the [[Module]]
 * class.
 *
 * This class has a `Singleton` annotation because we need to make
 * sure we only use one counter per application. Without this
 * annotation we would get a new instance every time a [[Counter]] is
 * injected.
 */
@Singleton
class AtomicBoard extends LifeBoard {

  private val X = 36;
  private val Y = 36;

  private var board = Array.ofDim[String](X, Y)
  val r = scala.util.Random
  val dead = " ";
  random();

  def random() {
    for (i <- 0 to X - 1; j <- 0 to Y - 1) {
      if (r.nextBoolean())
        board(i)(j) = dead;
      else
        board(i)(j) = "-";
    }
  }

  def update() {
    val newboard = Array.ofDim[String](X, Y)
    for (i <- 0 to X - 1; j <- 0 to Y - 1) {
      val v = update(i, j)
      newboard(i)(j) = v;
    }
    var eq = true;
    breakable {
      for (i <- 0 to X - 1; j <- 0 to Y - 1) {
        eq = board(i)(j) == newboard(i)(j)
        if (!eq) {
          break;
        }
      }
    }
    if (eq) {
      random();
    } else {
      board = newboard;
    }
  }

  def update(x: Int, y: Int): String = {

    val alive = board(x)(y) != dead
    var count = 0;
    for (i <- -1 to 1; j <- -1 to 1) {
      breakable {
        val xx = x + i;
        val yy = y + j;
        if (xx < 0 || yy < 0 || xx >= board.length || yy >= board(0).length) {
          break;
        }
        val v = board(xx)(yy)
        if (v != dead) {
          count += 1;
        }
      }
    }
    var nv = "?"
    if (alive) {
      count -= 1; //self
      if (count == 2 || count == 3) {
        nv = "*"
      } else {
        nv = dead //"*" //count + "";
      }
    } else { //dead
      if (count == 3) {
        nv = "*";
      } else {
        nv = dead
      }
    }
    return nv;
  }

  override def nextMove(): String = {
    update();
    val builder = StringBuilder.newBuilder
    
    for (i <- 0 to X-1; j <- 0 to Y-1) {
      builder.append(board(i)(j))
      if (j == 35)
        builder.append("\n");
    }
    return builder.toString()
  }
}
