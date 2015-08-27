package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FriendRequestReceiver extends ParsePushBroadcastReceiver {


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
            PendingIntent pContentIntent = PendingIntent.getBroadcast(context, contentIntentRequestCode, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pDeleteIntent = PendingIntent.getBroadcast(context, deleteIntentRequestCode, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder parseBuilder = new NotificationCompat.Builder(context);
            parseBuilder.setContentTitle(title).setContentText(alert).setTicker(tickerText).setSmallIcon(this.getSmallIconId(context, intent)).setLargeIcon(this.getLargeIcon(context, intent)).setContentIntent(pContentIntent).setDeleteIntent(pDeleteIntent).setAutoCancel(true).setDefaults(-1);
            if(alert != null && alert.length() > 38) {
                parseBuilder.setStyle((new android.support.v4.app.NotificationCompat.BigTextStyle()).bigText(alert));
            }

            if (pushData.has(Constants.EXTRA_KEY_OBJECT_ID)) {
                Intent acceptIntent = new Intent(context, ProfileActivity.class);
                String objectId = pushData.optString(Constants.EXTRA_KEY_OBJECT_ID, "");
                acceptIntent.putExtra(Constants.EXTRA_KEY_OBJECT_ID, objectId);
                PendingIntent accept = PendingIntent.getActivity(context, Constants.REQUEST_CODE_FRIEND_ACCEPT, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                parseBuilder.addAction(R.drawable.ic_done_black_24dp, "Accept", accept);
                parseBuilder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "Decline", pDeleteIntent);
            }

            if (pushData.has("accepted")) {
                String objectId = pushData.optString("accepted", "");
                final ParseUser me = ParseUser.getCurrentUser();
                final ParseRelation<ParseUser> relation = me.getRelation("friends");
                ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
                query.whereEqualTo(Constants.EXTRA_KEY_OBJECT_ID, objectId);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        ParseUser friend = list.get(0);
                        relation.add(friend);
                        me.saveInBackground();
                    }
                });

                // delete from pending friends list.
                final ParseRelation<ParseUser> pendingRelation = me.getRelation("pending_friends");
                pendingRelation.getQuery().whereEqualTo(Constants.EXTRA_KEY_OBJECT_ID, objectId).findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        ParseUser pending = list.get(0);
                        pendingRelation.remove(pending);
                        me.saveInBackground();
                    }
                });


            }

            return parseBuilder.build();
        } else {
            return null;
        }
    }
}
