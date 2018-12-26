package com.unden.json

import play.api.libs.json.{Json, OWrites, Writes}

/**
 * @Author unden
 * @Date 8/24/18
 */
object JsonObj {
  def main(args: Array[String]): Unit = {
    simpleStr2JsObj
    simpleTuple2JsObj
    simpleObj2JsValue
    simpleObj2JsObj
  }

  def simpleStr2JsObj() = {
    val name = "zhangsan"
    val simpleStrJsObj = Json.obj(
      "name" -> name
    )
    println(simpleStrJsObj)
  }

  def simpleTuple2JsObj() = {
    val simpleTuple = (1, "zhangsan", 18)
    val simpleJsObj = Json.obj(
      "id" -> simpleTuple._1,
      "name" -> simpleTuple._2,
      "age" -> simpleTuple._3
    )
    println(simpleJsObj)
  }

  def simpleObj2JsValue() = {
    case class Person(name: String, age: Int)
    implicit val personReader = Writes[Person] { r =>
      Json.obj(
        "name" -> r.name,
        "age" -> r.age
      )
    }
    val person = Person("zhangsan", 20)
    val simpleJsValue = Json.toJson(person)
    println(simpleJsValue)
  }

  def simpleObj2JsObj() = {
    case class Person(name: String, age: Int)
    implicit val personReader = OWrites[Person] { r =>
      Json.obj(
        "name" -> r.name,
        "age" -> r.age
      )
    }
    val person = Person("zhangsan", 20)
    val simpleJsObj = Json.toJsObject(person)
    println(simpleJsObj)

  }
}
