package dk.izbrannick.glutter.sheetstest;

/**
 * Created by luther on 19/11/2016.
 */

import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.izbrannick.glutter.sheetstest.API.SheetsHandler;
import dk.izbrannick.glutter.sheetstest.SMS.SmsHandler;

import static dk.izbrannick.glutter.sheetstest.StaticDB.*;

public class UpdateService extends AppCompatActivity {

    /**
     * This service keeps values in StaticDB updated. Every StaticDB.updateRefreshRate
     */
    UpdateService() {

        groupMessage_ = "";
        currentTime_ = getCurrentTimeStamp();

        //Update repeatably
        t.start();

    }

    Thread t = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(updateRefreshRate);

                    // Update Contacts
                    try {
                        myContacts_ = SheetsHandler.getAllContacs(sheetId, "Contact!A1:F");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Update Groups
                    try {
                        myGroups_ = SheetsHandler.getAllGroups(sheetId, "Groups!A1:A99");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Update Message
                    try {
                        groupMessage_ = "" + SheetsHandler.getColumnsLastObject(sheetId, "Ark1!F1:F99");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Check for group message
                    if (!(groupMessage_.equalsIgnoreCase(groupMessageOld_))) {
                        groupMessageOld_ = groupMessage_;
                        SmsHandler smsHandler = new SmsHandler(groupMessage_, "from sheet");
                        smsHandler.startSmsTask();
                    }

                    //Update timestamp
                    currentTime_ = getCurrentTimeStamp();


                    /*
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // update here!

                        }
                    });
                    */
                }
            } catch (InterruptedException e) {
            }
        }
    };


    /**
     * @return yyyy-MM-dd HH:mm:ss formate date as string
     */
    public static String getCurrentTimeStamp() {
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