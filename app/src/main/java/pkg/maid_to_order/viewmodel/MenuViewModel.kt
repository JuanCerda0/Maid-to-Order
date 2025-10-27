package pkg.maid_to_order.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import pkg.maid_to_order.data.model.Dish
import pkg.maid_to_order.R

class MenuViewModel : ViewModel() {

    private val _menuList = mutableStateListOf<Dish>()
    val menuList: SnapshotStateList<Dish> get() = _menuList

    init {
        _menuList.addAll(
            listOf(
                Dish(1, "Katsudon", "Cerdo frito con arroz y huevo", 6.990, R.drawable.katsudon),
                Dish(2, "Ramen", "Sopa japonesa con fideos y cerdo", 7.490, R.drawable.ramen),
                Dish(3, "Onigiri", "Bola de arroz rellena con salmón", 3.990, R.drawable.onigiri),
                Dish(4, "Curry Japonés", "Arroz con curry suave y carne", 8.490, R.drawable.curry)
            )
        )
    }

    fun loadMenu(): List<Dish> = _menuList

    fun getDishById(id: Int): Dish? = _menuList.find { it.id == id }
}
