package dk.izbrannick.glutter.sheetstest.API;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.izbrannick.glutter.sheetstest.Constants;
import dk.izbrannick.glutter.sheetstest.MyContact;

import static dk.izbrannick.glutter.sheetstest.Constants.mService_;

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
            return Constants.mService_.spreadsheets().values().append(sheetId, range, valueRange).setValueInputOption("RAW").execute();
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
            return Constants.mService_.spreadsheets().values().append(sheetId, range, valueRange).setValueInputOption("RAW").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * Fetch a list of names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1jxuF1ytooaTRwc-qDq5tHMwhJc7f5JtmM6zbb4mCN1I/edit
     * @return ArrayList<Object> with column[columnNumber]
     * @throws IOException
     * @param range example = "Contact!A1:F"
     */
    public static List<Object> getColumnObjects(String spreadSheetId, String range, int columnNumber) throws IOException {

        List<Object> results = new ArrayList<>();
        ValueRange response = mService_.spreadsheets().values().get(spreadSheetId, range).execute();
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (List row : values) {
                results.add(row.get(columnNumber));
            }
        }
        return results;
    }


    /**
     * Gets a list of objects of contacts from a spreadsheet:
     * https://docs.google.com/spreadsheets/d/....................../edit
     * @return List<MyContact> list of all contacts
     * @throws IOException
     * @param range example = "Contact!A1:F"
     */
    public static List<MyContact> getAllContacs(String spreadSheetId, String range) throws IOException {

        List<MyContact> myContacts = new ArrayList<>();
        ValueRange response = mService_.spreadsheets().values().get(spreadSheetId, range).execute();
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (List row : values) {

                Object o1,o2,o3,o4,o5, o6;
                o1 = row.get(0);
                o2 = row.get(1);
                o3 = row.get(2);
                o4 = row.get(3);
                o5 = row.get(4);
                o6 = row.get(5);

                try {
                    MyContact myContact = new MyContact(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5));

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
}
