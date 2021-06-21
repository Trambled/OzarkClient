package me.trambled.ozark.ozarkclient.util;

public class AutoKitUtil {
    private static String message;

    public static void set_message(String message) {
        AutoKitUtil.message = message;
    }

    public static String get_message() {
        return message;
    }
}
