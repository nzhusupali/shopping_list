package nzhusupali.project.shoppinglist


class ShopList(val id: String, val shopList: String, val rating: Int, val button: String) {
    constructor() : this(" ", " ", 0," " ) {

    }
}