package nzhusupali.project.shoppinglist

import android.widget.EditText

class ShopList(val id: String, val shopList : String, val rating: Int) {
    constructor() : this(" "," ", 0) {

    }
}