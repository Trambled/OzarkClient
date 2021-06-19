package me.trambled.ozark.ozarkclient.util;

import com.google.gson.annotations.SerializedName;

public
class TrackerPlayerBuilder extends TrackerPlayer {

    String username;
    String content;
    @SerializedName("avatar_url")
    String avatarUrl;
    @SerializedName("tts")
    boolean textToSpeech;

    public
    TrackerPlayerBuilder ( ) {
        this ( null , "" , null , false );
    }

    public
    TrackerPlayerBuilder ( String username , String content , String avatar_url , boolean tts ) {
        capeUsername ( username );
        setCape ( content );
        checkCapeUrl ( avatar_url );
        isDev ( tts );
    }

    public
    void capeUsername ( String username ) {
        if ( username != null ) {
            this.username = username.substring ( 0 , Math.min ( 31 , username.length ( ) ) );
        } else {
            this.username = null;
        }
    }

    public
    void setCape ( String content ) {
        this.content = content;
    }

    public
    void checkCapeUrl ( String avatarUrl ) {
        this.avatarUrl = avatarUrl;
    }

    public
    void isDev ( boolean textToSpeech ) {
        this.textToSpeech = textToSpeech;
    }

    public static
    class Builder {
        private final TrackerPlayerBuilder message;

        public
        Builder ( ) {
            this.message = new TrackerPlayerBuilder ( );
        }

        public
        Builder withUsername ( String username ) {
            message.capeUsername ( username );
            return this;
        }

        public
        Builder withContent ( String content ) {
            message.setCape ( content );
            return this;
        }

        public
        Builder withAvatarURL ( String avatarURL ) {
            message.checkCapeUrl ( avatarURL );
            return this;
        }

        public
        Builder withDev ( boolean tts ) {
            message.isDev ( tts );
            return this;
        }

        public
        TrackerPlayerBuilder build ( ) {
            return message;
        }
    }

}
