package dk.izbrannick.glutter.sheetstest.SMS;

/**
 * Created by u321424 on 09-11-2016.
 */


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dk.izbrannick.glutter.sheetstest.AsyncResult;
import dk.izbrannick.glutter.sheetstest.DownloadWebPageTask;
import dk.izbrannick.glutter.sheetstest.MyContact;

/**
 * Created by luther on 02/04/15.
 */
public class MyBroadcastReceiver extends android.content.BroadcastReceiver {

    static String beskedOld = "";
    String currMsg = "";
    String currNr = "";
    Context context;
    SharedPreferences preferences;
    private String SpreadSheetURL_ = "https://spreadsheets.google.com/tq?key=1552qHrDx3gS6S2LU8wuhFYZoIZE1oQJ_B18HBqTivEs";
    ArrayList<String> myContactsAllNumbers = new ArrayList<>();
    ArrayList<String> myContactsLederNumbers = new ArrayList<>();
    ArrayList<MyContact> myContacts = new ArrayList<>();


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(
                "android.provider.Telephony.SMS_DELIVERED")) {
            Toast.makeText(context.getApplicationContext(), "SMS_DELIVERED",
                    Toast.LENGTH_LONG).show();

        }
        if (intent.getAction().equals(
                "android.provider.Telephony.SMS_RECEIVED")) {

            SmsMessage[] msg = null;
            this.context = context;
            preferences = context.getApplicationContext().getSharedPreferences("LittleSmsBroadcaster", context.MODE_PRIVATE);

            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus");
            msg = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                currNr = msg[i].getOriginatingAddress();
            }

            SmsMessage sms = msg[0];
            try {
                if (msg.length == 1 || sms.isReplace()) {
                    currMsg = sms.getDisplayMessageBody();
                } else {
                    StringBuilder bodyText = new StringBuilder();
                    for (int i = 0; i < msg.length; i++) {
                        bodyText.append(msg[i].getMessageBody());
                    }
                    currMsg = bodyText.toString();
                }
            } catch (Exception e) {
            }

            if (!currMsg.equals(beskedOld)) {

                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    new DownloadWebPageTask(new AsyncResult() {
                        @Override
                        public void onResult(JSONObject object) {
                            processJson(object);
                        }
                    }).execute(SpreadSheetURL_);

                    if (currMsg.startsWith("Teen")) {
                        SmsHandler smsHandler = new SmsHandler(context, myContactsAllNumbers, currMsg, currNr);
                        smsHandler.startSmsTask();
                    }
                    if (currMsg.startsWith("Test")) {
                        SmsHandler smsHandler = new SmsHandler(context, myContactsLederNumbers, currMsg, currNr);
                        smsHandler.startSmsTask();
                    }
                }

            }

        }

        beskedOld = currMsg;
    }

    private void processJson(JSONObject object) {

        try {
            myContacts = new ArrayList<>();
            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                String name = "";
                int numberPrimary = 0;
                int numberSecondary = 0;
                int numberOther = 0;
                boolean leaderState = false;

                try {
                    name = columns.getJSONObject(0).getString("v");
                }catch (Exception e){}

                try {
                    leaderState = columns.getJSONObject(1).getBoolean("v");
                }catch (Exception e){}

                try {
                    numberPrimary = columns.getJSONObject(2).getInt("v");
                }catch (Exception e){}

                try {
                    numberSecondary = columns.getJSONObject(3).getInt("v");
                }catch (Exception e){}

                try {
                    numberOther = columns.getJSONObject(4).getInt("v");
                }catch (Exception e){}

                MyContact myContact = new MyContact(name, numberPrimary, numberSecondary, numberOther, leaderState);

                myContacts.add(myContact);
                myContactsAllNumbers.add(String.valueOf(myContact.getNumberPrimary()));
                if (myContact.isLeader()) {
                    myContactsLederNumbers.add(String.valueOf(myContact.getNumberPrimary()));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}