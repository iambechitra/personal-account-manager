package com.example.bechitra.walleto;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlertManager extends BroadcastReceiver{
    DatabaseHelper db;
    @Override
    public void onReceive(Context context, Intent intent) {
        db = new DatabaseHelper(context);
        String percentage = db.getOnAlertPercentage();
        if(!percentage.equals("0") && percentage != null) {
            String ratio = db.getBalanceToEarningRatio();
            if(Double.parseDouble(ratio) <= Double.parseDouble(percentage))
                notificationCreator(context, ratio);
        }
    }

    public void notificationCreator(Context context, String ratio) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeat = new Intent(context, MainActivity.class);
        repeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 100, repeat, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, null)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_import_contacts_black_24dp)
                .setContentTitle("Excess Spending Alert")
                .setContentText("Your Spending is very high and balance is "+ratio+"% remaining")
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = "my_channel_01";
            CharSequence name = "Char Seq";
            String description = "Hello World";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            mChannel.setDescription(description);

            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);

            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(mChannel);
            builder.setChannelId(id);
        }

        notificationManager.notify(100, builder.build());
    }
}
