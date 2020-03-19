package tej.mann.pokemon.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tej.mann.pokemon.PokemonViewModel

val viewModelModule = module{
    viewModel {
        PokemonViewModel(get())
    }
}