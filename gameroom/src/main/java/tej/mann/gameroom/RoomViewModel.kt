package tej.mann.gameroom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import tej.mann.data.Path
import tej.mann.data.Room
import tej.mann.data.Status
import kotlin.coroutines.CoroutineContext

class RoomViewModel(
    auth: FirebaseAuth, private val database: FirebaseFirestore
) : ViewModel(),
    CoroutineScope {

    companion object {
        const val TAG = "room_view_model"
        const val FIELD_NAME = "name"
        const val FIELD_STATUS = "status"
        const val FIELD_JOINER = "joiner"
    }

    private val email = auth.currentUser?.email

    private val _joined: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()
    fun joined(): LiveData<Pair<Boolean, String>> = _joined

    private val _created: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()
    fun created(): LiveData<Pair<Boolean, String>> = _created

    private val _emptyRooms: MutableLiveData<List<Room>> = MutableLiveData()
    fun emptyRooms(): LiveData<List<Room>> = _emptyRooms

    var registration: ListenerRegistration? = null
    var roomRegistration: ListenerRegistration? = null
    var creatorRegistration: ListenerRegistration? = null

    private var gamePath: String? = null

    fun removeRegistration() {
        registration?.remove()
        creatorRegistration?.remove()
        roomRegistration?.remove()
    }


    fun createRoom() {
        email?.let {
            val room = Room(
                it,
                Status.EMPTY
            )

            val ref = database.collection(Path.COLLECTION_PATH_ROOMS)
                .whereEqualTo(FIELD_NAME, email)
                .get()

            ref.addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    val roomRef = database.collection(Path.COLLECTION_PATH_ROOMS).document(it)
                    roomRef.set(room)
                    roomRegistration = roomRef.addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.d(TAG, e.toString())
                        } else {
                            if (snapshot != null) {
                                val path = snapshot.data?.get(Path.DOCUMENT_PATH_GAME)
                                if (path != null) {
                                    _joined.postValue(Pair(true, path.toString()))
                                    gamePath = path.toString()
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    fun checkAvailableRooms() {
        registration =
            database.collection(Path.COLLECTION_PATH_ROOMS).whereEqualTo(FIELD_STATUS, Status.EMPTY)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.d(TAG, e.toString())
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val rooms = mutableListOf<Room>()
                        for (doc in snapshot) {
                            val room = doc.toObject<Room>()
                            rooms.add(room)
                        }
                        _emptyRooms.postValue(rooms)
                    }

                }
    }

    fun joinRoom(creator: String) {
        email?.let {
            if (creator == it) {
                return
            }
            val doc = database.collection(Path.COLLECTION_PATH_ROOMS).document(creator)
            database.runTransaction { transaction ->
                transaction.update(doc, FIELD_JOINER, it)
            }.addOnSuccessListener {
                creatorRegistration = doc.addSnapshotListener { snapshot, _ ->
                    val path = snapshot?.get(Path.DOCUMENT_PATH_GAME)

                    if (path != null) {
                        _created.postValue(Pair(true, path.toString()))
                    }
                }
            }

        }
    }

    fun deleteRoom() {
        email?.let {
            val doc = database.collection(Path.COLLECTION_PATH_ROOMS).document(it)
            database.runTransaction { transaction ->
                transaction.update(doc, FIELD_STATUS, Status.DELETE)
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}
