package dk.izbrannick.glutter.sheetstest;

/**
 * Created by luther on 19/11/2016.
 */

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.izbrannick.glutter.sheetstest.API.SheetsHandler;
import dk.izbrannick.glutter.sheetstest.SMS.SmsHandler;
import dk.izbrannick.glutter.sheetstest.SMS.StringValidator;

import static dk.izbrannick.glutter.sheetstest.StaticDB.*;

public class UpdateService extends IntentService implements Runnable{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public UpdateService() {
        super("UpdateService");
    }

    /**
     * This service keeps values in StaticDB updated. Every StaticDB.updateRefreshRate
     */
    public void startUpdating()
    {
        enableUpdateData_ = true;
    }
    public void stopUpdating()
    {
        enableUpdateData_ = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        run();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        enableUpdateData_ = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        enableUpdateData_ = false;
    }

    @Override
    public void run() {

        while (enableUpdateData_) {

            Log.i("Update Service", "Updating....from.....onHandleIntent.......");

            //TODO: REMOVE -  tryout of getting all parameters from ( pmdb!A1:A99 )
            // -- Update all parameters
            try {
                SheetsHandler.updateParametersInStaticDB(sheetId, pmdbSheetRange);
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

            // Update Message From Sheets
            try {
                String selectedGroup = SheetsHandler.getColumnsLastObject(sheetId, selectedGroupForGroupMessageSheetRange).toString().replace("[", "").replace("]", "");
                String msg = SheetsHandler.getColumnsLastObject(sheetId, messagesSheetRange).toString().replace("[", "").replace("]", "");
                if (!msg.isEmpty())
                {
                    groupMessage_ = selectedGroup + " " + msg;
                }
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





                    /*
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // update here!

                        }
                    });
                    */
            try {
                Thread.sleep(updateDataRefreshRate_);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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