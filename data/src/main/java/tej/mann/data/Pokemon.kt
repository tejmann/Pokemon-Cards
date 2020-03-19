package tej.mann.data

import com.google.gson.annotations.SerializedName

data class Pokemon(val name: String,val stats: List<Stat>) {

}

data class Stat(@SerializedName("base_stat") val baseStat: Int, val stat:StatName) {

}

data class StatName(val name: String) {

}