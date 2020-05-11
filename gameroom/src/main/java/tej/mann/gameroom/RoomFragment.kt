package tej.mann.gameroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.layout_room.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import tej.mann.common.views.showToast
import tej.mann.data.ClickListener
import tej.mann.data.RecyclerViewAdapter

class RoomFragment : Fragment(), ClickListener {

    val db: FirebaseFirestore by inject()
    val auth: FirebaseAuth by inject()
    private val recyclerViewAdapter = RecyclerViewAdapter(this)
    private val viewModel: RoomViewModel by viewModel()

    private var backPressHandler: BackPressHandler? = null

    companion object {
        const val TAG = "room"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        backPressHandler = activity as? BackPressHandler
        return inflater.inflate(R.layout.layout_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        create_room.setOnClickListener {
            viewModel.createRoom()
            showToast(getString(R.string.wait_room))
        }

        cap.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.animation_enter, R.anim.animation_exit)
                .addToBackStack(TAG)
                .replace(R.id.container, CollectionFragment()).commit()
        }

        viewModel.joined()
            .observe(viewLifecycleOwner, Observer {
                if (it.first) {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, GameFragment.newInstance(it.second)).commit()
                }
            })

        viewModel.emptyRooms().observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.data = it
        })


        gameroom_recycler_view.apply {
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.created().observe(viewLifecycleOwner, Observer {
            if (it.first) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, GameFragment.newInstance(it.second)).commit()
            }
        })

    }

    override fun onStart() {
        super.onStart()
        backPressHandler?.selectFragment(this)
        viewModel.checkAvailableRooms()
    }


    override fun onClick(s: String) {
        viewModel.joinRoom(s)
    }

    override fun onStop() {
        super.onStop()
        backPressHandler?.selectFragment(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeRegistration()
    }

    fun deleteRoom() {
        viewModel.deleteRoom()
    }

}
