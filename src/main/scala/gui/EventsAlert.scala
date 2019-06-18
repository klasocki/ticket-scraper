package gui

import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.Stage

class EventsAlert(  private val alertContentText: String, private val alertTitle: String,
                      private val stage: Stage, alertType: AlertType=AlertType.Error) extends Alert(alertType) {


  def getHeaderText: String = {
    alertType match {
      case AlertType.Error => "Error!"
      case AlertType.Warning => "Warning!"
      case AlertType.Confirmation => "Do you really want to exit?"
      case AlertType.Information => "Information"
    }
  }

  initOwner(stage)
  title = alertTitle
  headerText = getHeaderText
  contentText = alertContentText
}
