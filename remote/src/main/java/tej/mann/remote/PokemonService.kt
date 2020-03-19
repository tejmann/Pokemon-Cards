package tej.mann.remote

import retrofit2.http.GET
import retrofit2.http.Path
import tej.mann.data.Pokemon

interface PokemonService {
    @GET("pokemon/{id}/")
    suspend fun getPokemons(@Path("id")id: String): Pokemon
}