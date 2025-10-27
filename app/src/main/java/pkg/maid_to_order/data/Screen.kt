package pkg.maid_to_order.data

sealed class Screen(val route: String) {

    object Home : Screen("home")
    object Cart : Screen("cart")
    object Form : Screen("form")

    class DishDetail(val dishId: Int) : Screen("detail/$dishId")

}