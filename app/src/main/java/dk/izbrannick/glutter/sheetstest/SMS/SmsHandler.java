package dk.izbrannick.glutter.sheetstest.SMS;

import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.api.services.sheets.v4.model.UpdateValuesResponse;

import java.util.ArrayList;
import java.util.List;

import dk.izbrannick.glutter.sheetstest.API.SheetsHandler;
import dk.izbrannick.glutter.sheetstest.MyContact;
import dk.izbrannick.glutter.sheetstest.MyGroup;

import static dk.izbrannick.glutter.sheetstest.StaticDB.contactsSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.currSenderNumber_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.groupMessage_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.messageLOGSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.messagesSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.myContacts_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.sheetId;
import static dk.izbrannick.glutter.sheetstest.StaticDB.words;

/**
 * Created by u321424 on 07-11-2016.
 */

public class SmsHandler {

    private SmsManager smsManager;
    List<MyContact> contactsInCurrentGroup;

    public SmsHandler()
    {
        this.smsManager = SmsManager.getDefault();
    }

    public void startSmsTask()
    {
        if (StringValidator.isGroupMessage(groupMessage_)) {
            // ---- Get current group ---- //
            MyGroup currentGroup = StringValidator.getCurrentGroup(groupMessage_);

            if (currentGroup != null)
            {
                // ---- Find current numbers ---- //
                if (myContacts_ != null)
                {
                     contactsInCurrentGroup = new ArrayList<>();
                    for (int i = 0; i < myContacts_.size(); i++)
                    {
                        MyContact myContact = myContacts_.get(i);
                        for (int g = 0; g < myContact.getGroups().size(); g++) {
                            Object myGroup = myContact.getGroups().get(g);
                            if (myGroup.toString().equals(currentGroup.getGroupName()))
                            {
                                contactsInCurrentGroup.add(myContact);
                            }
                        }
                    }
                }


                // ---- Start Send SMS Task  ---- //
                new LongOperation().execute(currSenderNumber_, groupMessage_);

            }
        }
        if (StringValidator.isSignup(groupMessage_)) {
            // Add user to google sheets
            /// words /// [0]Signup [1]Group Name [2]Name
            if (!words.isEmpty()) {
                if (words.size() > 1) {
                    new LongOperation2().execute(currSenderNumber_, groupMessage_, words.get(1) , words.get(2));
                }
            }
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... params) {
            Log.d("LongOperation", "doInBackground");

            ArrayList<String> fragmentedMessageList = smsManager.divideMessage(params[1]);

            try {
                    for (int i = 0; i < contactsInCurrentGroup.size(); i++) {
                        Log.d("SEND SMS", "Sending to #" + i + " "+ contactsInCurrentGroup.get(i));
                        if (contactsInCurrentGroup.get(i).getNumberPrimary() != null) {
                            smsManager.sendMultipartTextMessage(contactsInCurrentGroup.get(i).getNumberPrimary(), null, fragmentedMessageList, null, null);
                        }
                    }

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

    private class LongOperation2 extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... params) {
            Log.d("LongOperation", "doInBackground");

            String senderNumber = params[0];
            String message = params[1];
            String groupName = params[2];
            String senderName = params[3];


            //TODO: if contact is existing contact

                //TODO: get contacts already existing groups

                //TODO: if contact has already this group


            //TODO: ------ APPEND NEW USER TO CONTACTS SHEET LIST
            ArrayList<Object> userValues = new ArrayList<>();
            userValues.add(0, senderName); // name
            userValues.add(1,senderNumber); // phone
            userValues.add(2,""); // email
            userValues.add(3,""); // credit
            userValues.add(4,groupName); // group 1
            try {
                //SheetsHandler.getNumberRangePosition(sheetId, contactsSheetRange, senderNumber);
                //SheetsHandler.appendValues(sheetId, contactsSheetRange, userValues);
                //SheetsHandler.updateValues(sheetId, contactsSheetRange, userValues);
                UpdateValuesResponse updateValuesResponse = SheetsHandler.updateFieldWithParticularNumber(sheetId, contactsSheetRange, userValues, senderNumber);
                if (updateValuesResponse == null)
                {
                    SheetsHandler.appendValues(sheetId, contactsSheetRange, userValues);
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //TODO: Send SMS response to user
            try {
                smsManager.sendTextMessage(params[0], null, "Du er nu tilmeldt. Tak :)", null, null);
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