package com.example.a3enraya.models;

public class User {
    private String nick;
    private int puntos;
    private int partidas;

    public User() {
    }

    public User(String nick, int puntos, int partidas) {
        this.nick = nick;
        this.puntos = puntos;
        this.partidas = partidas;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getPartidas() {
        return partidas;
    }

    public void setPartidas(int partidas) {
        this.partidas = partidas;
    }

    @Override
    public String toString() {
        return this.nick +"\nPuntos:\t" + this.puntos + "\nPartidas:\t" + this.partidas;
    }
}
