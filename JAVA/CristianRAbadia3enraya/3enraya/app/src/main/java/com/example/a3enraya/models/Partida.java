package com.example.a3enraya.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class Partida implements Serializable {
    private String jugador1,jugador2,abandono,ganador;
    private  boolean turno;
    private ArrayList<Integer> tablero;
    private Date fecha;

    public Partida() {
    }

    public Partida(String jugador1) {
        this.jugador1 = jugador1;
        this.jugador2 = "";
        this.abandono = "";
        this.ganador = "";
        this.turno = true;
        this.fecha = new Date();
        this.tablero = new ArrayList<>();
        for (int i = 0; i <9 ; i++) {
            this.tablero.add(0);
        }
    }

    public String getJugador1() {
        return jugador1;
    }

    public void setJugador1(String jugador1) {
        this.jugador1 = jugador1;
    }

    public String getJugador2() {
        return jugador2;
    }

    public void setJugador2(String jugador2) {
        this.jugador2 = jugador2;
    }

    public String getAbandono() {
        return abandono;
    }

    public void setAbandono(String abandono) {
        this.abandono = abandono;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public boolean isTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    public ArrayList<Integer> getTablero() {
        return tablero;
    }

    public void setTablero(ArrayList<Integer> tablero) {
        this.tablero = tablero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Partida{" +
                "jugador1='" + jugador1 + '\'' +
                ", jugador2='" + jugador2 + '\'' +
                ", abandono='" + abandono + '\'' +
                ", ganador='" + ganador + '\'' +
                ", turno=" + turno +
                ", tablero=" + tablero +
                ", fecha=" + fecha +
                '}';
    }
}
