package tej.mann.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tej.mann.data.Pokemon
import tej.mann.remote.PokemonService

interface PokemonRepository {
    suspend fun fetchPokemon(): Pokemon?
}

class PokemonRepositoryImpl(private val service: PokemonService): PokemonRepository{
    override suspend fun fetchPokemon(): Pokemon? {
        var result: Pokemon? = null
        withContext(Dispatchers.IO){
            result = service.getPokemons("25")
        }
        return result
    }
}