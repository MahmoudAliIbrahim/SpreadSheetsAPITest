package dk.izbrannick.glutter.sheetstest;

/**
 * Created by luther on 19/11/2016.
 */

import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dk.izbrannick.glutter.sheetstest.API.SheetsHandler;
import dk.izbrannick.glutter.sheetstest.SMS.SmsHandler;

import static dk.izbrannick.glutter.sheetstest.Constants.*;

public class UpdateService extends AppCompatActivity {


    UpdateService() {

        groupMessage_ = "";
        currentTime_ = getCurrentTimeStamp();

        new DownloadWebPageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                /*
                try {
                    SheetsHandler.getAllContacs(spreadsheetsIdOnly_, "Contact!A1:F");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
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
                        SmsHandler smsHandler = new SmsHandler(groupMessage_, "from sheet");
                        smsHandler.startSmsTask();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // update here!
                            new DownloadWebPageTask(new AsyncResult() {
                                @Override
                                public void onResult(JSONObject object) {
                                    currentTime_ = getCurrentTimeStamp();
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

}