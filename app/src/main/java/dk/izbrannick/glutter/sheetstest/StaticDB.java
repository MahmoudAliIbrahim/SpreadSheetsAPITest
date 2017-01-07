package dk.izbrannick.glutter.sheetstest;

import java.util.List;

/**
 * Created by luther on 10/11/2016.
 * This is SIMPLE Static DB - KISS
 */

public class StaticDB {
    public static String applicationName_ = "smsportal";
    public static String sheetId = "1XWNq-rqnLot0JZztxzcgCG-Cny133Sb_Lawyh4Z8wig";
    public static List<MyGroup> myGroups_;
    public static List<MyContact> myContacts_;
    public static String groupMessage_ = "";
    public static String groupMessageOld_ = "";
    public static com.google.api.services.sheets.v4.Sheets mService_ = null;
    public static String currentTimeStamp_;
    public static boolean enableUpdateUI_ = true;
    public static boolean enableUpdateData_ = true;
    public static long updateUIRefreshRate_ = 3000; /// synchronizes with sheets - refresh rate
    public static long updateDataRefreshRate_ = 2000; /// synchronizes with sheets - refresh rate
    // TODO: (1) to be tested with broadcast + sheets
    public static String currSenderNumber_;
    public static String contactsSheetRange = "Contact!A2:H";
    public static String pmdbSheetRange = "pmdb!A2:C99";
    public static String groupsSheetRange = "Groups!A1:A99";
    public static String messagesSheetRange = "SendMessage!B2:B2";
    public static String selectedGroupForGroupMessageSheetRange = "SendMessage!A2:A";
    public static String messageLOGSheetRange = "MessageLOG!A1:A";
}