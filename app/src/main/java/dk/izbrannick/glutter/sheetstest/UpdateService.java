package dk.izbrannick.glutter.sheetstest;

/**
 * Created by luther on 19/11/2016.
 */

import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dk.izbrannick.glutter.sheetstest.SMS.SmsHandler;

import static dk.izbrannick.glutter.sheetstest.Constants.*;

public class UpdateService extends AppCompatActivity {


    UpdateService() {

        groupMessage_ = "";

        //getApplicationContext().ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);

        new DownloadWebPageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                processJson(object);
            }
        }).execute(SpreadSheetURL_);

        //update repeatably
        t.start();

    }

    Thread t = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(10000);

                    if (!(groupMessage_.equalsIgnoreCase(groupMessageOld_))) {
                        groupMessageOld_ = groupMessage_;
                        currentTime_ = getCurrentTimeStamp();
                        SmsHandler smsHandler = new SmsHandler(groupMessage_, "from sheet");
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


    /**
     *
     * @return yyyy-MM-dd HH:mm:ss formate date as string
     */
    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private void processJson(JSONObject object) {

        try {
            myContacts_ = new ArrayList<>();
            myContactsAllNumbers_ = new ArrayList<>();
            myContactsLeaderNumbers_ = new ArrayList<>();
            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                String name = "";
                boolean isLeader = false;
                boolean isTester = false;
                boolean isOnRecipientList = false;
                String numberPrimary = "0";
                String numberOther = "0";
                String credit = "";

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
                    numberOther = columns.getJSONObject(3).getString("f");
                }catch (Exception e){}

                try {
                    isOnRecipientList = columns.getJSONObject(4).getBoolean("v");
                }catch (Exception e){}

                try {
                    groupMessage_ = columns.getJSONObject(5).getString("v");
                }catch (Exception e){}

                try {
                    credit = columns.getJSONObject(6).getString("v");
                }catch (Exception e){}


                /*

                MyContact myContact = new MyContact(name, numberPrimary, isOnRecipientList, numberOther, isLeader, credit);
                myContacts_.add(myContact);
                if (myContact.getOnRecipientList()) {
                    myContactsAllNumbers_.add(myContact.getNumberPrimary());
                    if (myContact.isLeader()) {
                        myContactsLeaderNumbers_.add(myContact.getNumberPrimary());
                    }
                }
                */
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}