package com.example.a3enraya.vmachine;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.example.a3enraya.R;
import com.example.a3enraya.models.Partida;
import com.example.a3enraya.ui.EncontrarPartidaActivity;
import com.example.a3enraya.ui.EndGameActivity;
import com.example.a3enraya.ui.GameActivity;
import com.example.a3enraya.ui.RankingActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FacilActivity extends AppCompatActivity {
    List<ImageView> casillas;
    List<Integer> respaldo;
    String campeon;
    int aux[][];

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facil);
        this.aux = new int[3][3];
        this.campeon="";
        this.respaldo = new ArrayList<>();
        for (int i = 0; i <9 ; i++) {
            this.respaldo.add(0);
        }
        this.initCasillas();
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void casillaPulsada(View v){
        if (this.campeon.isEmpty()){
            this.actualizarPartida(Integer.parseInt(v.getTag().toString()));
            if(this.respaldo.stream().filter(x->x!=0).count() < 9){
                this.movimientoAleatorio();
            }
        }
        else{
            mostrarDialogo();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void movimientoAleatorio() {
        this.aux = this.convertirDatos();
        int aleatorio = (int) (Math.random()*3);
        Long cuenta = Arrays.stream(this.aux[aleatorio]).filter(x->x == 0).count();

        if (cuenta == 0){
            movimientoAleatorio();
        }

        mover(aleatorio);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void mover(int aleatorio){
        if(this.aux[aleatorio][0] == 0){
            this.aux[aleatorio][0] = 2;
            this.restructurarDatos();
            return;
        }

        if(this.aux[aleatorio][1] == 0){
            this.aux[aleatorio][1] = 2;
            this.restructurarDatos();
            return;
        }

        if (this.aux[aleatorio][2] == 0){
            this.aux[aleatorio][2] = 2;
            this.restructurarDatos();
            return;
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void restructurarDatos(){
        int cont =0;
        for (int i = 0; i <this.aux.length ; i++) {
            for (int j = 0; j <this.aux[i].length ; j++) {
                this.respaldo.set(cont,this.aux[i][j]);
                cont++;
            }
        }
        this.actualizarTablero();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void actualizarPartida(int ncasilla) {
        if (this.respaldo.get(ncasilla) == 0) {
            this.casillas.get(ncasilla).setImageResource(R.drawable.skull);
            this.respaldo.set(ncasilla, 1);
            this.actualizarTablero();

        }
        else{
            Toast.makeText(this,"Casilla ocupada",Toast.LENGTH_LONG);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void actualizarTablero() {
        for (int i = 0; i <this.respaldo.size() ; i++) {
            if(this.respaldo.get(i) == 0){
                this.casillas.get(i).setImageResource(R.drawable.casilla);
                System.out.println(this.casillas.get(i).getDrawable());
            }
            else if(this.respaldo.get(i) == 1){
                this.casillas.get(i).setImageResource(R.drawable.skull);
            }
            else{
                this.casillas.get(i).setImageResource(R.drawable.fantasma);
            }

        }
        int winner = this.ganador();
        if (winner != 0){
            if (winner == 1){
                this.campeon ="HUMAN";

            }
            else if(winner == 2){
                this.campeon ="Maquina";

            }
            else {
                this.campeon="Empate";
            }
        }
        if(!this.campeon.isEmpty()){
            this.mostrarDialogo();
        }

    }

    private void mostrarDialogo() {
        System.out.println("Mostrar dialogo");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setCancelable(false);
        View v = getLayoutInflater().inflate(R.layout.activity_machine_end,null);
        LottieAnimationView animation = v.findViewById(R.id.mmanimacion);
        if (this.campeon.equals("Maquina")){
            animation.setAnimation("robot.json");
        }
        else if(this.campeon.equals("Empate")){
            animation.setAnimation("empate.json");
        }
        builder.setView(v);

        builder.setPositiveButton("\tMENU", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(FacilActivity.this, MachineActivity.class);
                finish();
                startActivity(i);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public int[][] convertirDatos(){
        int tableroAux[][] = new int[3][3];
        int cont = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tableroAux[i][j] = this.respaldo.get(cont);
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
        else if(this.respaldo.stream().filter(x-> x!=0).count() == 9){
            return 3;
        }
        else {
            return 0;
        }

    }
}