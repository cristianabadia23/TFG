package com.example.proyectofinal.models

import java.io.Serializable

data class Chat(val id:String?="",val isWritting:Boolean?=false,val timestamp:Long?=0,
                var idUser1:String?="",var idUser2:String?="",val ids:List<String>?):Serializable{
                    constructor():this("",false,0,"","", emptyList())
                }