package tej.mann.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_pokemon_viewholder.view.*
import kotlinx.android.synthetic.main.layout_room_viewholder.view.*

class RecyclerViewAdapter(private val clickListener: ClickListener? = null) :
    RecyclerView.Adapter<BaseViewHolder>() {

    var data: List<Item> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == ItemType.ROOM.ordinal) RoomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_room_viewholder, parent, false), clickListener
        )
        else PokemonViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_pokemon_viewholder, parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = data[position].type.ordinal

//    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
//        with(holder.itemView){
//            room_name.text = data[position].name
//            creator.text = data[position].status.toString()
//            setOnClickListener{
//                clickListener?.onClick(room_name.text.toString())
//            }
//        }
//    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
        super.onViewRecycled(holder)
        holder.recycle()
    }
}

class RoomViewHolder(itemView: View, private val clickListener: ClickListener?) :
    BaseViewHolder(itemView) {
    override fun bind(item: Item) {
        item as Room
        with(itemView) {
            room_name.text = item.name
            creator.text = item.status.toString()
            setOnClickListener {
                clickListener?.onClick(room_name.text.toString())
            }
        }
    }

    override fun recycle() {
        with(itemView) {
            room_name.text = null
            creator.text = null
            setOnClickListener(null)
        }
    }


}

class PokemonViewHolder(itemView: View) : BaseViewHolder(itemView) {
    override fun bind(item: Item) {
        item as Pokemon
        with(itemView) {
            pokemon_name_vh.text = item.name
            Glide.with(this).load(item.sprites.frontDefault).into(pokemon_vh)
        }

    }


    override fun recycle() {
        with(itemView) {
            pokemon_name_vh.text = null
            pokemon_vh.setImageDrawable(null)
        }
    }

}

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: Item)
    abstract fun recycle()
}

open class Item(val type: ItemType)

enum class ItemType {
    ROOM,
    POKEMON
}

interface ClickListener {
    fun onClick(s: String = "")
}

