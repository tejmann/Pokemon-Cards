package tej.mann.gameroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.layout_collection.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tej.mann.data.RecyclerViewAdapter

class CollectionFragment : Fragment() {

    private val recyclerViewAdapter = RecyclerViewAdapter()
    private val viewModel: CollectionViewModel by viewModel()

    companion object {
        private const val GRID_SPAN_COUNT = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collection_recycler_view.apply {
            adapter = recyclerViewAdapter
            layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)
        }

        viewModel.pokemon().observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.data = it
        })

    }

    override fun onStart() {
        super.onStart()
        viewModel.getPokemon()

    }
}

