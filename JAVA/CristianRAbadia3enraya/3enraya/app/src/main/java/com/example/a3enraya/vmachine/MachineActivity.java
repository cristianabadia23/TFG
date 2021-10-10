package com.example.a3enraya.vmachine;

import androidx.appcompat.app.AppCompatActivity;
import com.example.a3enraya.R;
import com.example.a3enraya.ui.EncontrarPartidaActivity;
import com.example.a3enraya.ui.GameActivity;
import com.example.a3enraya.ui.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MachineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);
    }

    public void mostrarDificil(View v){
        Intent i = new Intent(MachineActivity.this, DificilActivity.class);
        startActivity(i);
    }

    public void mostrarFacil(View v){
        Intent i = new Intent(MachineActivity.this, FacilActivity.class);
        startActivity(i);
    }

    public void mostrarLogin(View v){
        Intent i = new Intent(MachineActivity.this, LoginActivity.class);
        startActivity(i);
    }
}