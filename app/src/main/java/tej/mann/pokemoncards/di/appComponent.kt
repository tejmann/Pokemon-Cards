package tej.mann.pokemoncards.di

import tej.mann.gameroom.di.roomModule
import tej.mann.login.di.loginModule
import tej.mann.pokemon.di.viewModelModule
import tej.mann.repository.di.createNetworkModule

val BASE_URL = "https://pokeapi.co/api/v2/"

val appComponent = listOf(
    createNetworkModule(BASE_URL),
    viewModelModule,
    mainModule,
    loginModule,
    roomModule
)