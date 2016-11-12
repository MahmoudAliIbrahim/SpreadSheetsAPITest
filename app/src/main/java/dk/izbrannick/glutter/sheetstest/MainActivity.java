package dk.izbrannick.glutter.sheetstest;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import dk.izbrannick.glutter.sheetstest.SMS.SmsHandler;

import static dk.izbrannick.glutter.sheetstest.Constants.*;

public class MainActivity extends AppCompatActivity {

    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        groupMessage = "";

        listview = (ListView) findViewById(R.id.listview);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        if (networkInfo != null && networkInfo.isConnected()) {

            try {
                new DownloadWebPageTask(new AsyncResult() {
                    @Override
                    public void onResult(JSONObject object) {
                        processJson(object);
                    }
                }).execute(SpreadSheetURL_);

                //update repeatably
                t.start();
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    Thread t = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(10000);

                    if (!(groupMessage.equalsIgnoreCase(groupMessageOld))) {
                        groupMessageOld = groupMessage;
                        SmsHandler smsHandler = new SmsHandler(groupMessage, "from sheet");
                        smsHandler.startSmsTask();
                    }
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
            myContactsAllNumbers = new ArrayList<>();
            myContactsLeaderNumbers = new ArrayList<>();
            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                String name = "";
                boolean isLeader = false;
                String numberPrimary = "0";
                String numberSecondary = "0";
                String numberOther = "0";

                try {
                    name = columns.getJSONObject(0).getString("v");
                }catch (Exception e){}

                try {
                    isLeader = columns.getJSONObject(1).getBoolean("v");
                }catch (Exception e){}

                try {
                    numberPrimary = columns.getJSONObject(2).getString("f");
                }catch (Exception e){}

                try {
                    numberSecondary = columns.getJSONObject(3).getString("f");
                }catch (Exception e){}

                try {
                    numberOther = columns.getJSONObject(4).getString("f");
                }catch (Exception e){}

                try {
                    groupMessage = columns.getJSONObject(10).getString("v");
                }catch (Exception e){}

                MyContact myContact = new MyContact(name, numberPrimary, numberSecondary, numberOther, isLeader);

                myContacts.add(myContact);
                myContactsAllNumbers.add(myContact.getNumberPrimary());
                if (myContact.isLeader()) {
                    myContactsLeaderNumbers.add(myContact.getNumberPrimary());
                }
            }

            final MyContactsAdapter adapter = new MyContactsAdapter(this, R.layout.my_contact, myContacts);
            listview.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
