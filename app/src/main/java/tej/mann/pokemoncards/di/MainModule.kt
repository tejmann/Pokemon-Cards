package tej.mann.pokemoncards.di

import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainModule = module {
    factory {
        AWSConfiguration(androidContext())
    }
    factory {
        AWSAppSyncClient.builder()
            .context(androidContext())
            .awsConfiguration(get())
            .build()
    }
}