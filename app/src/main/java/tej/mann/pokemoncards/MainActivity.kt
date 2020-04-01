package tej.mann.pokemoncards

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import tej.mann.login.InviteFragment
import tej.mann.login.WelcomeFragment


class MainActivity : AppCompatActivity() {

    val auth:FirebaseAuth by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        supportFragmentManager.beginTransaction().add(R.id.container, WelcomeFragment(),"").commit()
//        if (currentUser == null) supportFragmentManager.beginTransaction().add(R.id.container, WelcomeFragment(),"").commit()
//        else supportFragmentManager.beginTransaction().add(R.id.container, InviteFragment(),"").commit()
    }




}
