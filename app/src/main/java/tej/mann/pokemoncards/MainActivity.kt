package tej.mann.pokemoncards

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import tej.mann.gameroom.BackPressHandler
import tej.mann.gameroom.GameFragment
import tej.mann.login.WelcomeFragment


class MainActivity : AppCompatActivity(), BackPressHandler {

    private var selectedFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        savedInstanceState?.let {
//            supportFragmentManager.getFragment(savedInstanceState, "fragment")
//            return
//        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                    R.id.container,
                    WelcomeFragment()
                )
                .commit()
        }
    }

    override fun onBackPressed() {
        if (selectedFragment is GameFragment) {
            MaterialAlertDialogBuilder(this, R.style.Theme_MaterialComponents_Dialog_Alert)
                .setTitle("Do you want to exit the game?")
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.container,
                            WelcomeFragment()
                        ).commit()
                }
                .setNegativeButton(android.R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            super.onBackPressed()
        }
    }

//    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
//        super.onSaveInstanceState(outState, outPersistentState)
//        supportFragmentManager.putFragment(outState, "fragment", GameFragment())
//    }

    override fun selectFragment(fragment: Fragment?) {
        selectedFragment = fragment
    }


}
