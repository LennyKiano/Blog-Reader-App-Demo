package com.leonkianoapps.d4a.leonard

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)   //making it full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_launcher)

        animateScreen()

    }

    private fun animateScreen() {

        launcherImageView.animate().rotation(360f).duration = 5000 //for 5seconds

        tileTextView.animate().alpha(1f).duration = 5000 //for 5 seconds


        object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                //do nothing
            }

            override fun onFinish() {

                val intent = Intent(this@LauncherActivity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)   //So that we can't come back to this activity
                startActivity(intent)
                finish()


            }
        }.start()

    }
}
