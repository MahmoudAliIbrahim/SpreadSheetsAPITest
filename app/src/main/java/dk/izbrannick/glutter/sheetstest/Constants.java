package dk.izbrannick.glutter.sheetstest;

import java.util.ArrayList;

/**
 * Created by luther on 10/11/2016.
 */

public class Constants {
    // teams - private String SpreadSheetURL_ = "https://spreadsheets.google.com/tq?key=15nFv1Ap8NHAwFW0NU6ow9DGLdI4sT4pLiCUfdFmW6XQ";
    //private String SpreadSheetURL_ = "https://spreadsheets.google.com/tq?key=1552qHrDx3gS6S2LU8wuhFYZoIZE1oQJ_B18HBqTivEs";
    //public static String SpreadSheetURL_ = "https://spreadsheets.google.com/tq?key=15nFv1Ap8NHAwFW0NU6ow9DGLdI4sT4pLiCUfdFmW6XQ"; //TEST
    public static String SpreadSheetURL_ = "https://spreadsheets.google.com/tq?key=1jxuF1ytooaTRwc-qDq5tHMwhJc7f5JtmM6zbb4mCN1I";
    public static String spreadsheetsIdOnly_ = "1jxuF1ytooaTRwc-qDq5tHMwhJc7f5JtmM6zbb4mCN1I";
    public static ArrayList<MyContact> myContacts_;
    public static ArrayList<String> myContactsAllNumbers_;
    public static ArrayList<String> myContactsLeaderNumbers_;
    public static String groupMessage_;
    public static String groupMessageOld_;
    public static com.google.api.services.sheets.v4.Sheets mService_ = null;
    public static String currentTime_;
}
