package tej.mann.repository.di

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import tej.mann.remote.PokemonService
import tej.mann.repository.PokemonRepository
import tej.mann.repository.PokemonRepositoryImpl

fun createNetworkModule(baseUrl: String) = module {
    factory {
        OkHttpClient.Builder().build()
    }
    factory<Converter.Factory> {
        GsonConverterFactory.create()
    }
    single {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(get())
            .addConverterFactory(get())
            .build()
    }

    factory<PokemonService> {
        get<Retrofit>().create()
    }

    single<PokemonRepository>{
        PokemonRepositoryImpl(get())
    }
}