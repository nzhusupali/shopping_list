package nzhusupali.project.shoppinglist

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import nzhusupali.project.shoppinglist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var ET_ShopList: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var buttonSend: Button
    private lateinit var listView : ListView

    private lateinit var shoppingList: MutableList<ShopList>
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        shoppingList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("Shop list")

        ET_ShopList = _binding.ETShoppingList
        ratingBar = _binding.ratingBar
        buttonSend = _binding.BTNSend
        listView = _binding.listView

        buttonSend.setOnClickListener {
            shopListSend()
        }
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (h in snapshot.children) {
                        val shopPingList = h.getValue(ShopList::class.java)
                        shoppingList.add(shopPingList!!)
                    }
                    val adapter = ShopListAdapter(this@MainActivity, R.layout.shopping_list,shoppingList )
                    listView.adapter = adapter

                }
            }

        })
    }

    private fun shopListSend() {
        val list = ET_ShopList.text.toString().trim()
        if (list.isEmpty()) {
            ET_ShopList.error = getString(R.string.writeShopList)
            return
        }

        val shopId = ref.push().key

        val shops = shopId?.let { ShopList(it, list, ratingBar.rating.toInt()) }
        shopId?.let {
            ref.child(it).setValue(shops).addOnCompleteListener {
                Toast.makeText(this, getString(R.string.succss_saved), Toast.LENGTH_LONG).show()

            }
        }


    }
}