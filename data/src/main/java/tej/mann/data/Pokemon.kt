package tej.mann.data

import com.google.gson.annotations.SerializedName

data class Pokemon(val name: String,val stats: List<Stat>, val sprites: Sprite) {

}

data class Sprite(@SerializedName("front_default") val frontDefault: String)

data class Stat(@SerializedName("base_stat") val baseStat: Int = 0, val stat:StatName = StatName()) {

}

data class StatName(val name: String = "") {

}