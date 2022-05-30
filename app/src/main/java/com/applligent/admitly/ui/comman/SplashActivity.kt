package com.applligent.admitly.ui.comman

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.applligent.admitly.R
import com.applligent.admitly.ui.counselor.CounselorDashboardActivity
import com.applligent.admitly.ui.counselor.CounselorSignupActivity
import com.applligent.admitly.ui.student.StudentDashboardActivity
import com.applligent.admitly.ui.student.StudentInfoActivity
import com.applligent.admitly.utils.preferences.getAccountType
import com.applligent.admitly.utils.preferences.getLoginStatus
import com.applligent.admitly.utils.preferences.getUserType

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            //var iNext: Intent
            if (getLoginStatus()) {
                if (getUserType() == 1 && getAccountType() == 1 ){
                    startActivity(Intent(this@SplashActivity, StudentDashboardActivity::class.java))
                    finish()
                }else if (getUserType() == 1 && getAccountType() == 2){
                    startActivity(Intent(this@SplashActivity, StudentInfoActivity::class.java))
                    finish()
                }else if (getUserType() == 2 && getAccountType() == 1 ){
                    startActivity(Intent(this@SplashActivity, CounselorDashboardActivity::class.java))
                    finish()
                }else if (getUserType() == 2 && getAccountType() == 2 ){
                    startActivity(Intent(this@SplashActivity, CounselorSignupActivity::class.java))
                    finish()
                }
            } else {
                startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                finish()
            }
        }, 3000)
    }
}