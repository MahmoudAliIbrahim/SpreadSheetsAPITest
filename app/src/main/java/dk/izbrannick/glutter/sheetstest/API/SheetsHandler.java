package dk.izbrannick.glutter.sheetstest.API;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.izbrannick.glutter.sheetstest.MyGroup;
import dk.izbrannick.glutter.sheetstest.StaticDB;
import dk.izbrannick.glutter.sheetstest.MyContact;

import static dk.izbrannick.glutter.sheetstest.StaticDB.mService_;

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
            return StaticDB.mService_.spreadsheets().values().append(sheetId, range, valueRange).setValueInputOption("RAW").execute();
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
            return StaticDB.mService_.spreadsheets().values().append(sheetId, range, valueRange).setValueInputOption("RAW").execute();
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

                    MyContact myContact = new MyContact(row.get(0), row.get(1), row.get(2), row.get(3), groups);

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
    public static List<Object> getAllParameters(String spreadSheetId, String range) throws IOException {

        List<Object> myObjects = new ArrayList<>();
        ValueRange response = mService_.spreadsheets().values().get(spreadSheetId, range).execute();
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (List row : values) {

                Object temp = row;
                //String str = temp.toString();


                try {
                    myObjects.add(temp);
                }catch (Exception r)
                {
                    r.printStackTrace();
                    return null;
                }
            }
        }
        return myObjects;
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
