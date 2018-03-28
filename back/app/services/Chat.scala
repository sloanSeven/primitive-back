package services

import javax.inject._
import util.control.Breaks._
import scala.collection.mutable.HashMap
import java.time.Instant
import scala.collection.mutable.ListBuffer
import scala.collection._
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.MutableList

/**
 * This trait demonstrates how to create a component that is injected
 * into a controller. The trait represents a counter that returns a
 * incremented number each time it is called.
 */
trait Chat {
  def send(a: String, b: String, message: String): Boolean
  def getLog(a: String, b: String): ListBuffer[Message]
  def getUserList(): Set[Person]
  def getUserList(person: String): Set[Person]

}

class Person(n: String) {
  val name = n;

  override def toString(): String = {
    return "[" + name + "]";
  }

}

class Message(a: Person, b: Person, m: String) {

  val from = a;
  val to = b;
  val message = m;
  val unixTimestamp: Long = Instant.now.getEpochSecond
  val guid = Message.atomicGuid.getAndIncrement();

  override def toString(): String = {
    return unixTimestamp + ": " + from + " " + message;
  }
}

object Message {

  val atomicGuid = new AtomicInteger();

}

class Conversation() {

  val unixTimestamp: Long = Instant.now.getEpochSecond
  val guid = Conversation.atomicGuid.getAndIncrement();

  override def toString(): String = {
    return unixTimestamp + " [Conversation] " + guid;
  }
}

object Conversation {

  val atomicGuid = new AtomicInteger();

}

@Singleton
class AtomicChat extends Chat {

  private val X = 36;
  private val Y = 36;

  val chatMap: HashMap[Person, HashMap[Person, mutable.ListBuffer[Message]]] = HashMap()
  val personMap: HashMap[String, Person] = HashMap()

  override def send(a: String, b: String, message: String): Boolean = {
    val aPerson = getPerson(a);
    val bPerson = getPerson(b);

    val aChatMap = chatMap.get(aPerson).get;

    var messageList = aChatMap.getOrElse(bPerson, new ListBuffer[Message]()); //todo change to conversation
    aChatMap.put(bPerson, messageList);

    if (messageList.isEmpty) {
      val bChatMap = chatMap.get(bPerson).get;
      bChatMap.put(aPerson, messageList);
    }
    val m = new Message(aPerson, bPerson, message);
    messageList += m;
    return true
  }

  override def getUserList(): Set[Person] = {
    return chatMap.keySet;
  }

  override def getUserList(person: String): Set[Person] = {
    val p = getPerson(person);
    val map = chatMap.get(p).get
    return map.keySet;
  }

  def getPerson(person: String): Person = {
    val p = personMap.getOrElse[Person](person, new Person(person));
    if (!personMap.contains(person)) {
      chatMap.put(p, new HashMap());
      personMap.put(person, p)
    }
    return p;
  }

  override def getLog(a: String, b: String): ListBuffer[Message] = {
    val aPerson = getPerson(a);
    val bPerson = getPerson(b);

    val aMap = chatMap.get(aPerson)
    val messageList = aMap.get(bPerson);
    return messageList
  }

}
