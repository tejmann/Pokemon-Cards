package tej.mann.data

import com.google.gson.annotations.SerializedName

data class Pokemon(val name: String,val stats: List<Stat>) {

}

data class Stat(@SerializedName("base_stat") val baseStat: Int = 0, val stat:StatName = StatName()) {

}

data class StatName(val name: String = "") {

}