package gui

import scalafx.scene.control.Button

class EventsButton( title: String, private val offX: Double,
                   private val offY: Double, width: Double= 150) extends Button(title) {

  this.prefWidth = width
  this.layoutX = offX
  this.layoutY = offY


}
