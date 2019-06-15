package gui

import events.Event
import scalafx.scene.control.ListView

class EventsList(private val itemsList: List[Event], private val offsetX: Double, private val offsetY: Double,
                 width: Double= 560, height: Double=480) extends ListView(itemsList) {

  this.prefWidth = width
  this.prefHeight = height
  this.layoutX = offsetX
  this.layoutY = offsetY

  def add(event: Event): Boolean = items.getValue.add(event)
  def getSelected: Event = selectionModel.apply.getSelectedItem
  def remove(event: Event) = items.getValue.remove(event)
}
