package me.trambled.ozark.ozarkclient.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public
class TrackerPlayer {
    private static final Gson gson = new Gson ( );
    private static final Gson PRETTY_PRINTING = ( new GsonBuilder ( ) ).setPrettyPrinting ( ).create ( );

    public
    String toJson ( boolean prettyPrinting ) {
        return prettyPrinting ? PRETTY_PRINTING.toJson ( this ) : gson.toJson ( this );
    }
}
