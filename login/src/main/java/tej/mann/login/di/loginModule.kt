package tej.mann.login.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tej.mann.login.LoginViewModel

val loginModule = module {
    viewModel {
        LoginViewModel(get(), get(), get())
    }
}
