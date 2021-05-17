package nzhusupali.project.shoppinglist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase


class ShopListAdapter(
    private val mctx: Context,
    private val layoutresId: Int,
    private val shoppingList: List<ShopList>,
) : ArrayAdapter<ShopList>(mctx, layoutresId, shoppingList) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mctx)
        val view: View = layoutInflater.inflate(layoutresId, null)

        val textViewNameProducts = view.findViewById<TextView>(R.id.textViewName)
        val textViewUpdate = view.findViewById<TextView>(R.id.textViewUpdate)

        val shops = shoppingList[position]

        textViewNameProducts.text = shops.shop_List

        textViewUpdate.setOnClickListener {
            showUpdateDialog(shops)
        }

        return view

    }

    private fun showUpdateDialog(shops: ShopList) {
        val builder = AlertDialog.Builder(mctx)

        builder.setTitle(context.getString(R.string.Update_shopping_list))
        val inflater = LayoutInflater.from(mctx)

        val view = inflater.inflate(R.layout.layout_update_shop, null)

        val editText = view.findViewById<EditText>(R.id.ET_shoppingList_l)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar_l)

        editText.setText(shops.shop_List)
        ratingBar.rating = shops.rating.toFloat()

        builder.setView(view)
        builder.setPositiveButton(R.string.update) { _, _ ->
            val dbProducts = FirebaseDatabase.getInstance().getReference("Shop list")
            val products = editText.text.toString().trim()
            if (products.isEmpty()) {
                editText.error = R.string.enterShopList.toString()
                editText.requestFocus()
                return@setPositiveButton
            }

            val productsListUpdate = ShopList(shops.id, shop_List = String(), ratingBar.rating.toInt())

            dbProducts.child(productsListUpdate.id).setValue(productsListUpdate)
            Toast.makeText(mctx, R.string.updated, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton(R.string.No) { _, _ ->
            Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
        }
        val alert = builder.create()
        alert.show()

    }
}
