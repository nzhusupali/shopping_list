package nzhusupali.project.shoppinglist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class ShopListAdapter(
    private val mCtx: Context,
    private val layoutResId: Int,
    private val shoppingList: List<ShopList>,
) : ArrayAdapter<ShopList>(mCtx, layoutResId, shoppingList) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewNameProducts = view.findViewById<TextView>(R.id.textViewName)
        val textViewUpdate = view.findViewById<TextView>(R.id.textViewUpdate)

        val shops = shoppingList[position]

        textViewNameProducts.text = shops.shopList

        textViewUpdate.setOnClickListener {
            showUpdateDialog(shops)
        }

        return view

    }

    private fun showUpdateDialog(shops: ShopList) {
        val builder = AlertDialog.Builder(mCtx)

        builder.setTitle(context.getString(R.string.Update_shopping_list))
        builder.setIcon(R.drawable.shopping96)

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.layout_update_shop, null)

        val editText = view.findViewById<EditText>(R.id.ET_shoppingList_l)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar_l)

        editText.setText(shops.shopList)
        ratingBar.rating = shops.rating.toFloat()

        builder.setView(view)

        builder.setPositiveButton(R.string.update, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val dbProducts = FirebaseDatabase.getInstance().getReference("Shop list")
                val products = editText.text.toString().trim()
                if (products.isEmpty()) {
                    editText.error = R.string.enterShopList.toString()
                    editText.requestFocus()
                    return
                }

                val productsListUpdate =
                    ShopList(shops.id, products, ratingBar.rating.toInt())

                dbProducts.child(shops.id).setValue(productsListUpdate)
                Toast.makeText(mCtx, R.string.updated, Toast.LENGTH_SHORT).show()
            }
        })
        builder.setNegativeButton(R.string.No) { _, _ -> Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP }
        builder.setNeutralButton(R.string.delete) { _, _ ->
            val dbProducts = FirebaseDatabase.getInstance().getReference("Shop list")
            dbProducts.child(shops.id).removeValue()
            Toast.makeText(mCtx, R.string.deleted, Toast.LENGTH_SHORT).show()

        }

        val alert = builder.create()
        alert.show()

    }
}
