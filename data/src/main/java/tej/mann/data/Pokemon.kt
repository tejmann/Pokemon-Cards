package tej.mann.data


import com.google.gson.annotations.SerializedName

data class Pokemon(
    val name: String = "",
    val stats: List<Stat> = emptyList(), val sprites: Sprite = Sprite()
) : Item(ItemType.POKEMON)

data class Sprite(@SerializedName("front_default") val frontDefault: String = "frontDefault")

data class Stat(@SerializedName("base_stat") val baseStat: Int = 0, val stat: StatName = StatName())

data class StatName(val name: String = "")

data class PokemonList(val pokemonList: List<Pokemon> = emptyList())
