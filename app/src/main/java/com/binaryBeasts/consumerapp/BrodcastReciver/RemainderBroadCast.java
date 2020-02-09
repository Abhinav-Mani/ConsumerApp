package com.binaryBeasts.consumerapp.BrodcastReciver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.binaryBeasts.consumerapp.MainActivity;
import com.binaryBeasts.consumerapp.R;


public class RemainderBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ak47","createNot c");
        Intent intent1=new Intent(context, MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(
                context,
                0,
                intent1,
                0
        );
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"notifactionBrtoadcast")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Grocery List")
                .setContentText("Yor Scheduled your Grocery List")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200,builder.build());
    }
}
