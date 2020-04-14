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
import kotlin.coroutines.CoroutineContext

class RoomViewModel(
    private val auth: FirebaseAuth, private val database: FirebaseFirestore
) : ViewModel(),
    CoroutineScope {

    private val _joined: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()
    fun joined(): LiveData<Pair<Boolean, String>> = _joined

    private val _created: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()
    fun created(): LiveData<Pair<Boolean, String>> = _created

    private val _emptyRooms: MutableLiveData<List<Room>> = MutableLiveData()
    fun emptyRooms(): LiveData<List<Room>> = _emptyRooms

    var registration: ListenerRegistration? = null
    var roomRegistration: ListenerRegistration? = null
    var creatorRegistration: ListenerRegistration? = null

    private var playerId: String? = null
    private var otherPlayer: String? = null

    private var gamePath: String? = null

    fun removeRegistration() {
        registration?.remove()
        creatorRegistration?.remove()
        roomRegistration?.remove()
    }


    fun createRoom() {
        val email = auth.currentUser?.email ?: "null"
        val room = Room(email, Status.EMPTY)

        val ref = database.collection("rooms")
            .whereEqualTo("name", email)
            .get()

        ref.addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                val roomRef = database.collection("rooms").document(email)
                roomRef.set(room)
                roomRegistration = roomRef.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.d("_CALLED_", e.toString())
                    } else {
                        if (snapshot != null) {
                            val path = snapshot.data?.get("game_path")
                            if (path != null) {
                                playerId = "player_1"
                                otherPlayer = "player_2"
                                _joined.postValue(Pair(true, path.toString()))
                                gamePath = path.toString()
                            }

                        }
                    }
                }
            }
        }
    }


    fun checkAvailableRooms() {
        registration = database.collection("rooms").whereEqualTo("status", Status.EMPTY)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("_CALLED_E", e.toString())
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
        if (creator == auth.currentUser?.email) {
            return
        }
        val doc = database.collection("rooms").document(creator)
        database.runTransaction {
            it.update(doc, "joiner", auth.currentUser?.email)
        }.addOnSuccessListener {
            creatorRegistration = doc.addSnapshotListener { snapshot, e ->
                val path = snapshot?.get("game_path")
                if (path != null) {
                    _created.postValue(Pair(true, path.toString()))
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


}