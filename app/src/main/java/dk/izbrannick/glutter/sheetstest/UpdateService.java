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
import dk.izbrannick.glutter.sheetstest.SMS.StringValidator;

import static dk.izbrannick.glutter.sheetstest.StaticDB.*;

public class UpdateService extends AppCompatActivity {

    /**
     * This service keeps values in StaticDB updated. Every StaticDB.updateRefreshRate
     */
    UpdateService() {
        currentTimeStamp_ = getCurrentTimeStamp();

        //Update repeatably
        t.start();

    }

    Thread t = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(updateRefreshRate);

                    //TODO: REMOVE -  tryout of getting all parameters from ( pmdb!A1:A99 )
                    // -- Update all parameters
                    try {
                        SheetsHandler.getAllParameters(sheetId, pmdbSheetRange);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // -- Update timestamp
                    currentTimeStamp_ = getCurrentTimeStamp();

                    // -- Update Contacts
                    try {
                        myContacts_ = SheetsHandler.getAllContacs(sheetId, contactsSheetRange);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // -- Update Groups
                    //    1 - Create groups
                    try {
                        myGroups_ = SheetsHandler.getAllGroups(sheetId, groupsSheetRange);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Check for group message
                    if (!(groupMessage_.equalsIgnoreCase(groupMessageOld_))) {
                        groupMessageOld_ = groupMessage_;
                        if (StringValidator.isGroupMessage(groupMessage_)) {
                            SmsHandler smsHandler = new SmsHandler();
                            smsHandler.startSmsTask();
                        }
                    }


                    // Update Message
                    try {
                        groupMessage_ = "" + SheetsHandler.getColumnsLastObject(sheetId, groupsSheetRange);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


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