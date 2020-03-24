package tej.mann.pokemoncards

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.amplify.generated.graphql.CreatePokeMasterMutation
import com.amazonaws.amplify.generated.graphql.ListPokeMastersQuery
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import org.koin.android.ext.android.inject
import tej.mann.pokemon.PokemonFragment
import type.CreatePokeMasterInput


class MainActivity : AppCompatActivity() {

    val mAWSAppSyncClient:AWSAppSyncClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null){
            Log.d("_CALLED_","create_view_activity")
            supportFragmentManager.beginTransaction().add(R.id.container, PokemonFragment(),"").commit()
        }
        runMutation()
        runQuery()
    }

    fun runMutation(){
        val createPokeMasterInput = CreatePokeMasterInput.builder()
            .name("Tej")
            .score(0)
            .description("new_user")
            .build()
        mAWSAppSyncClient.mutate(CreatePokeMasterMutation.builder().input(createPokeMasterInput).build())
            .enqueue(mutationCallback)


    }

    private val mutationCallback =  object :GraphQLCall.Callback<CreatePokeMasterMutation.Data>(){
        override fun onFailure(e: ApolloException) {
            Log.d("_CALLED_",e.toString())
        }

        override fun onResponse(response: Response<CreatePokeMasterMutation.Data>) {
            Log.d("_CALLED_","ADDED")
        }
    }

    fun runQuery() {
        mAWSAppSyncClient.query(ListPokeMastersQuery.builder().build())
            .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
            .enqueue(queryCallback)
    }

    private val queryCallback = object : GraphQLCall.Callback<ListPokeMastersQuery.Data>() {
        override fun onFailure(e: ApolloException) {
            Log.d("_CALLED_","_FAILED_QUERY")
        }

        override fun onResponse(response: Response<ListPokeMastersQuery.Data>) {
            Log.d("_CALLED_",response.data()?.listPokeMasters()?.items().toString())
        }


    }

}
