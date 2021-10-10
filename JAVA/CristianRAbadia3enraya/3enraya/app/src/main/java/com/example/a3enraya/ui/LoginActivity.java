package com.example.a3enraya.ui;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.a3enraya.R;
import com.example.a3enraya.models.User;
import com.example.a3enraya.vmachine.MachineActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    //Declaro las variables
    EditText email, contra,nick,cemail;
    Button blogin, resgistro,bnick,bemail,offline;
    String snick;
    LinearLayout completarNick,lemail;
    TextView rescon;
    Pattern correo;
    //Firebase
    FirebaseAuth autentificacion;
    FirebaseFirestore almacenamiento;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton loogingoogle;
    private final int RC_SIGN_IN = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //iniciamos las variables con los componentes correspondientes del activity
        this.email = findViewById(R.id.email);
        this.contra = findViewById(R.id.contra);
        this.blogin = findViewById(R.id.blogin);
        this.resgistro = findViewById(R.id.registro);
        this.nick = findViewById(R.id.nick);
        this.bnick = findViewById(R.id.bnick);
        this.rescon = findViewById(R.id.rescon);
        this.offline = findViewById(R.id.offline);
        this.completarNick = findViewById(R.id.completarNick);
        this.correo = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        this.cemail = findViewById(R.id.cemail);
        this.bemail = findViewById(R.id.bemail);
        this.lemail = findViewById(R.id.lemail);
        this.lemail.setVisibility(View.GONE);
        //firebase
        this.autentificacion = FirebaseAuth.getInstance();
        this.almacenamiento = FirebaseFirestore.getInstance();
        this.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        this.mGoogleSignInClient = GoogleSignIn.getClient(this,this.gso);
        this.loogingoogle = findViewById(R.id.logingoogle);
        this.loogingoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    @Override 
    protected void onStart() {
        super.onStart();
        FirebaseUser user = this.autentificacion.getCurrentUser();
        if (user != null){
            cambiarDePantalla();
        }
    }

    public void entrar(View v){
        String vCorreo = this.email.getText().toString();
        Matcher mather = this.correo.matcher(vCorreo);
        if (vCorreo.isEmpty() && !mather.find()){
            this.email.setError("CORREO NO VALIDO");
        }
        else if (this.contra.getText().toString().length() < 8){
            this.contra.setError("CONTRASEÑA NO VALIDA");
        }
        else{
            iniciarSesion(this.email.getText().toString(),this.contra.getText().toString());
        }
    }

    public void cambiarDePantalla(View v){
        Intent i = new Intent(LoginActivity.this,RegistroActivity.class);
        startActivity(i);
    }

    private void mostrarToast(String mens) {
        Toast toast = Toast.makeText(this,mens,Toast.LENGTH_LONG);
        toast.show();
    }

    public void verificarNick(View view){
        if (!this.nick.getText().toString().isEmpty()){
            this.snick = this.nick.getText().toString();
            final String id = autentificacion.getUid();
            this.almacenamiento.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (!documentSnapshot.exists()){
                        completarNick.setVisibility(View.VISIBLE);
                        User user1 = new User(snick,0,0);
                        almacenamiento.collection("users")
                                .document(id)
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
                    else{
                        cambiarDePantalla();
                    }
                }
            });
        }
        else{
            this.nick.setError("NICK NO VALIDO");
        }
    }

    private void cambiarDePantalla(){
        Intent i = new Intent(LoginActivity.this,EncontrarPartidaActivity.class);
        startActivity(i);
        finish();
    }

    private void mostrarNick(boolean estado,int num){
        this.email.setVisibility(!estado ? View.VISIBLE : View.GONE);
        this.contra.setVisibility(!estado ? View.VISIBLE : View.GONE);
        this.blogin.setVisibility(!estado ? View.VISIBLE : View.GONE);
        this.resgistro.setVisibility(!estado ? View.VISIBLE : View.GONE);
        this.loogingoogle.setVisibility(!estado ? View.VISIBLE : View.GONE);
        this.offline.setVisibility(!estado ? View.VISIBLE : View.GONE);
        this.rescon.setVisibility(View.VISIBLE);
        if(num == 0) {
            this.completarNick.setVisibility(estado ? View.VISIBLE : View.GONE);
            this.rescon.setVisibility(View.GONE);
        }
        else if(num == 1){
            this.lemail.setVisibility(estado ? View.VISIBLE : View.GONE);
            this.rescon.setVisibility(View.GONE);
        }

    }

    private void iniciarSesion(String email, String contra) {
        this.autentificacion.signInWithEmailAndPassword(email,contra)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = autentificacion.getCurrentUser();
                            if (user != null){
                                mostrarToast("ACCEDIENDO A SU CUENTA");
                                cambiarDePantalla();
                            }
                            else{
                                mostrarToast("EMAIL/CONTRASEÑA INCORRECTOS");
                            }
                        }
                        else{
                            mostrarToast("EMAIL/CONTRASEÑA INCORRECTOS");
                        }
                    }
                });
    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Error", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("Error", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        this.autentificacion.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            verificarUser(autentificacion.getUid());
                        } else {
                            mostrarToast("ERROR AL HACER LOGGING CON GOOGLE");
                        }
                    }
                });
    }

    public void verificarUser(final String id){
        this.almacenamiento.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    mostrarNick(true,0);
                }
                else{
                    cambiarDePantalla();
                }
            }
        });
    }


    public void resetearPass(View view) {
        System.out.println("Texto pulsado");
        this.mostrarNick(true,1);

    }

    public void mandarEmail(View v){
        if (!this.cemail.getText().toString().isEmpty()){
            this.autentificacion.setLanguageCode("es");
            this.autentificacion.sendPasswordResetEmail(this.cemail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mostrarToast("Mensaje enviado");
                                mostrarNick(false,3);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStop() {
        this.mostrarNick(false,3);
        super.onStop();
    }

    public void offLine(View v){
        Intent i = new Intent(LoginActivity.this, MachineActivity.class);
        startActivity(i);
    }
}