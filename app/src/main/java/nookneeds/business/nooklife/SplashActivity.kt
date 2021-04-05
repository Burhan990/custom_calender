package nookneeds.business.nooklife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import nookneeds.business.nooklife.databinding.ActivitySplashBinding
import nookneeds.business.nooklife.login.LogInActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_splash)
        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.mytransition)
        binding.splash.startAnimation(animation)

        val intent = Intent(this, LogInActivity::class.java)

        val timer: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                    runOnUiThread { // lottieAnimationView.setVisibility(View.GONE);
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    startActivity(intent)
                }
            }
        }
        timer.start()
    }
}