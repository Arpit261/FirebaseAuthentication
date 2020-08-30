package com.arpit.firebasesignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePassword : AppCompatActivity() {

     private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        auth= FirebaseAuth.getInstance()

        btnchangepassword.setOnClickListener {
            checkPassword()
        }

    }
    private fun checkPassword(){
        if (currentPassword.text.toString().isNotEmpty() && newPassword.text.toString().isNotEmpty()&&confirmPassword.text.toString().isNotEmpty()){

            if (newPassword.text.toString() == confirmPassword.text.toString()){

             val user = auth.currentUser

                if (user!=null && user.email!=null){

                    val Credentials=EmailAuthProvider.getCredential(user.email!!,currentPassword.text.toString())

                    user?.reauthenticate(Credentials)?.addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this ,"Re-authentication Success" ,Toast.LENGTH_SHORT).show()

                            user?.updatePassword(newPassword.text.toString()).addOnCompleteListener {
                                task ->
                                if (task.isSuccessful){
                                    Toast.makeText(this ,"Password Change Successfully" ,Toast.LENGTH_SHORT).show()
                                    auth.signOut()
                                    startActivity(Intent(this ,LoginActivity::class.java))
                                    finish()
                                }
                            }
                        }
                        else{
                            Toast.makeText(this ,"Re-authentication Failed" ,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    startActivity(Intent(this,LoginActivity::class.java))
                }

            }
            else{
                Toast.makeText(this ,"Password Mismatching" ,Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this ,"Please fill your Credentials" ,Toast.LENGTH_SHORT).show()
        }
    }
}