package tej.mann.gameroom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import tej.mann.data.Stat

class RoomRepository(private val auth: FirebaseAuth, private val database: FirebaseFirestore) {

    private val _joined: MutableLiveData<Boolean> = MutableLiveData()
    fun joined(): LiveData<Boolean> = _joined

    private val _created: MutableLiveData<Boolean> = MutableLiveData()
    fun created(): LiveData<Boolean> = _created

    private val _emptyRooms: MutableLiveData<List<Room>> = MutableLiveData()
    fun emptyRooms(): LiveData<List<Room>> = _emptyRooms

    private val _draw: MutableLiveData<Boolean> = MutableLiveData()
    fun draw(): LiveData<Boolean> = _draw

    private var playerId: String? = null
    private var otherPlayer: String? = null
    var number: String? = null
    private val _turn: MutableLiveData<Boolean> = MutableLiveData()
    fun turn(): LiveData<Boolean> = _turn


    private var gamePath: String? = null
    private var currentStat: String? = null

    var myTurn: Boolean = false


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
                roomRef.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.d("_CALLED_", e.toString())
                    } else {
                        if (snapshot != null) {
                            val path = snapshot.data?.get("game_path")
                            if (path != null) {
                                playerId = "player_1"
                                otherPlayer = "player_2"
                                number = "0"
                                _joined.postValue(true)
                                gamePath = path.toString()
                            }

                        }
                    }
                }
            }
        }
    }


    fun createGame(creator: String) {
        if (creator == auth.currentUser?.email) {
            return
        }

        val doc = database.collection("rooms").document(creator)

        database.runTransaction {
                it.update(doc, "status", Status.IN_PLAY)
            }
            .addOnSuccessListener {
                val joiner = auth.currentUser?.email ?: ""
                val game = Game(creator, joiner, "player_1", Move.SET)
                playerId = "player_2"
                otherPlayer = "player_1"
                number = "0"
                doc.collection("game").add(
                    game
                ).addOnSuccessListener {
                    doc.update(mapOf("game_path" to it.path))
                    _created.postValue(true)
                    gamePath = it.path
                }
//                doc.collection("game").add(
//                        mapOf(
//                            "turn" to "player_1",
//                            "new" to true,
//                            "player_2" to auth.currentUser?.email,
//                            "player_1" to creator,
//                            "sc" to "set",
//                            "player_1_score" to "0",
//                            "player_2_score" to "0",
//                            "number" to "0"
//                        )
//                    )
            }
    }


    fun checkAvailableRooms() {
        database.collection("rooms").whereEqualTo("status", Status.EMPTY)
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

    fun myTurn() {
        gamePath?.let { it ->
            database.document(it).get()
                .addOnSuccessListener { snapshot ->
                    val turn = snapshot.data?.get("turn")
                    _turn.postValue(turn == playerId)
                }
        }

    }


    fun listenToGame() {
        gamePath?.let { it ->
            val doc = database.document(it)
            doc.addSnapshotListener { snapshot, e ->
                e?.let {
                    Log.d("_CALLED_", e.toString())
                    return@addSnapshotListener
                }
                snapshot?.let { s ->
                    val game = s.toObject<Game>()
                    val turn = game?.turn
                    myTurn = turn == playerId
                    val draw = game?.draw
                    _draw.postValue(draw == Draw.YES)
                }
            }
        }
    }

    fun setOrCompare(stat: Stat) {
        gamePath?.let {
            val doc = database.document(it)
            doc.get().addOnSuccessListener { snapshot ->
                snapshot.toObject<Game>()?.let { game ->
                    val move = game.move
                    if (move == Move.SET) {
                        set(doc, stat)
                        return@addOnSuccessListener
                    }

                    game.stat?.let { cStat ->
                        if (stat.stat == cStat.stat) {
                            val cValue = cStat.baseStat
                            if (stat.baseStat > cValue) {
                                val score = snapshot["${playerId}_score"].toString().toInt() + 1
                                doc.update(
                                    mapOf(
                                        "${playerId}_score" to score,
                                        "turn" to playerId,
                                        "move" to Move.SET
                                    )
                                )
                            } else {
                                val score = snapshot["${otherPlayer}_score"].toString().toInt() + 1
                                doc.update(
                                    mapOf(
                                        "${otherPlayer}_score" to score,
                                        "turn" to otherPlayer,
                                        "move" to Move.SET
                                    )
                                )
                            }
                            doc.update(mapOf("draw" to Draw.YES))
                        }
                    }
                }
            }


        }
    }

    fun set(documentReference: DocumentReference, stat: Stat) {
        documentReference.update(
            mapOf(
                "stat" to stat,
                "move" to Move.COMPARE,
                "turn" to otherPlayer,
                "draw" to Draw.NO
            )
        )
    }


    fun getName(): String {
        val user = auth.currentUser
        val name =
            (if (user?.displayName.isNullOrEmpty()) user?.email else user?.displayName)?.let {
                return it
            }
        return "Name"
    }
}