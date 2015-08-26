package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.parse.PLog;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Random;

public class FriendRequestReceiver extends ParsePushBroadcastReceiver {

    private Bitmap mBitmap;
    private Context mContext;


    private JSONObject getPushData(Intent intent) {
        try {
            return new JSONObject(intent.getStringExtra("com.parse.Data"));
        } catch (JSONException var3) {
            return null;
        }
    }

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        JSONObject pushData = this.getPushData(intent);
        if(pushData != null && (pushData.has("alert") || pushData.has("title"))) {
            String title = pushData.optString("title", "Friend Request");
            String alert = pushData.optString("alert", "Notification received.");
            String tickerText = String.format(Locale.getDefault(), "%s: %s", new Object[]{title, alert});
            Bundle extras = intent.getExtras();
            Random random = new Random();
            int contentIntentRequestCode = random.nextInt();
            int deleteIntentRequestCode = random.nextInt();
            String packageName = context.getPackageName();
            Intent contentIntent = new Intent("com.parse.push.intent.OPEN");
            contentIntent.putExtras(extras);
            contentIntent.setPackage(packageName);
            Intent deleteIntent = new Intent("com.parse.push.intent.DELETE");
            deleteIntent.putExtras(extras);
            deleteIntent.setPackage(packageName);
            PendingIntent pContentIntent = PendingIntent.getBroadcast(context, contentIntentRequestCode, contentIntent, 134217728);
            PendingIntent pDeleteIntent = PendingIntent.getBroadcast(context, deleteIntentRequestCode, deleteIntent, 134217728);
            NotificationCompat.Builder parseBuilder = new NotificationCompat.Builder(context);
            parseBuilder.setContentTitle(title).setContentText(alert).setTicker(tickerText).setSmallIcon(this.getSmallIconId(context, intent)).setLargeIcon(this.getLargeIcon(context, intent)).setContentIntent(pContentIntent).setDeleteIntent(pDeleteIntent).setAutoCancel(true).setDefaults(-1);
            if(alert != null && alert.length() > 38) {
                parseBuilder.setStyle((new NotificationCompat.Builder.BigTextStyle()).bigText(alert));
            }

            return parseBuilder.build();
        } else {
            return null;
        }
    }
}
