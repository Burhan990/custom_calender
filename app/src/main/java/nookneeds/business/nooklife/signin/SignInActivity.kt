package nookneeds.business.nooklife.signin

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import nookneeds.business.nooklife.R
import nookneeds.business.nooklife.databinding.ActivitySignInBinding
import nookneeds.business.nooklife.forgetpassword.ForgetPasswordActivity
import nookneeds.business.nooklife.helper.ProgressCustomDialog
import nookneeds.business.nooklife.landing.LandingActivity
import nookneeds.business.nooklife.register.RegisterActivity
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignInActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySignInBinding
    private lateinit var mAuth:FirebaseAuth
    private lateinit var customDialog: ProgressCustomDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        setUpView()
        setUpListener()
    }

    private fun setUpView() {
        mAuth= FirebaseAuth.getInstance()
        customDialog= ProgressCustomDialog(this)
    }

    private fun setUpListener() {


        binding.editEmail.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty() && binding.editPassword.editText?.text.toString().isNotEmpty()){
                    ViewCompat.setBackgroundTintList(
                        binding.logIn,
                        ColorStateList.valueOf(ContextCompat.getColor(this@SignInActivity,R.color.black)))
                    binding.logIn.setTextColor(ContextCompat.getColor(this@SignInActivity,R.color.white))
                }else{
                    ViewCompat.setBackgroundTintList(
                        binding.logIn,
                        ColorStateList.valueOf(ContextCompat.getColor(this@SignInActivity,R.color.signInColor)))
                    binding.logIn.setTextColor(ContextCompat.getColor(this@SignInActivity,R.color.black))
                }
            }
        })

        binding.editPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty() && binding.editEmail.editText?.text.toString().isNotEmpty()){
                    ViewCompat.setBackgroundTintList(
                        binding.logIn,
                        ColorStateList.valueOf(resources.getColor(R.color.black)))
                    binding.logIn.setTextColor(resources.getColor(R.color.white))
                }else{
                    ViewCompat.setBackgroundTintList(
                        binding.logIn,
                        ColorStateList.valueOf(resources.getColor(R.color.signInColor)))
                    binding.logIn.setTextColor(resources.getColor(R.color.black))
                }
            }
        })


        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.signUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.forgetPassword.setOnClickListener{
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        binding.logIn.setOnClickListener {
            customDialog.show()
            if (!isEmailValid(
                    binding.editEmail.editText?.text.toString()
                ) ||!validatePassword()){
                customDialog.dismiss()
                return@setOnClickListener
            }else{
                signIn()
            }
        }
    }

    private fun signIn() {
        binding.editEmail.error=null
        binding.editPassword.error=null

        mAuth.signInWithEmailAndPassword(binding.editEmail.editText?.text.toString(), binding.editPassword.editText?.text.toString())
            .addOnCompleteListener(this) { task ->
                customDialog.dismiss()
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information

                 startActivity(Intent(this,LandingActivity::class.java))
                    Toast.makeText(
                       this,
                        "Welcome To Nook life. Sign In Success",
                        Toast.LENGTH_SHORT
                    )
                        .show()


                    //FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);


                    //HomeRestaurant.fragmentManager.beginTransaction().replace(R.id.container,,null).addToBackStack(null).commit();
                }
            }.addOnFailureListener { e->
                customDialog.dismiss()
                Toast.makeText(
                    this,
                    "Sign In Failed. "+e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun isEmailValid(email: String): Boolean {
        val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
        val emaill = email.trim { it <= ' ' }
        val inputStr: CharSequence = emaill
        val pattern: Pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(inputStr)
        if (matcher.matches()) {
            binding.editEmail.error=null
            return true
        }
        else binding.editEmail.error = "Please Enter Valid Email!"
        return false
    }
    private fun validatePassword(): Boolean {
        val passwordInput: String = binding.editPassword.editText?.text.toString().trim()
        return when {
            passwordInput.isEmpty() -> {
                binding.editPassword.error = "Field can't be empty"
                false
            }
            passwordInput.length <= 6 -> {
                binding.editPassword.error = "Password has minimum 7 digit"
                false
            }
            else -> {
                binding.editPassword.error = null
                true
            }
        }
    }

}