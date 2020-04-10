package tej.mann.gameroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_room_viewholder.view.*

class RecyclerViewAdapter(private val clickListener: ClickListener? = null) : RecyclerView.Adapter<RoomViewHolder>(){

    var data: List<Room> = emptyList()
        set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_room_viewholder, parent, false)
        return RoomViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        with(holder.itemView){
            room_name.text = data[position].name
            creator.text = data[position].status.toString()
            setOnClickListener{
                clickListener?.onClick(room_name.text.toString())
            }
        }
    }
}

class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}

interface ClickListener{
    abstract fun onClick(s: String = "")
}

