package controllers

import play.api.mvc._

object Application extends Controller {

  import play.api.data._
  import play.api.data.Forms._
  import models.Task

  val taskForm = Form(
    mapping(
      "id" -> ignored(1234l), //shit no sure
      "label" -> nonEmptyText,
      "label1" -> text,
      "label2" -> text
    )(Task.apply)(Task.unapply)
  )

  def index = Action {
    Redirect(routes.Application.tasks)
  }

  def tasks = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      value => {
        Task.create(value.label,value.label1, value.label2)
        Redirect(routes.Application.tasks)
      }
    )
  }

  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>
      import java.io.File
      val filename = picture.filename
      val contentType = picture.contentType
      picture.ref.moveTo(new File("/tmp/picture"))
      Ok("File uploaded")
    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Missing file"
      )
    }
  }
}