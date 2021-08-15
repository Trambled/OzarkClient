package me.trambled.ozark.ozarkclient.util.player.social;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

public class FriendUtil {

    public static ArrayList<Friend> friends = new ArrayList<>();

    public static boolean isFriend(String name) {
        return friends.stream().filter(Objects::nonNull).anyMatch(friend -> friend.getUsername().equalsIgnoreCase(name));
    }

    public static class Friend {
        String username;
        UUID uuid;

        public Friend(String username, UUID uuid) {
            this.username = username;
            this.uuid = uuid;
        }

        public String getUsername() {
            return username;
        }

        public UUID getUUID() {
            return uuid;
        }
    }

    public static FriendUtil.Friend get_friend_object(String name) {
        ArrayList<NetworkPlayerInfo> infoMap = new ArrayList <> ( Objects.requireNonNull ( mc.getConnection ( ) ).getPlayerInfoMap ( ) );
        NetworkPlayerInfo profile = infoMap.stream().filter(networkPlayerInfo -> networkPlayerInfo.getGameProfile().getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        if (profile == null) {
            String s = request_ids("[\"" + name + "\"]");
            if (s == null || s.isEmpty()) {
                // cant find
            } else {
                JsonElement element = new JsonParser().parse(s);
                if (element.getAsJsonArray().size() != 0) {
                    try {
                        String id = element.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
                        String username = element.getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
                        return new Friend(username, UUIDTypeAdapter.fromString(id));
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        return new Friend(profile.getGameProfile().getName(), profile.getGameProfile().getId());
    }
    
    public static List<EntityPlayer> get_friends() {
        if (mc.world.getLoadedEntityList().size() == 0)
            return null;

        return mc.world.playerEntities.stream().filter(entityPlayer -> mc.player != entityPlayer).filter(entityPlayer -> !entityPlayer.isDead).filter(entityPlayer -> FriendUtil.isFriend(entityPlayer.getName())).collect(Collectors.toList());
    }

    
    private static String request_ids(String data) {
        try{
            String query = "https://api.mojang.com/profiles/minecraft";

            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes( StandardCharsets.UTF_8 ));
            os.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String res = convertStreamToString(in);
            in.close();
            conn.disconnect();

            return res;
        }catch (Exception e) {
            return null;
        }
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "/";
    }

}
