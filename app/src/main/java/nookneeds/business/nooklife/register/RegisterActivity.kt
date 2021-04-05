package nookneeds.business.nooklife.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import nookneeds.business.nooklife.R
import nookneeds.business.nooklife.databinding.ActivityRegisterBinding
import nookneeds.business.nooklife.helper.ProgressCustomDialog
import nookneeds.business.nooklife.landing.LandingActivity
import nookneeds.business.nooklife.signin.SignInActivity
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.HashMap


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding

    private lateinit var mAuth: FirebaseAuth

    private lateinit var customDialog: ProgressCustomDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=DataBindingUtil.setContentView(this, R.layout.activity_register)

        setUpView()
        setUpListener()
    }

    private fun setUpView() {
        mAuth= FirebaseAuth.getInstance()
        customDialog= ProgressCustomDialog(this)
    }

    private fun setUpListener() {



        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.signInNow.setOnClickListener{
            startActivity(Intent(this, SignInActivity::class.java))
        }


        binding.signUp.setOnClickListener {
            customDialog.show()
            if (!validateFirstName() || !validateLastName() || !validatePhone() || !isEmailValid(
                            binding.editEmail.editText?.text.toString()
                    ) || !validatePassword()) {
                customDialog.dismiss()
                return@setOnClickListener
            }else{
                if (!validatePolicy()){
                    customDialog.dismiss()
                    Toast.makeText(this, "Please check the privacy policy", Toast.LENGTH_SHORT).show()
                }else{

                    registerUser()
                }
            }
        }
    }

    private fun registerUser() {

        Toast.makeText(this, "register user", Toast.LENGTH_SHORT).show()

        mAuth.createUserWithEmailAndPassword(binding.editEmail.editText?.text.toString(), binding.editPassword.editText?.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    customDialog.dismiss()

                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["FirstName"] = binding.editFirstName.editText?.text.toString()
                    hashMap["LastName"] = binding.editLastName.editText?.text.toString()
                    hashMap["Email"] = binding.editEmail.editText?.text.toString()
                    hashMap["PhoneNumber"] =
                        binding.ccp.selectedCountryCodeWithPlus + binding.etPhone.text.toString()
                    hashMap["password"] = binding.editPassword.editText?.text.toString()

                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(mAuth.currentUser.uid)
                        .setValue(hashMap).addOnCompleteListener { task ->
                            // progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Join In SuccessFull. Congratulations!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                startActivity(Intent(this, LandingActivity::class.java))

                            } else {
                                Toast.makeText(
                                    this,
                                    "Join In Failed. Please try again!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    customDialog.dismiss()
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG)
                        .show()
                }
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

    private fun validateFirstName(): Boolean {
        val firstnameInput: String = binding.editFirstName.editText?.text.toString().trim()
        return when {
            firstnameInput.isEmpty() -> {
                binding.editFirstName.error = "Field can't be empty"
                false
            }
            firstnameInput.length > 10 -> {
                binding.editFirstName.error = "First name is too long"
                false
            }
            else -> {
                binding.editFirstName.error = null
                true
            }
        }
    }


    private fun validateLastName(): Boolean {
        val lastNameInput: String = binding.editLastName.editText?.text.toString().trim()
        return when {
            lastNameInput.isEmpty() -> {
                binding.editLastName.error = "Field can't be empty"
                false
            }
            lastNameInput.length > 10 -> {
                binding.editLastName.error = "Last name is too long"
                false
            }
            else -> {
                binding.editLastName.error = null
                true
            }
        }
    }

    private fun validatePhone(): Boolean {
        val phoneInput: String = binding.etPhone.text.toString().trim()
        return when {
            phoneInput.isEmpty() -> {
                binding.etPhone.error = "Field can't be empty"
                false
            }
            else -> {
                binding.etPhone.error = null
                true
            }
        }
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

    private fun validatePolicy(): Boolean {
        return binding.tvPolicy.isChecked
    }
}