package tej.mann.pokemoncards

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tej.mann.pokemoncards.di.appComponent

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        configureDi()
    }

    private fun configureDi(){
        startKoin {
            androidContext(this@App)
            modules(appComponent)
        }
    }
}