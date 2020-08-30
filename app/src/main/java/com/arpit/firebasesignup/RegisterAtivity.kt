package com.arpit.firebasesignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register_.*

class RegisterAtivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_)

        auth = FirebaseAuth.getInstance()





        btnsignup.setOnClickListener {
                signupuser()
        }
    }


   private fun signupuser(){
      if (emailregister.text.toString().isEmpty()){
          emailregister.error="Please fill your email"
          emailregister.requestFocus()
          return

      }
      if (!Patterns.EMAIL_ADDRESS.matcher(emailregister.text.toString()).matches()){
          emailregister.error ="Please enter valid email"
          emailregister.requestFocus()
          return
      }

   if (passwordregister.text.toString().isEmpty()){
       passwordregister.error="Please enter your password"
       passwordregister.requestFocus()
       return
   }

       auth.createUserWithEmailAndPassword(emailregister.text.toString() ,passwordregister.text.toString())
           .addOnCompleteListener(this) { task ->
             if (task.isSuccessful){
                 val user = auth.currentUser
                 user?.sendEmailVerification()
                     ?.addOnCompleteListener {task ->
                       if (task.isSuccessful){
                           startActivity(Intent(this ,LoginActivity::class.java))
                           finish()
                       }
                     }
             }
           else{
                 Toast.makeText(this ,"Authentication Failed",Toast.LENGTH_SHORT).show()
             }
       }

    }
}