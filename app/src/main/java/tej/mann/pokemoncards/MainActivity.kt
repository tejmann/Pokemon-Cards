package tej.mann.pokemoncards

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import tej.mann.gameroom.BackPressHandler
import tej.mann.gameroom.GameFragment
import tej.mann.gameroom.RoomFragment
import tej.mann.gameroom.WelcomeFragment


class MainActivity : AppCompatActivity(), BackPressHandler {

    private var selectedFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isNetworkConnected()) {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction().add(
                    R.id.container,
                    WelcomeFragment()
                ).commit()
            }
        } else {
            container.visibility = View.INVISIBLE
            no_internet.visibility = View.VISIBLE
        }

    }

    override fun onBackPressed() {
        when (selectedFragment) {
            is GameFragment -> {
                MaterialAlertDialogBuilder(
                    this,
                    R.style.Theme_MaterialComponents_Light_Dialog_Alert
                )
                    .setTitle(getString(R.string.exit_game))
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
            }
            is RoomFragment -> {
                MaterialAlertDialogBuilder(
                    this,
                    R.style.Theme_MaterialComponents_Light_BottomSheetDialog
                )
                    .setTitle(R.string.exit_room)
                    .setMessage(R.string.delete_room)
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                        (selectedFragment as? RoomFragment)?.deleteRoom()
                        super.onBackPressed()
                    }
                    .setNegativeButton(android.R.string.no) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun selectFragment(fragment: Fragment?) {
        selectedFragment = fragment
    }

    private fun isNetworkConnected() =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as?
                ConnectivityManager)?.activeNetworkInfo?.isConnectedOrConnecting
            ?: false

}
