package com.example.proyectofinal.utils

import android.util.Log

class PartidaUtil(tablero:IntArray) {

    private var tablero: IntArray = tablero
    private lateinit var tableroVertical: Array<IntArray>
    private lateinit var tableroHorizontal: Array<IntArray>
    private lateinit var tablerodiagonal1: IntArray
    private lateinit var tablerodiagonal2: IntArray

    init {
        convertirDatos()
    }

    fun convertirDatos() {
        tableroHorizontal = convertirDatosHorizontales()
        tableroVertical = convertirDatosVerticales()
        tablerodiagonal1 = convertirDatosDiagonal1()
        tablerodiagonal2 = convertirDatosDiagonal2()
    }

    fun reConvertirDatos(): IntArray {
        val tableroAux: IntArray = IntArray(9)
        var cont = 0
        for (i in 0..2) {
            for (j in 0..2) {
                tableroAux[cont] = tableroHorizontal[i][j]
                cont++
            }
        }

        return tableroAux
    }
    fun getTablero():IntArray{
        return tablero
    }
    /*************************************CONVERTIDOR*DE*DATOS*********************************************************/
    /*****************************************************************************************************************/
    fun convertirDatosHorizontales(): Array<IntArray> {
        val tableroAux = Array(3) { IntArray(3) }
        var cont = 0
        for (i in 0..2) {
            for (j in 0..2) {
                tableroAux[i][j] = this.tablero[cont]
                cont++
            }
        }
        return tableroAux
    }

    fun convertirDatosDiagonal1(): IntArray {
        val tableroAux: IntArray = IntArray(3)
        tableroAux[0] = tableroHorizontal[0][0]
        tableroAux[1] = tableroHorizontal[1][1]
        tableroAux[2] = tableroHorizontal[2][2]
        return tableroAux
    }

    fun convertirDatosDiagonal2(): IntArray {
        val tableroAux: IntArray = IntArray(3)
        tableroAux[0] = tableroHorizontal[0][2]
        tableroAux[1] = tableroHorizontal[1][1]
        tableroAux[2] = tableroHorizontal[2][0]
        return tableroAux
    }

    fun convertirDatosVerticales(): Array<IntArray> {
        val tableroAux = Array(3) { IntArray(3) }
        var cont = 0
        for (i in 0..2) {
            for (j in 0..2) {
                tableroAux[j][i] =  tableroHorizontal[i][j]
                cont++
            }
        }

        return tableroAux
    }
    /*******************************************************************************************************/
    /************************************VALIDACIONES*******************************************************/
    /*******************************************************************************************************/
    fun verificarDiagonal1(jugador: Int): Int {
        return if (tablerodiagonal1.filter { x -> x == jugador }.count() == 3) jugador else 0
    }

    fun verificarDiagonal2(jugador: Int): Int {
        return if (tablerodiagonal2.filter { x -> x == jugador }.count() == 3) jugador else 0
    }

    fun verificarVertical(jugador: Int): Int {
        tableroVertical.forEach { x -> if (x.filter { y -> y == jugador }.count() == 3) return jugador }
        return 0
    }

    fun verificarHorizontal(jugador: Int): Int {
        tableroHorizontal.forEach { x -> if (x.filter { y -> y == jugador }.count() == 3) return jugador }
        return 0
    }
    /*******************************************************************************************************/
    /**********************************GANADOR**************************************************************/
    /******************************************************************************************************/
    fun determinarGanador(): Int {
        convertirDatos()
        return if (verificarVertical(1) === 1) {
            1
        } else if (verificarVertical(2) === 2) {
            2
        } else if (verificarHorizontal(1) === 1) {
            1
        } else if (verificarHorizontal(2) === 2) {
            2
        } else if (verificarDiagonal1(1) === 1) {
            1
        } else if (verificarDiagonal1(2) === 2) {
            2
        } else if (verificarDiagonal2(1) === 1) {
            1
        } else if (verificarDiagonal2(2) === 2) {
            2
        } else if (tablero.filter { x -> x != 0 }.count() == 9) {
            3
        } else {
            0
        }
    }
    /*****************************************************************************************************/
    /**********************************IAs****************************************************************/
    /*****************************************************************************************************/
    fun selectorInteligente(): IntArray? {
        convertirDatos()
        val ocupadoD1 = tablerodiagonal1.filter { x -> x == 0 }.count() == 0
        val ocupadoD2 = tablerodiagonal2.filter { x -> x == 0 }.count() == 0
        /***********************************************************************************/
        if(tableroHorizontal[1][1] == 0){
            tableroHorizontal[1][1]=2
            return reConvertirDatos()
        }
        if(tableroHorizontal[2][2] == 0){
            tableroHorizontal[2][2]=2
            return reConvertirDatos()
        }
        /***********************************************************************************/
        if (tablerodiagonal1.filter { x -> x == 2 }.count() == 2 && !ocupadoD1) {
            movimientoDiagonal1()
            return reConvertirDatos()
        }
        if (tablerodiagonal2.filter { x -> x == 2 }.count() == 2 && !ocupadoD2) {
            movimientoDiagonal2()
            return reConvertirDatos()
        }
        if (tablerodiagonal1.filter { x -> x == 1 }.count() == 2 && !ocupadoD1) {
            movimientoDiagonal1()
            return reConvertirDatos()
        }
        if (tablerodiagonal2.filter { x -> x == 1 }.count() == 2 && !ocupadoD2) {
            movimientoDiagonal2()
            return reConvertirDatos()
        }
        for (i in 0..2) {
            if (tableroHorizontal[i].filter { x -> x == 0 }.count() != 0) {
                if (tableroHorizontal[i].filter { x -> x == 2 }.count() == 2) {
                    movimeintoHorizontal(i)
                    return reConvertirDatos()
                } else if (tableroHorizontal[i].filter { x -> x == 2 }.count() == 1) {
                    movimeintoHorizontal(i)
                    return reConvertirDatos()
                }
            }
        }
        for (i in 0..2) {
            Log.i("CoUNT" , tableroVertical[i].filter { x -> x == 0 }.count().toString())
            if (tableroVertical[i].filter { x -> x == 0 }.count() != 0) {
                if (tableroVertical[i].filter { x -> x == 2 }.count() == 2) {
                    movimeintoVertical(i)
                    return reConvertirDatos()
                } else if (tableroVertical[i].filter { x -> x == 2 }.count() == 1) {
                    movimeintoVertical(i)
                    return reConvertirDatos()
                }
            }
        }
        /**********************************************************************************/
        for (i in 0..2) {
            if (tableroHorizontal[i].filter { x -> x == 0 }.count() != 0) {
                if (tableroHorizontal[i].filter { x -> x == 1 }.count() == 2) {
                    movimeintoHorizontal(i)
                    return reConvertirDatos()
                } else if (tableroHorizontal[i].filter { x -> x == 1 }.count() == 1) {
                    movimeintoHorizontal(i)
                    return reConvertirDatos()
                }
            }
        }
        for (i in 0..2) {
            if (tableroVertical[i].filter { x -> x == 0 }.count() != 0) {
                if (tableroVertical[i].filter { x -> x == 1 }.count() == 2) {
                    movimeintoVertical(i)
                    return reConvertirDatos()
                } else if (tableroVertical[i].filter { x -> x == 1 }.count() == 1) {
                    movimeintoVertical(i)
                    return reConvertirDatos()
                }
            }
        }


        return null
    }
    /******************************************************************************************/
    fun selectorImposible(): IntArray?{
        Log.i("ESTADO","Calculando ...")
        convertirDatos()
        val ocupadoD1 = tablerodiagonal1.filter { x -> x == 0 }.count() == 0
        val ocupadoD2 = tablerodiagonal2.filter { x -> x == 0 }.count() == 0
        /***********************************************************************************/
        Log.i("ESTADO","Calculando Diagonales...")
        if (tablerodiagonal1.filter { x -> x == 2 }.count() == 2 && !ocupadoD1) {
            movimientoDiagonal1()
            return reConvertirDatos()
        }
        if (tablerodiagonal2.filter { x -> x == 2 }.count() == 2 && !ocupadoD2) {
            movimientoDiagonal2()
            return reConvertirDatos()
        }
        if (tablerodiagonal1.filter { x -> x == 1 }.count() == 2 && !ocupadoD1) {
            movimientoDiagonal1()
            return reConvertirDatos()
        }
        if (tablerodiagonal2.filter { x -> x == 1 }.count() == 2 && !ocupadoD2) {
            movimientoDiagonal2()
            return reConvertirDatos()
        }

        /***********************************************************************************/
        Log.i("ESTADO","Calculando 2 ... ")

        for (i in 0..2){
            Log.i("COUNT2",tableroHorizontal[i].filter { x -> x == 0 }.count().toString())
            if (tableroHorizontal[i].filter { x-> x == 0 }.count() != 0){
                if (tableroHorizontal[i].filter { x -> x == 2 }.count() == 2) {
                    movimeintoHorizontal(i)
                    return reConvertirDatos()
                }
            }
        }
        for (i in 0..2){
            if(tableroVertical[i].filter { x -> x == 0 }.count() != 0) {
                if (tableroVertical[i].filter { x -> x == 2 }.count() == 2) {
                    movimeintoVertical(i)
                    return reConvertirDatos()
                }
            }
        }
        /***********************************************************************************/
        Log.i("ESTADO","Calculando 1 ...")
        for (i in 0..2){
            Log.i("COUNT1",tableroHorizontal[i].filter { x -> x == 0 }.count().toString())
            if (tableroHorizontal[i].filter { x -> x == 0 }.count() != 0) {
                if (tableroHorizontal[i].filter { x -> x == 1 }.count() == 2) {
                    movimeintoHorizontal(i)
                    return reConvertirDatos()
                }
            }
        }
        for (i in 0..2){
            if(tableroVertical[i].filter { x -> x == 0 }.count() != 0) {
                if (tableroVertical[i].filter { x -> x == 1 }.count() == 2) {
                    movimeintoVertical(i)
                    return reConvertirDatos()
                }
            }
        }
        /**********************************************************************************/

        Log.i("ESTADO","Calculando 2 complete ...")
        for (i in 0..2){
            if(tableroHorizontal[i].filter { x -> x == 0 }.count() != 0) {
                if (tableroHorizontal[i].filter { x -> x == 2 }.count() == 1) {
                    movimeintoHorizontal(i)
                    return reConvertirDatos()
                }
            }
        }
        for (i in 0..2){
            if(tableroVertical[i].filter { x -> x == 0 }.count() != 0) {
                if (tableroVertical[i].filter { x -> x == 2 }.count() == 1) {
                    movimeintoVertical(i)
                    return reConvertirDatos()
                }
            }
        }
        /**********************************************************************************/
        Log.i("ESTADO","Calculando Movimiento predefinido ...")
        if (tableroHorizontal[1][1] == 0){
            tableroHorizontal[1][1] = 2
            return reConvertirDatos()
        }
        if (tableroHorizontal[2][2] == 0){
            tableroHorizontal[2][2] = 2
            return reConvertirDatos()
        }
        /**********************************************************************************/
        Log.i("ESTADO","Calculando completar linea ...")
        for (i in 0..2){
            if (tableroHorizontal[i].filter { x -> x == 0 }.count() == 2) {
                movimeintoHorizontal(i)
                return reConvertirDatos()
            }
        }

        for (i in 0..2){
            if (tableroVertical[i].filter { x -> x == 0 }.count() == 2) {
                movimeintoVertical(i)
                return reConvertirDatos()
            }
        }

        for (i in 0..2){
            if (tableroHorizontal[i].filter { x -> x == 0 }.count() == 1) {
                movimeintoHorizontal(i)
                return reConvertirDatos()
            }
        }
        for (i in 0..2){
            if (tableroVertical[i].filter { x -> x == 0 }.count() == 1) {
                movimeintoVertical(i)
                return reConvertirDatos()
            }
        }
        return null
    }
    /**********************************************************************************/
    fun selectorAleatorio(): IntArray {

        var num:Int = (Math.random() * 3).toInt()
        Log.i("num",num.toString())
        if (tableroHorizontal[num].filter { x-> x ==0 }.count() == 0){
            selectorAleatorio()
        }
        if(tableroHorizontal[num][0] == 0){
            tableroHorizontal[num][0] = 2
            Log.i("pos","0")
            return reConvertirDatos()
        }
        if(tableroHorizontal[num][1] == 0){
            tableroHorizontal[num][1] = 2
            Log.i("pos","1")
            return reConvertirDatos()
        }
        if(tableroHorizontal[num][2] == 0){
            tableroHorizontal[num][2] = 2
            Log.i("pos","2")
            return reConvertirDatos()
        }
        return reConvertirDatos()
    }
    /*****************************************************************************************************/
    /*******************************MOVIMIENTOS***********************************************************/
    /*****************************************************************************************************/
    fun movimeintoHorizontal(indice: Int) {
        if (tableroHorizontal[indice][0] == 0) {
            tableroHorizontal[indice][0] = 2
        } else if (tableroHorizontal[indice][1] == 0) {
            tableroHorizontal[indice][1] = 2
        } else if (tableroHorizontal[indice][2] == 0) {
            tableroHorizontal[indice][2] = 2
        }
        else{
            Log.i("ERROR H ",indice.toString())
        }
    }

    fun movimeintoVertical(indice: Int) {
        if (tableroHorizontal[0][indice] == 0) {
            tableroHorizontal[0][indice] = 2
        } else if (tableroHorizontal[1][indice] == 0) {
            tableroHorizontal[1][indice] = 2
        } else if (tableroHorizontal[2][indice] == 0) {
            tableroHorizontal[2][indice] = 2
        }
        else{
            Log.i("ERROR",indice.toString())
        }
    }

    fun movimientoDiagonal1() {
        if (tableroHorizontal[0][0] == 0) {
            tableroHorizontal[0][0] = 2
        } else if (tableroHorizontal[1][1] == 0) {
            tableroHorizontal[1][1] = 2
        } else if (tableroHorizontal[2][2] == 0) {
            tableroHorizontal[2][2] = 2
        }
    }

    fun movimientoDiagonal2() {
        if (tableroHorizontal[0][2] == 0) {
            tableroHorizontal[0][2] = 2
        } else if (tableroHorizontal[1][1] == 0) {
            tableroHorizontal[1][1] = 2
        } else if (tableroHorizontal[2][0] == 0) {
            tableroHorizontal[2][0] = 2
        }
    }
    /*****************************************************************************************************/
}