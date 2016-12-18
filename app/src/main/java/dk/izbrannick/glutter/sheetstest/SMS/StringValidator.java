package dk.izbrannick.glutter.sheetstest.SMS;

/**
 *
 * Created by izbrannick on 23-02-2015.
 * Edited by izbrannick on 10-12-2016.
 */

import java.util.ArrayList;

import static dk.izbrannick.glutter.sheetstest.StaticDB.myGroups_;

public class StringValidator {

    // checks if message contains requested signup fraze
    // [0]Signup [1]Group Name [2]Name
    public static ArrayList<String> words;

    // array is filled if isGroupMessage() executed | else NULL
    public static ArrayList<String> groupNumbers;

    public static String signup;
    public static String resign;

    public static boolean isSignup(String message)
    {
        words = null;
        if (!message.isEmpty()) {
            String[] splitedMessage = message.split(" ");
            if (splitedMessage.length > 1) {
                if (splitedMessage[0].equalsIgnoreCase(signup)) {
                    words = new ArrayList<>();
                    for (int i = 0; i < splitedMessage.length; i++)
                    {
                        words.add(splitedMessage[i]);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    // checks if message contains requested resign fraze
    public static boolean isResign(String message)
    {
        words = null;
        if (!message.isEmpty()) {
            String[] splitedMessage = message.split(" ");
            if (splitedMessage.length > 1) {
                if (splitedMessage[0].equalsIgnoreCase(resign)) {
                    words = new ArrayList<String>();
                    for (int i = 0; i < splitedMessage.length; i++)
                    {
                        words.add(splitedMessage[i]);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isGroupMessage(String message)
    {
        words = null;
        if (!message.isEmpty()) {
            String[] splitedMessage = message.split(" ");
            if (splitedMessage.length > 1) {
                words = new ArrayList<>();
                for (int i = 0; i < splitedMessage.length; i++)
                    words.add(splitedMessage[i]);
                if (isAGroup(words.get(0))) {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean isCreateGroup(String message)
    {
        if (!message.isEmpty() && message.startsWith("create group ")) {
                return true;
            }
        else {
            return false;
        }
    }

    public static boolean isTesting(String message)
    {
        if (message.equalsIgnoreCase("test")) {
            return true;
        }
        return false;
    }

    private static boolean isAGroup(String groupName)
    {
        for (int i = 0; i < myGroups_.size(); i++) {
            if (myGroups_.get(i).toString().equalsIgnoreCase(groupName)) {
                return true;
            }
        }
        return false;
    }
}