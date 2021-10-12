package com.example.proyectofinal.models

data class User(
        var id:String="", var nick:String = "", var nivel:String = "", var imageURI:String = "", var puntos:Int = 0
        , var victorias:Int = 0, var derrotas:Int = 0, var empates:Int=0,var estado:Boolean = false){
    constructor():this("","","","",0,0,0,0,false)

    fun cambiarRango(){
        if (puntos >= 0){
            when(puntos){
                in 0..5 -> nivel = "PRINCIPIANTE"
                in 6..20-> nivel = "EXPERIMENTADO"
                in 21..50-> nivel ="EXPERTO"
                else-> nivel = "GRAN EXPERTO"
            }
        }
        else{
            nivel ="NOVATO"
        }
    }

}