package dk.izbrannick.glutter.sheetstest.API;


import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.izbrannick.glutter.sheetstest.Constants;

import static dk.izbrannick.glutter.sheetstest.Constants.currentTime_;

/**
 * Created by u321424 on 07-12-2016.
 */

public class APICalls {
    public static AppendValuesResponse appendToSheetWithTimeStamp(String sheetId, String range, String text)
    {
        List<Object> results = new ArrayList<>();
        results.add(text);
        results.add(currentTime_);
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
}
