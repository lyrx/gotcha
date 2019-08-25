package com.lyrx.gotcha.components


object RuntimeStatus{


  val READY="ready"
  val ONGOING = "ongoing"
  val DONE = "done"


}


case class RuntimeStatus(msg:String,status:String){

  def blinkMe()=if(status == RuntimeStatus.ONGOING)
    "blink_me"
  else
    ""



  def isOnGoing():Boolean = (status==RuntimeStatus.ONGOING)
  def isReady():Boolean = (status==RuntimeStatus.READY)
  def isDone():Boolean = (status==RuntimeStatus.DONE)


}
