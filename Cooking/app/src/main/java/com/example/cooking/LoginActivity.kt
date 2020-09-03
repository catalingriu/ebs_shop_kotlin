package com.example.cooking

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.w3c.dom.Text

class LoginActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentification_layout)

        val register_button: Button = findViewById(R.id.register_button)
        val email_input: TextInputLayout = findViewById(R.id.email_InputLayout)
        val password_input: TextInputLayout = findViewById(R.id.password_InputLayout)
        val verify_password_input: TextInputLayout = findViewById(R.id.verify_password_InputLayout)


        register_button.setOnClickListener {
            //val intent = Intent(this, MainActivity::class.java)
           // startActivity(intent)
            val email = email_input.editText?.text.toString()
            val password = password_input.editText?.text.toString()
            val verify_passord = verify_password_input.editText?.text.toString()

            auth = FirebaseAuth.getInstance()

            println(email)
            println(password)
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                     //   Log.d(TAG, "createUserWithEmail:success")
                        println("createUserWithEmail:success")
                        val user = auth.currentUser
                     //   updateUI(user)
                    } else {
                        println("faaaaaaaaaaaaaaaaail")
                        // If sign in fails, display a message to the user.
                     //   Log.w(TAG, "createUserWithEmail:failure", task.exception)
                     //   Toast.makeText(baseContext, "Authentication failed.",
                     //       Toast.LENGTH_SHORT).show()
                     //   updateUI(null)
                    }

                    // ...
                }
        }
    }
}