package dk.izbrannick.glutter.sheetstest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dk.izbrannick.glutter.sheetstest.SMS.SmsHandler;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "HttpExample";
    ArrayList<MyContact> myContacts = new ArrayList<>();
    ArrayList<String> myContactsNumbers = new ArrayList<>();
    private ListView listview;
    // teams - private String SpreadSheetURL_ = "https://spreadsheets.google.com/tq?key=15nFv1Ap8NHAwFW0NU6ow9DGLdI4sT4pLiCUfdFmW6XQ";
    private String SpreadSheetURL_ = "https://spreadsheets.google.com/tq?key=1552qHrDx3gS6S2LU8wuhFYZoIZE1oQJ_B18HBqTivEs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            new DownloadWebPageTask(new AsyncResult() {
                @Override
                public void onResult(JSONObject object) {
                    processJson(object);
                }
            }).execute(SpreadSheetURL_);

            //update repeatably
            //t.start();
        }
        SmsHandler smsHandler = new SmsHandler(this, myContactsNumbers, "This is a test, lskadja lsk  alskjd las jdlkas jd", "77777777");
        smsHandler.startSmsTask();
    }

    Thread t = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // update TextView here!
                            new DownloadWebPageTask(new AsyncResult() {
                                @Override
                                public void onResult(JSONObject object) {
                                    processJson(object);
                                }
                            }).execute(SpreadSheetURL_);
                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };

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

                try {
                    name = columns.getJSONObject(0).getString("v");
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

                MyContact myContact = new MyContact(name, numberPrimary, numberSecondary, numberOther);

                myContacts.add(myContact);
                myContactsNumbers.add(String.valueOf(myContact.getNumberPrimary()));
            }

            final MyContactsAdapter adapter = new MyContactsAdapter(this, R.layout.my_contact, myContacts);
            listview.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
