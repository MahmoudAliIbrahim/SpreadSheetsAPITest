package dk.izbrannick.glutter.sheetstest.SMS;

import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

import dk.izbrannick.glutter.sheetstest.API.SheetsHandler;

import static dk.izbrannick.glutter.sheetstest.StaticDB.currSenderNumber_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.groupMessage_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.messageLOGSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.messagesSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.sheetId;

/**
 * Created by u321424 on 07-11-2016.
 */

public class SmsHandler {

    private SmsManager smsManager;

    public SmsHandler()
    {
        this.smsManager = SmsManager.getDefault();
    }

    public void startSmsTask()
    {
        if (StringValidator.isGroupMessage(groupMessage_)) {
            new LongOperation().execute(currSenderNumber_, groupMessage_);
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        ArrayList<String> iFragmentList = null;

        @Override
        protected String doInBackground(String... params) {
            Log.d("LongOperation", "doInBackground");

            iFragmentList = smsManager.divideMessage(params[1]);

            try {
                //TODO:send sms's is deactivated!!!
                /*
                    for (int i = 0; i < numbers.size(); i++) {
                        Log.d("SEND SMS", "Sending to #" + i + " "+ numbers.get(i));
                        smsManager.sendMultipartTextMessage(numbers.get(i), null, iFragmentList, null, null);
                    }
                */
            } catch (Exception e) {
                Thread.interrupted();
                Log.d("Exception", e.getMessage());
            }

            // ------ APPEND
            try {
                SheetsHandler.appendValue(sheetId, messageLOGSheetRange, groupMessage_);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ------ DELETE
            try {
                // -------- Delete message
                SheetsHandler.deleteValue(sheetId, messagesSheetRange);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
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