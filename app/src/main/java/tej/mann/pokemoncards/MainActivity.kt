package tej.mann.pokemoncards

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import tej.mann.login.WelcomeFragment


class MainActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null){
            Log.d("_CALLED_","create_view_activity")
            supportFragmentManager.beginTransaction().add(R.id.container, WelcomeFragment(),"").commit()
        }
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.d("_CALLED_USER_FIREBAsE", currentUser.toString())
    }




}
