package tej.mann.pokemoncards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import tej.mann.pokemon.PokemonFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null){
            Log.d("_CALLED_","create_view_activity")
            supportFragmentManager.beginTransaction().add(R.id.container, PokemonFragment(),"").commit()
        }
    }
}
