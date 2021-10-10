package com.example.a3enraya.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.a3enraya.R;
import com.example.a3enraya.models.Partida;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

public class EncontrarPartidaActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView;
    Button jugar,ranking;
    String uid,jugadaID="";
    boolean partidaAceptada=false;
    //firebase
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser user;
    ListenerRegistration listener = null;
    //lottie
    LottieAnimationView animation,animation2,animation3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encontrar_partida);
        this.progressBar = findViewById(R.id.progressBar2);
        this.textView = findViewById(R.id.textView);
        this.jugar = findViewById(R.id.jugar);
        this.ranking = findViewById(R.id.ranking);
        this.progressBar.setVisibility(View.GONE);
        this.textView.setVisibility(View.GONE);
        this.animation = findViewById(R.id.animacion);
        this.animation2 = findViewById(R.id.animacion1);
        this.animation3 = findViewById(R.id.animacion2);
        this.animation.setVisibility(View.VISIBLE);
        this.animation2.setVisibility(View.GONE);
        this.animation3.setVisibility(View.GONE);
        this.initFireBase();
    }
    public void initFireBase(){
        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.user = this.auth.getCurrentUser();
        this.uid = this.user.getUid();
    }
    public void buscarPartida(View v){
        this.animation.setVisibility(View.GONE);
        this.animation2.setVisibility(View.VISIBLE);
        this.animation3.setVisibility(View.GONE);
        this.mostrar(true);
        this.textView.setText("BUSCANDO PARTIDA ...");
        this.animation.setAnimation("carga.json");
        this.buscarPartidaIniciada();
    }

    public void mostrarRanking(View v){
        this.animation.setVisibility(View.GONE);
        this.animation2.setVisibility(View.GONE);
        this.animation3.setVisibility(View.VISIBLE);
        this.mostrar(true);
        this.textView.setText("CARGANDO RANKING ...");
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(EncontrarPartidaActivity.this,RankingActivity.class);
                startActivity(i);
            }
        };
        handler.postDelayed(runnable,1500);


    }

    public void mostrar(boolean estado){
        this.progressBar.setVisibility(estado ? View.VISIBLE : View.GONE);
        this.progressBar.setIndeterminate(estado);
        this.textView.setVisibility(estado ? View.VISIBLE : View.GONE);
        this.jugar.setVisibility(!estado ? View.VISIBLE : View.GONE);
        this.ranking.setVisibility(!estado ? View.VISIBLE : View.GONE);

    }

    //Firebase
    public void cerrarSesion(View v){
        this.auth.getInstance().signOut();
        finish();
        Intent i = new Intent(EncontrarPartidaActivity.this,LoginActivity.class);
        startActivity(i);
    }

    public void buscarPartidaIniciada(){
        this.textView.setText("Buscando partida libre ...");
        this.db.collection("jugadas")
                .whereEqualTo("jugador2","")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size() == 0){
                            crearPartida();
                        }
                        else{
                            DocumentSnapshot documentSnapshot = task.getResult()
                                    .getDocuments()
                                    .get(0);
                            jugadaID = documentSnapshot.getId();
                            Partida partida = documentSnapshot.toObject(Partida.class);
                            partida.setJugador2(uid);
                            db.collection("jugadas")
                                    .document(jugadaID)
                                    .set(partida)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            textView.setText("Partida encontrada!!!");
                                            final Handler handler = new Handler();
                                            final Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    cambiarpantalla();
                                                }
                                            };
                                            handler.postDelayed(runnable,1500);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("Fallo");
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Partida no encontrada");
            }
        });
    }

    private void cambiarpantalla() {
        this.partidaAceptada = true;
        finish();
        Intent i = new Intent(EncontrarPartidaActivity.this,GameActivity.class);
        i.putExtra("jugadaID",jugadaID);
        startActivity(i);

    }

    private void crearPartida() {
        this.textView.setText("Creando partida ...");
        Partida partida = new Partida(this.uid);
        this.db.collection("jugadas")
                .add(partida)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        jugadaID = documentReference.getId();
                        esperarJugador();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Fallo al crear la partida");
            }
        });
    }

    private void esperarJugador() {
        this.listener = this.db.collection("jugadas")
                .document(jugadaID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.get("jugador2").equals("")){

                            textView.setText("Ya ha llegado el jugador !!!");
                            final Handler handler = new Handler();
                            final Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cambiarpantalla();
                                }
                            };
                            handler.postDelayed(runnable,1500);

                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        this.animation.setVisibility(View.VISIBLE);
        this.animation2.setVisibility(View.GONE);
        this.animation3.setVisibility(View.GONE);
        this.mostrar(false);
        if (this.listener !=null){
            listener.remove();

        }
        if (!this.jugadaID.isEmpty() && !this.partidaAceptada){
            this.db.collection("jugadas")
                    .document(this.jugadaID)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            animation2.setVisibility(View.GONE);
                            animation3.setVisibility(View.GONE);
                            animation.setVisibility(View.VISIBLE);
                            jugadaID="";
                        }
                    });
        }
        finish();
        super.onStop();
    }

    @Override
    protected void onStart() {
        this.animation2.setVisibility(View.GONE);
        this.animation3.setVisibility(View.GONE);
        this.animation.setVisibility(View.VISIBLE);

        if (this.user == null){
            finish();
        }
        super.onStart();

    }
}