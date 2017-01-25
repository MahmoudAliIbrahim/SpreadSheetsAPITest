package dk.izbrannick.glutter.sheetstest.API;


import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.izbrannick.glutter.sheetstest.MyContact;
import dk.izbrannick.glutter.sheetstest.MyGroup;

import static dk.izbrannick.glutter.sheetstest.StaticDB.applicationName_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.contactsSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.currSenderNumber_;
import static dk.izbrannick.glutter.sheetstest.StaticDB.currentCountryCode;
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
import static dk.izbrannick.glutter.sheetstest.StaticDB.resign;
import static dk.izbrannick.glutter.sheetstest.StaticDB.selectedGroupForGroupMessageSheetRange;
import static dk.izbrannick.glutter.sheetstest.StaticDB.sheetId;
import static dk.izbrannick.glutter.sheetstest.StaticDB.signup;
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
            return mService_.spreadsheets().values().append(sheetId, range, valueRange).setValueInputOption("USER_ENTERED").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Array list of values are added next incrementing to column, example:
     * | value1 || timestamp || other value || etc. value || and so on value ||
     * @return AppendValuesResponse - List of Append Values
     */
    public static UpdateValuesResponse updateValues(String sheetId, String range, ArrayList<Object> valueList)
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
            //return mService_.spreadsheets().values().append(sheetId, range, valueRange).setValueInputOption("RAW").execute();
            return mService_.spreadsheets().values().update(sheetId, range, valueRange).setValueInputOption("RAW").execute();
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
                        if (pmName.toString().equals("currentCountryCode"))
                            currentCountryCode = pmValue.toString();

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

                        //---------- Update Sms API
                        if (pmName.toString().equals("signup"))
                            signup = pmValue.toString();
                        if (pmName.toString().equals("resign"))
                            resign = pmValue.toString();

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

    public static UpdateValuesResponse updateFieldWithParticularNumber(String sheetId, String range, ArrayList<Object> valueList, String number)
    {
        UpdateValuesResponse returnValue = null;
        int position = -3;
        try {
            position = getNumberRangePosition(sheetId, range, number);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (position > 0) {
            position +=2;
            List<List<Object>> resultsInResults = new ArrayList<>();
            resultsInResults.add(valueList);

            ValueRange response = null;
            try {
                response = mService_.spreadsheets().values().get(sheetId, range).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            response.setRange(range);
            response.setValues(resultsInResults);

            List<List<Object>> values = response.getValues();

            ValueRange valueRange = new ValueRange();
            valueRange.setValues(values);

            // -- Update Range ( Contact!A2:H )
            String[] words = range.split("!"); // "Contact!" + "A2:H"
            if (words.length > 0) {
                words[1] = words[1].replaceFirst("\\d+.*", String.valueOf(position)) + ":" + words[1].substring(words[1].length() - 1);

                range = words[0] + "!" + words[1];
                try {
                    //return mService_.spreadsheets().values().append(sheetId, range, valueRange).setValueInputOption("RAW").execute();
                    returnValue = mService_.spreadsheets().values().update(sheetId, range, valueRange).setValueInputOption("USER_ENTERED").execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return returnValue;
    }

    /**
     *
     * @param spreadSheetId
     * @param range
     * @param number
     * @return -1 for Exception errer and -2 for not found/new number
     * @throws IOException
     */
    public static int getNumberRangePosition(String spreadSheetId, String range, String number) throws IOException
    {
        ValueRange response = mService_.spreadsheets().values().get(spreadSheetId, range).execute();
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                List row = values.get(i);
                try {
                    Object o = null;
                    String tempNumber;
                    try {
                        o = row.get(1);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    if (o != null) {
                        tempNumber = o.toString();
                        if (number != null) {
                            if (number.equals(tempNumber)) {
                                return i;
                            }
                        }
                    }
                } catch (Exception r) {
                    r.printStackTrace();
                    return -1;
                }
            }
        }
        return -2;
    }

    public static boolean updateContactInfo(MyContact contact, String groupName, String senderNumber)
    {
        boolean returnValue = false;
        ArrayList<Object> userValues = new ArrayList<>();
        userValues.add(0, contact.getName()); // name
        userValues.add(1, contact.getNumberPrimary()); // phone
        userValues.add(2, contact.getMail()); // email
        userValues.add(3, contact.getCredit()); // credit
        int groupPosition = 0;
        for (int g = 0; g < contact.getGroups().size(); g++)
        {
            groupPosition = g+4;
            userValues.add(groupPosition, contact.getGroups().get(g)); // group 1
        }
        userValues.add(groupPosition, groupName);

        //TODO: if contact has already this group
        try {
            UpdateValuesResponse updateValuesResponse = updateFieldWithParticularNumber(sheetId, contactsSheetRange, userValues, senderNumber);
            returnValue = true;
            if (updateValuesResponse == null) {
                returnValue = false;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            returnValue = false;
        } catch (Exception e) {
            e.printStackTrace();
            returnValue = false;
        }
        return returnValue;
    }

    public static boolean addNewContact(String groupName, String senderNumber, String senderName)
    {
        boolean returnValue = false;
        ArrayList<Object> userValues = new ArrayList<>();
        userValues.add(0, senderName); // name
        userValues.add(1, senderNumber); // phone
        userValues.add(2, ""); // email
        userValues.add(3, ""); // credit
        userValues.add(4, groupName); // group 1
        try {
            AppendValuesResponse appendValuesResponse = SheetsHandler.appendValues(sheetId, contactsSheetRange, userValues);
            if (appendValuesResponse == null) {
                returnValue = false;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            returnValue = false;
        } catch (Exception e) {
            e.printStackTrace();
            returnValue = false;
        }
        return returnValue;
    }

    public static boolean removeContactsGroup(MyContact contact, String groupName, String senderNumber)
    {
        boolean returnValue = false;
        ArrayList<Object> userValues = new ArrayList<>();
        userValues.add(0, contact.getName()); // name
        userValues.add(1, contact.getNumberPrimary()); // phone
        userValues.add(2, contact.getMail()); // email
        userValues.add(3, contact.getCredit()); // credit


        int numberOfGroups = contact.getGroups().size();

        if (numberOfGroups > 0) {
            int groupPosition = 0;
            for (int g = 0; g < numberOfGroups; g++) {
                groupPosition = g + 4;

                String currGroup = "ThereIsNoGroupYet01923875id";

                try {
                    currGroup = contact.getGroups().get(g).toString();
                }catch (IndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }

                if (currGroup.equalsIgnoreCase(groupName))
                {
                    userValues.add(groupPosition, null);
                }
                if (!currGroup.equalsIgnoreCase(groupName))
                {
                    userValues.add(groupPosition, currGroup);
                }

            }

            try {
                UpdateValuesResponse updateValuesResponse = updateFieldWithParticularNumber(sheetId, contactsSheetRange, userValues, senderNumber);
                returnValue = true;
                if (updateValuesResponse == null) {
                    returnValue = false;
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                returnValue = false;
            } catch (Exception e) {
                e.printStackTrace();
                returnValue = false;
            }
        }
        if (numberOfGroups < 1)
        {
            deleteValue(sheetId, contactsSheetRange);
        }
        return returnValue;
    }

}

