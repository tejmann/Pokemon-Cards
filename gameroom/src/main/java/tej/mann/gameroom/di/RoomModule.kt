package tej.mann.gameroom.di

import tej.mann.gameroom.GameEndViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tej.mann.gameroom.LoginViewModel
import tej.mann.gameroom.CollectionViewModel
import tej.mann.gameroom.GameViewModel
import tej.mann.gameroom.RoomViewModel


val roomModule = module {
    viewModel {
        RoomViewModel(get(), get())
    }
    viewModel {
        GameViewModel(get(), get(), get())
    }
    viewModel {
        CollectionViewModel(get(), get())
    }
    viewModel {
        GameEndViewModel(get(), get(), get())
    }
}

val loginModule = module {
    viewModel {
        LoginViewModel(get(), get(), get())
    }
}
