package com.arpit.firebasesignup

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.forget_password.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

             auth =FirebaseAuth.getInstance()

        btnregister.setOnClickListener {
             val intent = Intent(this,RegisterAtivity::class.java)
            startActivity(intent)
            finish()

        }

        btnlogin.setOnClickListener {
            doLogin()
        }
        changePassword.setOnClickListener {
               startActivity(Intent(this ,ChangePassword::class.java))
           }

        forgetpassword.setOnClickListener {
            val builder= AlertDialog.Builder(this)
            builder.setTitle("Forget Password")
            val view = layoutInflater.inflate(R.layout.forget_password ,null)
            val userEmail= view.findViewById<EditText>(R.id.verifymail)
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
                forgetPassword(userEmail)
            })

            builder.setNegativeButton("Close", DialogInterface.OnClickListener { _, _ ->  })
            builder.show()
        }
    }


    private fun forgetPassword(userEmail:EditText){
        if (userEmail.text.toString().isEmpty()){
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.text.toString()).matches()){
            return
        }

        auth.sendPasswordResetEmail(userEmail.text.toString())
            .addOnCompleteListener{task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Email sent" ,Toast.LENGTH_SHORT).show()
            }

        }
    }

     private fun doLogin() {
    if (emailLogin.text.toString().isEmpty()){
        emailLogin.error="Please enter your email"
        emailLogin.requestFocus()
        return
    }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailLogin.text.toString()).matches()){
            emailLogin.error="Please enter valid email"
            emailLogin.requestFocus()
            return
        }

        if (passwordLogin.text.toString().isEmpty()){
            passwordLogin.error="Please enter your password"
            passwordLogin.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(emailLogin.text.toString() ,passwordLogin.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    updateUI(user)
                }
                else{
                    updateUI(null)
                }

            }

    }

    public override fun onStart() {
        super.onStart()
        val currentUser=auth.currentUser
        updateUI(currentUser)
    }



    fun updateUI(currentUser:FirebaseUser?){
        if (currentUser!=null){
              if (currentUser.isEmailVerified){
                  startActivity(Intent(this ,MainActivity::class.java))
                  finish()
              }
            else{
                  Toast.makeText(this ,"Please verify your mail",Toast.LENGTH_SHORT).show()
              }

        }

    }
}