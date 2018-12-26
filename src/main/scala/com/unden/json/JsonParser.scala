package com.unden.json

import play.api.libs.json.{JsPath, Json}
import play.api.libs.functional.syntax._

/**
 * @Author unden
 * @Date 8/24/18
 */
object JsonParser {

  def main(args: Array[String]): Unit = {
    simpleParse
    arrayParse
    simpleParse2Obj
    arrayParse2Obj
    multiParse
    multiParse2Obj
  }

  def simpleParse() = {
    val simpleJsonStr = "{\"name\":\"zhangsan\", \"age\":20}"
    val name = Json.parse(simpleJsonStr).\("name").get.as[String]
    val age = (Json.parse(simpleJsonStr) \ "age").get.as[Int]
    println(s"name: $name, age: $age")
  }

  def arrayParse() = {
    val arrayJsonStr = "[{\"name\":\"zhangsan\", \"age\":20}, {\"name\":\"lisi\", \"age\":30}]"
    val name1 = Json.parse(arrayJsonStr).\(0).\("name").get.as[String]
    val age2 = ((Json.parse(arrayJsonStr) \ 1) \ "age").get.as[Int]
    println(s"name: $name1, age: $age2");
  }

  def simpleParse2Obj() = {
    case class Person(name: String, age: Option[Int])
    implicit val personReader = ((JsPath \ "name").read[String] and
      (JsPath \ "age").readNullable[Int])(Person.apply _)
    val simpleJsonStr = "{\"name\":\"zhangsan\", \"age\":20}"
    val person = Json.parse(simpleJsonStr).validate[Person].get
//    val person = Json.parse(simpleJsonStr).as[Person]
    println(s"name: ${person.name}")
  }

  def arrayParse2Obj() = {
    val arrayJsonStr = "[{\"name\":\"zhangsan\", \"age\":20}, {\"name\":\"lisi\", \"age\":30}]"
    case class Person(name: String, age: Option[Int])
    implicit val personReader = ((JsPath \ "name").read[String] and
      (JsPath \ "age").readNullable[Int])(Person.apply _)
    val pList = Json.parse(arrayJsonStr).as[Seq[Person]]
    pList.foreach(println)
  }

  def multiParse() = {
    val resultJson = "{\"code\":0, \"message\":\"success\", \"data\":{\"total\":100, \"result\":[{\"name\":\"zhangsan\", \"age\":20}, {\"name\":\"lisi\", \"age\":30}]}}"
    val total = ((Json.parse(resultJson) \ "data") \ "total").get.as[Int]
    val name1 = ((((Json.parse(resultJson) \ "data") \ "result") \ 0) \ "name").get.as[String]
    val age2 = ((((Json.parse(resultJson) \ "data") \ "result") \ 1) \ "age").get.as[Int]
    println(s"total: $total")
    println(s"name: $name1")
    println(s"age: $age2")
  }

  def multiParse2Obj() = {
    case class Person(name: String, age: Option[Int])
    case class Data(total: Long, result: Option[Seq[Person]])
    case class Response(code: Int, message: String, data: Data)

    implicit val personReader = ((JsPath \ "name").read[String] and
      (JsPath \ "age").readNullable[Int])(Person.apply _)
    implicit val dataReader = ((JsPath \ "total").read[Long] and
      (JsPath \ "result").readNullable[Seq[Person]])(Data.apply _)
    implicit val responseReader = ((JsPath \ "code").read[Int] and
      (JsPath \ "message").read[String] and
      (JsPath \ "data").read[Data])(Response.apply _)

    val resultJson = "{\"code\":0, \"message\":\"success\", \"data\":{\"total\":100, \"result\":[{\"name\":\"zhangsan\", \"age\":20}, {\"name\":\"lisi\", \"age\":30}]}}"
    val result = Json.parse(resultJson).as[Response]
    println(s"code: ${result.code}, message: ${result.message}, data: ${result.data}")
  }

}
