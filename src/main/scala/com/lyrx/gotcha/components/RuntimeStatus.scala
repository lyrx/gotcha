package com.lyrx.gotcha.components


object RuntimeStatus{


  val READY="ready"
  val ONGOING = "ongoing"
  val DONE = "done"



}


case class RuntimeStatus(msg:String,status:String){

}
