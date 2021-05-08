package me.trambled.ozark.ozarkclient.util;

public
class EzMessageUtil {

    private static String message;

    public static
    String get_message ( ) {
        return message;
    }

    public static
    void set_message ( String message ) {
        EzMessageUtil.message = message;
    }

}
