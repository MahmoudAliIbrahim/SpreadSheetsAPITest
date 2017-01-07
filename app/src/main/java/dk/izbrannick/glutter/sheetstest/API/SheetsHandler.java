package dk.izbrannick.glutter.sheetstest.API;


import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.izbrannick.glutter.sheetstest.MyContact;
import dk.izbrannick.glutter.sheetstest.MyGroup;

import static dk.izbrannick.glutter.sheetstest.StaticDB.applicationName_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.contactsSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.currSenderNumber_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.currentTimeStamp_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.enableUpdateData_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.enableUpdateUI_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.groupMessageOld_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.groupMessage_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.groupsSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.mService_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.messageLOGSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.messagesSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.pmdbSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.selectedGroupForGroupMessageSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.updateDataRefreshRate_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.updateUIRefreshRate_;

/**
 * Created by u321424 on 07-12-2016.
 */

public class SheetsHandler {

    /**
     * Timestamp is added in the next column
     * | value || timestamp |
     * @return AppendValuesResponse
     */
    public static AppendValuesResponse appendValue(String sheetId, String range, String value)
    {
        List<Object> results = new ArrayList<>();
        results.add(value);
        List<List<Object>> resultsInResults = new ArrayList<>();
        resultsInResults.add(results);

        ValueRange response = new ValueRange();

        response.setRange(range);
        response.setValues(resultsInResults);

        List<List<Object>> values = response.getValues();

        ValueRange valueRange = new ValueRange();
        valueRange.setValues(values);
        try {
            return mService_.spreadsheets().values().append(sheetId, range, valueRange).setValueInputOption("RAW").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Delete a value from sheet
     * | value || timestamp |
     * @return AppendValuesResponse
     */
    public static void deleteValue(String sheetId, String range)
    {
        ClearValuesRequest clear = new ClearValuesRequest();

        try {
            mService_.spreadsheets().values().clear(sheetId, range, clear).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Array list of values are added next incrementing to column, example:
     * | value1 || timestamp || other value || etc. value || and so on value ||
     * @return AppendValuesResponse - List of Append Values
     */
    public static AppendValuesResponse appendValues(String sheetId, String range, ArrayList<Object> valueList)
    {
        List<List<Object>> resultsInResults = new ArrayList<>();
        resultsInResults.add(valueList);

        ValueRange response = new ValueRange();

        response.setRange(range);
        response.setValues(resultsInResults);

        List<List<Object>> values = response.getValues();

        ValueRange valueRange = new ValueRange();
        valueRange.setValues(values);
        try {
            return mService_.spreadsheets().values().append(sheetId, range, valueRange).setValueInputOption("RAW").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * Fetch a list of names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/................/edit
     * @return ArrayList<Object> with column[columnNumber]
     * @throws IOException
     * @param range example = "Contact!A1:F"
     */
    public static Object getColumnsLastObject(String spreadSheetId, String range) throws IOException {

        Object value = "";
        ValueRange response = mService_.spreadsheets().values().get(spreadSheetId, range).execute();
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (List row : values) {
                    value = row;
            }
        }
        return value;
    }

    /**
     * Gets a list of objects of contacts from a spreadsheet:
     * https://docs.google.com/spreadsheets/d/....................../edit
     * @return List<MyContact> list of all contacts
     * @throws IOException
     * @param range example = "Contact!A2:F"
     */
    public static List<MyContact> getAllContacs(String spreadSheetId, String range) throws IOException {

        List<MyContact> myContacts = new ArrayList<>();
        ValueRange response = mService_.spreadsheets().values().get(spreadSheetId, range).execute();
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (List row : values) {
                try {

                    ArrayList<Object> groups = new ArrayList<>();

                    for (int i = 4; i <= 7; i++)
                    {
                        try {
                            Object o = row.get(i);
                            if (!o.toString().isEmpty())
                            {
                                groups.add(o);
                            }
                        }catch (Exception r)
                        {
                            break;
                        }
                    }

                    Object phone = "0";
                    try {
                        phone = row.get(1);
                    }catch (Exception f)
                    {
                    }

                    Object mail = "0";
                    try {
                        mail = row.get(2);
                    }catch (Exception f)
                    {
                    }

                    Object credit = "0";
                    try {
                        credit = row.get(3);
                    }catch (Exception f)
                    {
                    }

                    MyContact myContact = new MyContact(row.get(0), phone, mail, credit , groups);

                    myContacts.add(myContact);
                }catch (Exception r)
                {
                    r.printStackTrace();
                    return null;
                }
            }
        }
        return myContacts;
    }

    /**
     * Gets a list of objects of contacts from a spreadsheet:
     * https://docs.google.com/spreadsheets/d/....................../edit
     * @return List<MyContact>  Returns list of all pmdb parameter Objects
     * @throws IOException
     * @param range example = "pmdb!A1:A99"
     */
    public static List<Object> updateParametersInStaticDB(String spreadSheetId, String range) throws IOException {

        ValueRange response = mService_.spreadsheets().values().get(spreadSheetId, range).execute();
        List<List<Object>> values = response.getValues();

        List<Object> prameterValues = new ArrayList<>();

        if (values != null) {
            for (List row : values) {

                try {
                    Object pmName = row.get(0);
                    Object pmValue = row.get(1);
                    if (!pmValue.toString().isEmpty()) {
                        if (pmName.toString().equals("applicationName_"))
                            applicationName_ = pmValue.toString();
                        if (pmName.toString().equals("groupMessage_"))
                            groupMessage_ = pmValue.toString();
                        if (pmName.toString().equals("groupMessageOld_"))
                            groupMessageOld_ = pmValue.toString();
                        if (pmName.toString().equals("currentTimeStamp_"))
                            currentTimeStamp_ = pmValue.toString();
                        if (pmName.toString().equals("enableUpdateUI_"))
                            enableUpdateUI_ = Boolean.valueOf(pmValue.toString());
                        if (pmName.toString().equals("enableUpdateData_"))
                            enableUpdateData_ = Boolean.valueOf(pmValue.toString());
                        if (pmName.toString().equals("updateUIRefreshRate_"))
                            updateUIRefreshRate_ = Long.valueOf((String) pmValue);
                        if (pmName.toString().equals("updateDataRefreshRate_"))
                            updateDataRefreshRate_ = Long.valueOf((String) pmValue);
                        if (pmName.toString().equals("currSenderNumber_"))
                            currSenderNumber_ = pmValue.toString();

                        //---------- Update Sheets Range
                        if (pmName.toString().equals("contactsSheetRange"))
                            contactsSheetRange = pmValue.toString();
                        if (pmName.toString().equals("pmdbSheetRange"))
                            pmdbSheetRange = pmValue.toString();
                        if (pmName.toString().equals("groupsSheetRange"))
                            groupsSheetRange = pmValue.toString();
                        if (pmName.toString().equals("messagesSheetRange"))
                            messagesSheetRange = pmValue.toString();
                        if (pmName.toString().equals("selectedGroupForGroupMessageSheetRange"))
                            selectedGroupForGroupMessageSheetRange = pmValue.toString();
                        if (pmName.toString().equals("messageLOGSheetRange"))
                            messageLOGSheetRange = pmValue.toString();

                    }
                }catch (Exception r)
                {
                }
            }
        }
        return prameterValues;
    }

    /**
     *
     * @param spreadSheetId - gets from separate Tab where you have all your groups listed
     * @param range - is probably "Groups!A:A10" or something like that
     * @return
     * @throws IOException
     */
    public static List<MyGroup> getAllGroups(String spreadSheetId, String range) throws IOException
    {
        List<MyGroup> myGroups = new ArrayList<>();
        ValueRange response = mService_.spreadsheets().values().get(spreadSheetId, range).execute();
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (List row : values) {
                try {
                    //myGroups.add(row.get(0));
                    MyGroup gr = new MyGroup((String) row.get(0));
                    myGroups.add(gr);
                }catch (Exception r)
                {
                    r.printStackTrace();
                    return null;
                }
            }
        }
        return myGroups;
    }
}
