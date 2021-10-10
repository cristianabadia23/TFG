package com.example.a3enraya.ui;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.a3enraya.R;
import com.example.a3enraya.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {
    //Declaro las variables
    EditText email,contra,nick,rcontra;
    TextView error;
    Button blogin;
    Switch condiciones;
    ProgressBar progressBar;
    Pattern correo;
    //Declaramos los objetos de firebase
    FirebaseAuth autentificacion;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //iniciamos las variables con los componentes correspondientes del activity
        this.nick = findViewById(R.id.nick);
        this.email = findViewById(R.id.email);
        this.contra = findViewById(R.id.contra);
        this.rcontra = findViewById(R.id.rcontra);
        this.blogin = findViewById(R.id.blogin);

        this.progressBar = findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.GONE);
        this.correo = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        this.error = findViewById(R.id.req);
        this.autentificacion = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    public void validarUser(View v){
        String vCorreo = this.email.getText().toString();
        Matcher mather = this.correo.matcher(vCorreo);
        if (this.nick.getText().toString().isEmpty()){
            this.nick.setError("NICK NO VALIDO");
        }
        else if (vCorreo.isEmpty() && !mather.find()){
            this.email.setError("CORREO NO VALIDO");
        }
        else if (this.contra.getText().toString().length() < 8){
            this.contra.setError("Contraseña no valida");
        }
        else if(!this.contra.getText().toString().equals(this.rcontra.getText().toString())){
            this.contra.setError("Las contraseñas no coinciden");
        }

        else {
            crearUser(this.nick.getText().toString(),this.email.getText().toString(),
                    this.contra.getText().toString());
        }
    }

    private void crearUser(final String nick, String email, String pass) {
        this.autentificacion.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = autentificacion.getCurrentUser();
                            if (user != null){
                                registrar(user,nick);
                            }

                        }
                        else{
                            mostrarToast("USUARIO NO CREADO");
                        }
                    }
                });

    }
    private void ocultarForm(){
        this.nick.setVisibility(View.GONE);
        this.email.setVisibility(View.GONE);
        this.contra.setVisibility(View.GONE);
        this.rcontra.setVisibility(View.GONE);
        this.blogin.setVisibility(View.GONE);
        this.condiciones.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.VISIBLE);
    }
    private void registrar(FirebaseUser user,String nick) {
        User user1 = new User(nick,0,0);
        this.db.collection("users")
                .document(user.getUid())
                .set(user1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mostrarToast("Registro Correcto");
                        finish();
                        cambiarDePantalla();
                    }
                });
    }

    private void mostrarToast(String mens) {
        Toast toast = Toast.makeText(this,mens,Toast.LENGTH_LONG);
        toast.show();
    }
    public void cambiarDePantalla(){
        Intent i = new Intent(RegistroActivity.this,EncontrarPartidaActivity.class);
        startActivity(i);
    }



}