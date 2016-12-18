package dk.izbrannick.glutter.sheetstest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luther on 10/11/2016.
 * This is SIMPLE Static DB - KISS
 */

public class StaticDB {
    public static String applicationName = "smsportal";
    public static String sheetId = "1XWNq-rqnLot0JZztxzcgCG-Cny133Sb_Lawyh4Z8wig";
    public static List<Object> myGroups_;
    public static List<MyContact> myContacts_;
    public static ArrayList<String> myContactsAllNumbers_;
    public static ArrayList<String> myContactsLeaderNumbers_;
    public static String groupMessage_ = "";
    public static String groupMessageOld_ = "";
    public static com.google.api.services.sheets.v4.Sheets mService_ = null;
    public static String currentTime_;
    public static long updateRefreshRate = 10000;
    // TODO: (1) to be tested with broadcast + sheets
    public static String currSenderNumber_;
}
