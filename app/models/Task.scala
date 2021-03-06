package models

case class Task(id: Long, label: String, label1: String, label2: String)

object Task {

  import anorm._
  import anorm.SqlParser._

  val task = {
    get[Long]("id") ~
      get[String]("label") ~
      get[String]("label1") ~
      get[String]("label2") map {
      case id ~ label ~ label1 ~ label2 => Task(id, label, label1, label2)
    }
  }

  import play.api.db._
  import play.api.Play.current

  def all(): List[Task] = DB.withConnection {
    implicit c =>
      SQL("select * from task").as(task *)
  }

  def create(label: String, label1: String, label2: String) {
    DB.withConnection {
      implicit c =>
        SQL("insert into task (label, label1, label2) values ({label}, {label1}, {label2})").on(
          'label -> label,
          'label1 -> label1,
          'label2 -> label2
        ).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection {
      implicit c =>
        SQL("delete from task where id = {id}").on(
          'id -> id
        ).executeUpdate()
    }
  }
}
