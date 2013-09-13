package controllers

import play.api.mvc._

object Application extends Controller {

  import play.api.data._
  import play.api.data.Forms._
  import models.Task

  val taskForm = Form(
    tuple(
      "label" -> nonEmptyText,
      "label1" -> nonEmptyText
    )
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
        Task.create(value._1, value._2)
        Redirect(routes.Application.tasks)
      }
    )
  }

  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }
}