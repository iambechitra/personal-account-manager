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

import com.example.bechitra.walleto.table.Schedule;
import com.example.bechitra.walleto.table.TableData;

import java.text.ParseException;
import java.util.List;
import java.util.StringTokenizer;

public class AlertManager extends BroadcastReceiver{
    DatabaseHelper db;
    final String COLUMN_NAME = "TIME";
    final String TABLE = "SCHEDULE";

    @Override
    public void onReceive(Context context, Intent intent) {
        db = new DatabaseHelper(context);
        List<Schedule> schedules = db.getScheduledData();
        StringPatternCreator spc = new StringPatternCreator();
        String currentDate = spc.getCurrentDate();
        boolean flag = false;
        int counter = 0;

        for(Schedule s : schedules) {
            try {
                String next = formatDate(spc.addDate(s.getTime(), Integer.parseInt(s.getRepeat())));
                //long diff = spc.dateDifference(currentDate, next);
                if(next.equals(currentDate)) {
                    TableData data = db.getDataFromRow(s.getItemID(), s.getTableName());
                    if(data!=null) {
                        db.insertOnTable(s.getTableName(), data);
                        db.updateRowFromTable(TABLE, s.getID(), COLUMN_NAME, next);
                        flag = true;
                        counter++;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(flag)
                notificationCreator(context, counter);
        }
    }

    private String formatDate(String date) {
        StringTokenizer stk = new StringTokenizer(date, "/");
        int[] d = new int[3];
        int i = 0;
        while (stk.hasMoreTokens())
            d[i++] = Integer.parseInt(stk.nextToken());

        return (Integer.toString(d[0])+"/"+Integer.toString(d[1])+"/"+Integer.toString(d[2]));

    }

    private void notificationCreator(Context context, int counter) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeat = new Intent(context, MainActivity.class);
        repeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 100, repeat, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, null)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_add_spending_black_24dp)
                .setContentTitle("Automatic Data insertion")
                .setContentText(counter+" new record Added to the database")
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
