package nzhusupali.project.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import nzhusupali.project.shoppinglist.databinding.LoginActivityBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var _binding: LoginActivityBinding
    private val mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.BTNLogIn.setOnClickListener {
            val enterEmail: String = getString(R.string.enterEmail)
            val enterPassword: String = getString(R.string.enterPassword)
            val vaildEmailPassword: String = getString(R.string.validEmailPassword)

            when {
                TextUtils.isEmpty(_binding.ETLogIn.text.toString().trim() { it <= ' ' }) -> {
                    _binding.ETLogIn.error = enterEmail
                    _binding.ETLogIn.requestFocus()
                    return@setOnClickListener
                }
                TextUtils.isEmpty(_binding.ETPassword.text.toString().trim() { it <= ' ' }) -> {
                    _binding.ETPassword.error = enterPassword
                    _binding.ETPassword.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    val email: String = _binding.ETLogIn.text.toString().trim { it <= ' ' }
                    val password: String = _binding.ETPassword.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    getString(R.string.successfulAuth),
                                    Toast.LENGTH_LONG
                                ).show()
                                if (task.isSuccessful) {
                                    val runIntent = Intent(this, MainActivity::class.java)
                                    runIntent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(runIntent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                _binding.ETLogIn.error = vaildEmailPassword
                                _binding.ETLogIn.requestFocus()
                                return@addOnCompleteListener
                            }
                        }
                }
            }
        }
    }
}