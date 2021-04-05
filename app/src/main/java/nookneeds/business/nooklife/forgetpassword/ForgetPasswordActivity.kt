package nookneeds.business.nooklife.forgetpassword

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
import nookneeds.business.nooklife.databinding.ActivityForgetPasswordBinding
import nookneeds.business.nooklife.helper.ProgressCustomDialog
import java.util.regex.Matcher
import java.util.regex.Pattern


class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityForgetPasswordBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var customDialog: ProgressCustomDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_forget_password)
        setUpView()
        setUpListener()

    }

    private fun setUpListener() {

        binding.imageBack.setOnClickListener {
            finish()
        }
        binding.signIn.setOnClickListener {
            finish()
        }


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
                if (s.isNotEmpty()){
                    ViewCompat.setBackgroundTintList(
                        binding.logIn,
                        ColorStateList.valueOf(ContextCompat.getColor(this@ForgetPasswordActivity,R.color.black)))
                    binding.logIn.setTextColor(ContextCompat.getColor(this@ForgetPasswordActivity,R.color.white))
                }else{
                    ViewCompat.setBackgroundTintList(
                        binding.logIn,
                        ColorStateList.valueOf(ContextCompat.getColor(this@ForgetPasswordActivity,R.color.signInColor)))
                    binding.logIn.setTextColor(ContextCompat.getColor(this@ForgetPasswordActivity,R.color.black))
                }
            }
        })
        binding.logIn.setOnClickListener {
            customDialog.show()

            if (!isEmailValid(
                    binding.editEmail.editText?.text.toString()
                )) {
                customDialog.dismiss()
                return@setOnClickListener
            }else {

                mAuth.sendPasswordResetEmail(binding.editEmail.editText?.text.toString()).addOnCompleteListener { task->
                    customDialog.dismiss()
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Password was Send to your Email Address",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.logIn.isEnabled=false
                        ViewCompat.setBackgroundTintList(
                            binding.logIn,
                            ColorStateList.valueOf(ContextCompat.getColor(this@ForgetPasswordActivity,R.color.signInColor)))
                        binding.logIn.setTextColor(ContextCompat.getColor(this@ForgetPasswordActivity,R.color.black))
                    } else {
                        Toast.makeText(
                            this,
                            "" + task.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }
        }


        }



    private fun setUpView() {
        mAuth= FirebaseAuth.getInstance()
        customDialog= ProgressCustomDialog(this)
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
}

