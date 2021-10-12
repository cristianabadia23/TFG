package com.example.proyectofinal.models

data class Message(var id:String="", var idEnviador:String="", var idRecibidor:String="",
                   var idChat:String="", var timestamp:Long=0, var isVisto:Boolean = false,var mBody:String=""){
    constructor():this("","","","",0,false,"")
}
