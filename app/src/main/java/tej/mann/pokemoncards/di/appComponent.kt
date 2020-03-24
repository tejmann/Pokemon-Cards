package tej.mann.pokemoncards.di

import tej.mann.pokemon.di.viewModelModule
import tej.mann.repository.di.createNetworkModule

val BASE_URL = "https://pokeapi.co/api/v2/"

val appComponent = listOf(
    createNetworkModule(BASE_URL),
    viewModelModule,
    mainModule
)