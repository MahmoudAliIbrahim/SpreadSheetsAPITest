package dk.izbrannick.glutter.sheetstest.API;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.izbrannick.glutter.sheetstest.Constants;

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
     * @return ArrayList<String> with row[0] + row[1]
     * @throws IOException
     * @param range example = "Contact!A1:F"
     */
    public static List<String> getDataFromApi(String spreadSheetId, String range) throws IOException {

        List<String> results = new ArrayList<>();
        ValueRange response = mService_.spreadsheets().values()
                .get(spreadSheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (List row : values) {
                results.add(row.get(0) + ", " + row.get(1));
            }
        }
        return results;
    }
}
