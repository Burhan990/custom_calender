package nookneeds.business.nooklife.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import nookneeds.business.nooklife.R
import nookneeds.business.nooklife.databinding.ActivityLogInBinding
import nookneeds.business.nooklife.register.RegisterActivity
import nookneeds.business.nooklife.signin.SignInActivity


class LogInActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_log_in)

        setUpView()
        setUpClickListener()
    }

    private fun setUpView() {


    }

    private fun setUpClickListener() {
         binding.register.setOnClickListener {
             startActivity(Intent(this,RegisterActivity::class.java))
         }

        binding.logIn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}