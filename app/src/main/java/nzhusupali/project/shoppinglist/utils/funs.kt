package nzhusupali.project.shoppinglist.utils

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.showToast(message : String) {
    Toast.makeText(this.context,message,Toast.LENGTH_SHORT).show()
}
fun AppCompatActivity.toast(message: String) {
    Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
}