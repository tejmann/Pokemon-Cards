package tej.mann.gameroom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.layout_room.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RoomFragment : Fragment(), ClickListener {

    val db: FirebaseFirestore by inject()
    val auth: FirebaseAuth by inject()
    val recyclerViewAdapter = RecyclerViewAdapter(this)
    val viewModel: RoomViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        create_room.setOnClickListener {
            viewModel.createRoom()
        }

        viewModel.joined().observe(viewLifecycleOwner, Observer {
            if(it) {
                parentFragmentManager.beginTransaction().replace(R.id.container, GameFragment()).commit()
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
            if (it) {
                parentFragmentManager.beginTransaction().replace(R.id.container, GameFragment()).commit()
            }
        })

    }

    override fun onStart() {
        super.onStart()
        viewModel.checkAvailableRooms()
    }

    private fun showToast(messgae: String) {
        Toast.makeText(context, messgae, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(s: String) {
        viewModel.createGame(s)
        showToast("clicked")
    }


}