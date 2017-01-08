package dk.izbrannick.glutter.sheetstest.SMS;

import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dk.izbrannick.glutter.sheetstest.API.SheetsHandler;
import dk.izbrannick.glutter.sheetstest.MyContact;
import dk.izbrannick.glutter.sheetstest.MyGroup;

import static dk.izbrannick.glutter.sheetstest.StaticDB.currSenderNumber_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.groupMessage_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.messageLOGSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.messagesSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.myContacts_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.sheetId;

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
}