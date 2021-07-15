package me.trambled.ozark.ozarkclient.util.player.social;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public static String getUUIDFromName(String name) throws IOException {
        Gson gson = new GsonBuilder().create();
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder resp = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null){
            resp.append(line);
        }
        JsonObject respObject = gson.fromJson(resp.toString(), JsonObject.class);

        return respObject.get("id").getAsString();
    }
}
