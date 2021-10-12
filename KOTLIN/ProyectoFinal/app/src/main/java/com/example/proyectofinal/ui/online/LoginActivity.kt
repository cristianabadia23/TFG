package com.example.proyectofinal.ui.online

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.providers.AuthProvider
import com.example.proyectofinal.providers.UserProvider
import com.example.proyectofinal.ui.offline.SelectorActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.alertpassword.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private val GOOGLE_SING_IN = 100
    private val authProvider:AuthProvider = AuthProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tatras.setOnClickListener{
            startActivity(Intent(this@LoginActivity,SelectorActivity::class.java))
        }
        tregistrar.setOnClickListener{
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }
        bgoogle.setOnClickListener{it->
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent,GOOGLE_SING_IN)
        }
        bentrar.setOnClickListener {
            Log.i("FIREBASE/AUTH","Boton pulsado")

            if ((mail.text.isNotEmpty() && validarEmail(mail.text.toString())) &&
                    (pasLogin.text.isNotEmpty() && pasLogin.text.length > 9)) {
                Log.i("FIREBASE/AUTH","datos correctos")
                AuthProvider().login(mail.text.toString(), pasLogin.text.toString())!!.addOnSuccessListener {
                            Log.i("FIREBASE/AUTH","inicio correcto")
                    startActivity(Intent(this@LoginActivity,MainOnlineActivity::class.java))
                }?.addOnFailureListener {
                    Toast.makeText(this, "ERROR AL INICIAR SESION", Toast.LENGTH_LONG).show()
                }
                Log.i("FIREBASEAUTH",AuthProvider().getUid().toString())
            } else if (mail.text.isEmpty() || !validarEmail(mail.text.toString())) {
                mail.error = "CORREO NO VALIDO !!";
            } else if (pasLogin.text.isEmpty() || pasLogin.text.length < 9) {
                pasLogin.error = "CONTRASEÑA NO VALIDA !!"
            }
            else{
                Log.e("Fallo","otro fallo")
            }
        }
        reccontra.setOnClickListener {
            var dialog = Dialog(this@LoginActivity)
            dialog.setContentView(R.layout.alertpassword)
            dialog.setCancelable(false)
            dialog.show()
            dialog.benviar.setOnClickListener{
                if (dialog.emailcambio.text.isNotEmpty() && validarEmail(dialog.emailcambio.text.toString())){
                    AuthProvider().mAuth.sendPasswordResetEmail(dialog.emailcambio.text.toString())
                            .addOnCompleteListener{
                                if(it.isSuccessful) {
                                    Toast.makeText(this, "Email enviado", Toast.LENGTH_LONG).show()
                                    dialog.dismiss()
                                }
                                else {
                                    Toast.makeText(this, "Email no enviado", Toast.LENGTH_LONG).show()
                                }
                            }
                }
                else{
                    dialog.emailcambio.error ="Email no valido"
                }
            }
            dialog.bcancelar.setOnClickListener{
                dialog.dismiss()
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != null){
            when(requestCode){
                GOOGLE_SING_IN ->{
                    Toast.makeText(this,"Correo recibido",Toast.LENGTH_LONG).show()
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try{
                        val account = task.getResult(ApiException::class.java)
                        if (account != null){
                            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                            authProvider.signGoogle(credential).addOnCompleteListener{it->
                                if (it.isSuccessful){
                                   verificarUser()
                                }
                                else{
                                    Toast.makeText(this,"Error al usar Google",Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    catch (e:ApiException){
                        Log.e("E/GOOGLE",e.stackTraceToString())
                    }
                }
            }
        }
    }

    private fun verificarUser() {
        AuthProvider().getUid()?.let { it1 ->
            UserProvider().read(it1).addOnSuccessListener {
                if (it.exists()){
                    startActivity(Intent(this@LoginActivity,MainOnlineActivity::class.java))
                }
                else{
                    startActivity(Intent(this@LoginActivity,CompletarPerfilActivity::class.java))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(authProvider.getUserSession()!=null){
            startActivity(Intent(this@LoginActivity,MainOnlineActivity::class.java))
        }
    }
    private fun validarEmail(email: String):Boolean{
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }
}