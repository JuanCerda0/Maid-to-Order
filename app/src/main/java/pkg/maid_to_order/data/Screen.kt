package pkg.maid_to_order.data

sealed class Screen(val route: String) {

    object Home : Screen("home")
    object Cart : Screen("cart")
    object Form : Screen("form")
    object Settings : Screen("settings")
    object Admin : Screen("admin")
    object AdminLogin : Screen("admin_login")
    object History : Screen("history")

    class DishDetail(val dishId: Int) : Screen("detail/$dishId")

}
