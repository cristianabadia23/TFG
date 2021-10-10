package com.example.a3enraya.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.a3enraya.R;
import com.example.a3enraya.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EndGameActivity extends AppCompatActivity {
    TextView result,nombre,puntos,total;
    LottieAnimationView animation,animation2;
    String resultado;
    User useraux;
    //Firebase
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser user;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);
        Bundle extras = getIntent().getExtras();
        this.resultado = extras.getString("resultado");
        this.result = findViewById(R.id.utotal);
        this.nombre = findViewById(R.id.unombre);
        this.puntos = findViewById(R.id.upuntos);
        this.total = findViewById(R.id.uinfo);
        this.animation = findViewById(R.id.uanimacion);
        this.animation2 = findViewById(R.id.uanimacion2);
        this.animation2.setVisibility(View.GONE);
        this.initFireBase();
        this.recuperarUsuario();

    }

    private void recuperarUsuario() {
        this.db.collection("users")
                .document(this.user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        useraux = documentSnapshot.toObject(User.class);
                        result.setText(resultado);

                        if(resultado.equals("Ganador")){
                           
                            useraux.setPuntos(useraux.getPuntos()+3);
                            puntos.setText("+3");
                        }
                        else if(resultado.equals("Perdedor")){
                            animation.setVisibility(View.GONE);
                            animation2.setVisibility(View.VISIBLE);
                            useraux.setPuntos(useraux.getPuntos()-3);
                            puntos.setText("-3");
                        }
                        else{
                            animation.setAnimation("empate.json");
                            puntos.setText("0");
                        }
                        useraux.setPartidas(useraux.getPartidas()+1);
                        nombre.setText(useraux.getNick());
                        total.setText(useraux.toString());

                        actualizarUser(useraux);
                    }
                });

    }

    private void initFireBase(){

            this.auth = FirebaseAuth.getInstance();
            this.db = FirebaseFirestore.getInstance();
            this.user = this.auth.getCurrentUser();
            this.uid = this.user.getUid();

    }


    private void actualizarUser(User aux) {
        this.db.collection("users")
                .document(this.uid)
                .set(aux)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
    }

    public void mostrarMenu(View v){
        Intent i = new Intent(EndGameActivity.this, EncontrarPartidaActivity.class);
        finish();
        startActivity(i);
    }

    public void cerrarSesion(View v){
        this.auth.getInstance().signOut();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent i = new Intent(EndGameActivity.this, EncontrarPartidaActivity.class);
        startActivity(i);
    }
}