package tej.mann.gameroom.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tej.mann.gameroom.*

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
        EndGameViewModel(get(), get(), get())
    }
}

val loginModule = module {
    viewModel {
        LoginViewModel(get(), get(), get())
    }
}
