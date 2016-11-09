package dk.izbrannick.glutter.sheetstest.SMS;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

import dk.izbrannick.glutter.sheetstest.MainActivity;
import dk.izbrannick.glutter.sheetstest.MyContact;

/**
 * Created by u321424 on 07-11-2016.
 */

public class SmsHandler {

    private Context context;
    private ArrayList<String> currentGroupNumbers = new ArrayList<>();
    private String message, senderNumber;
    private SmsManager smsManager;

    public SmsHandler(Context context, ArrayList<String> currentGroupNumbers, String message, String senderNumber)
    {
        this.context = context;
        this.currentGroupNumbers = currentGroupNumbers;
        this.message = message;
        this.senderNumber = senderNumber;
        this.smsManager = SmsManager.getDefault();
    }

    public void startSmsTask()
    {
        new LongOperation().execute(senderNumber, message);
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        ArrayList<String> iFragmentList = null;

        @Override
        protected String doInBackground(String... params) {
            Log.d("LongOperation", "doInBackground");

            iFragmentList = smsManager.divideMessage(params[1]);

            try {
                    for (int i = 0; i < currentGroupNumbers.size(); i++) {
                        smsManager.sendMultipartTextMessage(currentGroupNumbers.get(i), null, iFragmentList, null, null);
                    }
            } catch (Exception e) {
                Thread.interrupted();
                Log.d("Exception", e.getMessage());
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("onPostExecute", result);
        }

        @Override
        protected void onPreExecute() {
            Log.d("LongOperation", "PreExecute");
        }

        int m = 0;

        @Override
        protected void onProgressUpdate(Void... values) {
            m += 1;
            Log.d("LongOperation", "onProgressUpdate");
        }

    }
}