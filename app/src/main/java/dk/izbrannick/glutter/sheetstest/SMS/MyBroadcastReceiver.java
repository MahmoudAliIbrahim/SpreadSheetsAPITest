package dk.izbrannick.glutter.sheetstest.SMS;

/**
 * Created by u321424 on 09-11-2016.
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by luther on 02/04/15.
 */
public class MyBroadcastReceiver extends android.content.BroadcastReceiver {

    static String beskedOld = "";
    String currMsg = "";
    String currNr = "";
    Context context;


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

                        SmsHandler smsHandler = new SmsHandler(currMsg, currNr);
                        smsHandler.startSmsTask();

            }

        }

        beskedOld = currMsg;
    }

}