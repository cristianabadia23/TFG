package com.example.proyectofinal.models

import java.io.Serializable
import java.util.*

data class Partida(var jugador1:String = "", var jugador2:String = "", var tablero:MutableList<Int>,
        var ganador:String = "", var turno:Boolean = true, var fecha: Long = 0,
        var abandonar:String="",var casilla:Int = 0,var isPrivated:Boolean = false):Serializable{
                constructor():this("","", MutableList(9){0},"",true,0,"",0,false)
        }
