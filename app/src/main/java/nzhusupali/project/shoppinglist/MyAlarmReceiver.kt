package nzhusupali.project.shoppinglist

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build


class MyAlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        val channelId = "Shopping Time at 19:00"
        val channelName = "ShopTime"

        val intent = Intent(context, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(intent)
        val pendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//        val pendingIntent = PendingIntent(context, MainActivity::class.java)
//        pendingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context?.startActivity(pendingIntent)

//        if (android.R.attr.action.equalsIgnoreCase(BOOT_ACTION)) {
//            для Activity
//            val activivtyIntent = Intent(context, MainActivity::class.java)
//            activivtyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context?.startActivity(activivtyIntent)
//        }


//        val pendingIntent = PendingIntent.getActivity(
//            context?.startActivity(
//                Intent(
//                    Intent.makeRestartActivityTask(MainActivity::class.java)
//                )
//            )
//        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val imp = NotificationManager.IMPORTANCE_HIGH
            val mNotificationChannel =
                NotificationChannel(channelId, channelName, imp)
            val notificationBuilder: Notification.Builder =
                Notification.Builder(
                    context,
                    channelId
                )
                    .setSmallIcon(R.drawable.shopping96)
                    .setContentTitle(context?.getString(R.string.app_name))
                    .setContentText(context?.getString(R.string.doNotForget))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
            Notification.DEFAULT_ALL
            Notification.DEFAULT_VIBRATE
            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mNotificationChannel)
            notificationManager.notify(0, notificationBuilder.build())
        }
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}