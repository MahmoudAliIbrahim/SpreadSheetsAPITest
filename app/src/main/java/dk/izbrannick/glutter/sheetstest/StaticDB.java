package dk.izbrannick.glutter.sheetstest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luther on 10/11/2016.
 * This is SIMPLE Static DB - KISS
 */

public class StaticDB {
    // teams - private String SpreadSheetURL_ = "https://spreadsheets.google.com/tq?key=15nFv1Ap8NHAwFW0NU6ow9DGLdI4sT4pLiCUfdFmW6XQ";
    //private String SpreadSheetURL_ = "https://spreadsheets.google.com/tq?key=1552qHrDx3gS6S2LU8wuhFYZoIZE1oQJ_B18HBqTivEs";
    //public static String SpreadSheetURL_ = "https://spreadsheets.google.com/tq?key=15nFv1Ap8NHAwFW0NU6ow9DGLdI4sT4pLiCUfdFmW6XQ"; //TEST
    public static String applicationName = "smsportal";
    public static String sheetId = "1jxuF1ytooaTRwc-qDq5tHMwhJc7f5JtmM6zbb4mCN1I";
    public static List<Object> myGroups_;
    public static List<MyContact> myContacts_;
    public static ArrayList<String> myContactsAllNumbers_;
    public static ArrayList<String> myContactsLeaderNumbers_;
    public static String groupMessage_;
    public static String groupMessageOld_;
    public static com.google.api.services.sheets.v4.Sheets mService_ = null;
    public static String currentTime_;
    public static long updateRefreshRate = 10000;
}
