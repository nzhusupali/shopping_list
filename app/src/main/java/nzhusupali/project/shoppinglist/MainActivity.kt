package nzhusupali.project.shoppinglist

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.tomlonghurst.expandablehinttext.ExpandableHintText
import nzhusupali.project.shoppinglist.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var etShopList: ExpandableHintText
    private lateinit var ratingBar: RatingBar
    private lateinit var buttonSend: Button
    private lateinit var listView: ListView
    private lateinit var button : Button

    private lateinit var shoppingList: MutableList<ShopList>
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        scheduleAlarm()
        scheduleAlarm2()

        shoppingList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("Shop list")

        etShopList = _binding.ETShoppingList
        ratingBar = _binding.ratingBar
        buttonSend = _binding.BTNSend
        listView = _binding.listView

        buttonSend.setOnClickListener {
            shopListSend()
        }
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    shoppingList.clear()
                    for (h in snapshot.children) {
                        val shopPingList = h.getValue(ShopList::class.java)
                        shoppingList.add(shopPingList!!)
                    }
                    val adapter =
                        ShopListAdapter(this@MainActivity, R.layout.shopping_list, shoppingList)
                    listView.adapter = adapter

                }
            }
        })


    }

    //    Здесь устанавливаем время уведомления. Уведомление настраивается в MyAlarmReceiver
    private fun scheduleAlarm() {
        val id = 1
//        val time = GregorianCalendar().timeInMillis
        val intentAlarm = Intent(this, MyAlarmReceiver::class.java)
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 19)
        calendar.set(Calendar.MINUTE, 0)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            PendingIntent.getBroadcast(this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT)
        )

    }

    private fun scheduleAlarm2() {
        val id = 2
        val intentAlarm = Intent(this, MyAlarmReceiver::class.java)
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 18)
        calendar.set(Calendar.MINUTE, 0)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            PendingIntent.getBroadcast(this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT)
        )
    }

    private fun shopListSend() {
        val list = etShopList.text.toString().trim()

        val shopId = ref.push().key

        val shops = shopId?.let { ShopList(it, list, ratingBar.rating.toInt(), " " ) }
        shopId?.let {
            ref.child(it).setValue(shops).addOnCompleteListener {
                Toast.makeText(this, getString(R.string.succss_saved), Toast.LENGTH_LONG).show()

            }
        }
    }


}