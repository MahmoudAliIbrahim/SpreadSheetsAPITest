package dk.izbrannick.glutter.sheetstest.SMS;

import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import dk.izbrannick.glutter.sheetstest.Constants;
import static dk.izbrannick.glutter.sheetstest.Constants.*;

/**
 * Created by u321424 on 07-11-2016.
 */

public class SmsHandler {

    private ArrayList<String> numbers;
    private String message, senderNumber;
    private SmsManager smsManager;

    public SmsHandler(String message, String senderNumber)
    {
        this.message = message;
        this.senderNumber = senderNumber;
        this.smsManager = SmsManager.getDefault();
    }

    public void startSmsTask()
    {
        if (message.startsWith("Teen")) {
            numbers = myContactsAllNumbers_;
            new LongOperation().execute(senderNumber, message);
        }
        if (message.startsWith("Leder")) {
            numbers = myContactsLeaderNumbers_;
            new LongOperation().execute(senderNumber, message);
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        ArrayList<String> iFragmentList = null;

        @Override
        protected String doInBackground(String... params) {
            Log.d("LongOperation", "doInBackground");

            iFragmentList = smsManager.divideMessage(params[1]);

            try {
                    for (int i = 0; i < numbers.size(); i++) {
                        Log.d("SEND SMS", "Sending to #" + i + " "+ numbers.get(i));
                        smsManager.sendMultipartTextMessage(numbers.get(i), null, iFragmentList, null, null);
                    }
            } catch (Exception e) {
                Thread.interrupted();
                Log.d("Exception", e.getMessage());
            }

            List<Object> results = new ArrayList<>();
            results.add(message);
            List<List<Object>> resultsInResults = new ArrayList<>();
            resultsInResults.add(results);

            ValueRange response = new ValueRange();

            response.setRange("messages!A1:A");
            response.setValues(resultsInResults);

            List<List<Object>> values = response.getValues();

            ValueRange valueRange = new ValueRange();
            valueRange.setValues(values);

            try {
                Constants.mService_.spreadsheets().values().append(spreadsheetsIdOnly_, "messages!A1:A", valueRange).setValueInputOption("RAW").execute();
                if (values != null) {
                    for (List row : values) {
                        results.add(row.get(0) + ", " + row.get(0));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
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