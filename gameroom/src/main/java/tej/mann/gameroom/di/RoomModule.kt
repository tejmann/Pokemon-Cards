package tej.mann.gameroom.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tej.mann.gameroom.GameViewModel
import tej.mann.gameroom.RoomRepository
import tej.mann.gameroom.RoomViewModel

val roomModule = module {
    single {
        Firebase.firestore
    }
    viewModel {
        RoomViewModel(get(), get())
    }
    viewModel {
        GameViewModel(get(), get(), get())
    }
    single {
        RoomRepository(get(), get())
    }
    single {
        GameRepository(get(), get())
    }

}