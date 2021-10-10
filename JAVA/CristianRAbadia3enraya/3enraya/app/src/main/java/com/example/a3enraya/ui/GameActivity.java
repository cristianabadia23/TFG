package com.example.a3enraya.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.a3enraya.models.Partida;
import com.example.a3enraya.models.User;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3enraya.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    //varibles de layour
    List<ImageView> casillas;
    TextView jugador1,jugador2;
    int aux[][];
    //firebase
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser user;
    ListenerRegistration listener;
    String nJugador1="",nJugador2="",uid,pid;
    Partida partida;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.jugador1 = findViewById(R.id.jugador1);
        this.jugador2 = findViewById(R.id.jugador2);
        this.initCasillas();
        this.initFireBase();
        Bundle extras = getIntent().getExtras();
        this.pid = extras.getString("jugadaID");
        this.jugador2.setTextColor(getResources().getColor(R.color.accent));
        this.jugador1.setTextColor(getResources().getColor(R.color.colorPrimaryTextColor));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initCasillas(){
        this.casillas = new ArrayList<>();
        this.casillas.add((ImageView) findViewById(R.id.imageView0));
        this.casillas.add((ImageView) findViewById(R.id.imageView1));
        this.casillas.add((ImageView) findViewById(R.id.imageView2));
        this.casillas.add((ImageView) findViewById(R.id.imageView3));
        this.casillas.add((ImageView) findViewById(R.id.imageView4));
        this.casillas.add((ImageView) findViewById(R.id.imageView5));
        this.casillas.add((ImageView) findViewById(R.id.imageView6));
        this.casillas.add((ImageView) findViewById(R.id.imageView7));
        this.casillas.add((ImageView) findViewById(R.id.imageView8));
        this.casillas.forEach(x->x.setVisibility(View.VISIBLE));
        this.casillas.forEach(x->x.setImageResource(R.drawable.casilla));
    }

    public void initFireBase(){
        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.user = this.auth.getCurrentUser();
        this.uid = this.user.getUid();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.escucharMovimiento();
    }

    @Override
    protected void onStop() {
        if (this.listener != null){
            this.listener.remove();
        }
        super.onStop();
    }

    public void escucharMovimiento(){
        this.listener = this.db.collection("jugadas")
                .document(pid)
                .addSnapshotListener(GameActivity.this,
                        new EventListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        String source = value != null && value
                                .getMetadata()
                                .hasPendingWrites() ? "Local" : "Server";
                        if (value.exists() && source.equals("Server")){
                            partida = (Partida) value.toObject(Partida.class);
                            if (nJugador1.isEmpty() || nJugador2.isEmpty()){
                                getPlayerGame();
                            }
                            actualizarTablero();
                        }
                    }
                });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void actualizarTablero() {
        for (int i = 0; i <this.partida.getTablero().size() ; i++) {
            if(this.partida.getTablero().get(i) == 0){
                this.casillas.get(i).setImageResource(R.drawable.casilla);
            }
            else if(this.partida.getTablero().get(i) == 1){
                this.casillas.get(i).setImageResource(R.drawable.skull);
            }
            else{
                this.casillas.get(i).setImageResource(R.drawable.fantasma);
            }

        }
        if(!partida.getGanador().isEmpty()){
           this.cambiarDePantalla();
        }

        if(!partida.getAbandono().isEmpty()){
            this.rendicion();
        }

    }

    private void rendicion() {
        Intent i = new Intent(GameActivity.this, EndGameActivity.class);
        this.partida.setGanador(this.uid);
        this.actualizarFirebase();
        i.putExtra("resultado","Ganador");
        finish();
        startActivity(i);
    }

    private void getPlayerGame(){
        this.db.collection("users")
                .document(this.partida.getJugador1())
                .get()
                .addOnSuccessListener(GameActivity.this,
                        new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        nJugador1 = documentSnapshot.get("nick").toString();
                        jugador1.setText(nJugador1);
                    }
                });
        this.db.collection("users")
                .document(this.partida.getJugador2())
                .get()
                .addOnSuccessListener(GameActivity.this,
                        new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        nJugador2 = documentSnapshot.get("nick").toString();
                        jugador2.setText(nJugador2);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void casillaPulsada(View v){
        
        if (this.partida.getGanador().isEmpty()){
            if((this.partida.isTurno() && this.partida.getJugador1().equals(this.uid))||
                    (!this.partida.isTurno() && this.partida.getJugador2().equals(this.uid))
            ){

                this.actualizarPartida(Integer.parseInt(v.getTag().toString()));
            }


            else{
                Toast.makeText(this,"No es tu turno",Toast.LENGTH_LONG).show();

            }
        }
        else{
            int winner = this.ganador();

            if (winner != 0){

                if (winner == 1){
                    this.partida.setGanador(this.partida.getJugador1());

                }
                else if(winner == 2){
                    this.partida.setGanador(this.partida.getJugador2());

                }
                else {
                    this.partida.setGanador("EMPATE");

                }
                this.cambiarDePantalla();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void actualizarPartida(int ncasilla) {
        if (this.partida.getTablero().get(ncasilla) == 0) {

            if (this.partida.isTurno()) {
                this.casillas.get(ncasilla).setImageResource(R.drawable.skull);
                this.partida.getTablero().set(ncasilla, 1);
            } else {
                this.casillas.get(ncasilla).setImageResource(R.drawable.fantasma);
                this.partida.getTablero().set(ncasilla, 2);
            }
            this.partida.setTurno(!this.partida.isTurno());
            int winner = this.ganador();
            String resultado="";
            if (winner != 0){

                if (winner == 1){
                    this.partida.setGanador(this.partida.getJugador1());

                }
                else if(winner == 2){
                    this.partida.setGanador(this.partida.getJugador2());

                }
                else {
                    this.partida.setGanador("EMPATE");
                }
                this.cambiarDePantalla();
            }


            this.actualizarFirebase();
        }
        else{
            Toast.makeText(this,"Casilla ocupada",Toast.LENGTH_LONG);
        }
    }

    private void actualizarFirebase() {
        this.db.collection("jugadas").document(this.pid)
                .set(this.partida).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public int[][] convertirDatos(){
        int tableroAux[][] = new int[3][3];
        int cont = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tableroAux[i][j] = this.partida.getTablero().get(cont);
                cont++;
            }
        }
        return tableroAux;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int horizontal(int jugador){
        for (int i = 0; i <this.aux.length ; i++) {
            if(Arrays.stream(aux[i]).filter(aux -> aux == jugador).count() == 3){
                return jugador;
            }
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int vertical(int jugador){
        for (int i = 0; i < this.aux.length; i++) {
            int[] tabaux = new int[3];
            for (int j = 0; j <this.aux[i].length ; j++) {
                tabaux[j]=this.aux[j][i];
            }
            if(Arrays.stream(tabaux).filter(aux -> aux == jugador).count() == 3){
                return jugador;
            }

        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int diagonalFacil(int jugador){
        int[] tabaux = new int[3];
        for (int i = 0; i < this.aux.length; i++) {
            tabaux[i] = this.aux[i][i];
        }
        if(Arrays.stream(tabaux).filter(aux -> aux == jugador).count() == 3){
            return jugador;
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int diagonalDificil(int jugador){
        int[] tabaux = new int[3];
        tabaux[0] = this.aux[0][2];
        tabaux[1] = this.aux[1][1];
        tabaux[2] = this.aux[2][0];
        if(Arrays.stream(tabaux).filter(aux -> aux == jugador).count() == 3){
            return jugador;
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int ganador(){
        this.aux = this.convertirDatos();
        if(this.vertical(1) == 1){
            return 1;
        }
        else if(this.vertical(2) == 2){
            return 2;
        }
        else if(this.horizontal(1) == 1){
            return 1;
        }
        else if(this.horizontal(2) == 2){
            return 2;
        }
        else if (this.diagonalFacil(1) == 1){
            return 1;
        }
        else if(this.diagonalFacil(2) == 2){
            return 2;
        }
        else if(this.diagonalDificil(1) == 1){
            return 1;
        }
        else if(this.diagonalDificil(2) == 2){
            return 2;
        }
        else if(this.partida.getTablero().stream().filter(x-> x!=0).count() == 9){
            return 3;
        }
        else {
            return 0;
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void cambiarDePantalla(){
        String resultado="";
        if(this.ganador() == 3){
            resultado = "Empate";
        }
        else {
                resultado = partida.getGanador().equals(uid) ? "Ganador" : "Perdedor";
        }
        Intent i = new Intent(GameActivity.this, EndGameActivity.class);
        i.putExtra("resultado",resultado);
        finish();
        startActivity(i);
    }

    public void abandonar(View v){
        this.partida.setAbandono(this.uid);

        this.actualizarFirebase();
        Intent i = new Intent(GameActivity.this, EndGameActivity.class);
        i.putExtra("resultado","Perdedor");
        finish();
        startActivity(i);
    }
}