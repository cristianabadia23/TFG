package com.example.a3enraya.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import com.example.a3enraya.R;
import com.example.a3enraya.models.User;
import com.example.a3enraya.utils.UserAdapter;
import com.example.a3enraya.vmachine.FacilActivity;
import com.example.a3enraya.vmachine.MachineActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RankingActivity extends AppCompatActivity {
    RecyclerView usuarios;
    UserAdapter adapter;
    Query query;
    //firebase
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser user;

    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        this.initFireBase();
        this.usuarios = findViewById(R.id.usuarios);
        this.usuarios.setLayoutManager(new LinearLayoutManager(this));
        this.query = this.db.collection("users").orderBy("puntos", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<User> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(this.query,User.class)
                        .build();


        this.adapter = new UserAdapter(firestoreRecyclerOptions);
        this.adapter.notifyDataSetChanged();
        this.usuarios.setAdapter(this.adapter);

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
        this.adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.adapter.stopListening();

    }
}