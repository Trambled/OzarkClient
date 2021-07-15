package me.trambled.ozark.ozarkclient.util.player.social;

import com.google.gson.*;

import java.io.*;
import java.util.Map;
import java.util.Objects;

public class FriendSync {

    public static void init(){
        FutureUtil.init();
        RusherhackUtil.init();
        PyroUtil.init();
        AbyssUtil.init();
        LambdaUtil.init();
        GamesenseUtil.init();
        OzarkUtil.init();
    }

    public static void read() {
        FutureUtil.read();
        RusherhackUtil.read();
        PyroUtil.read();
        AbyssUtil.read();
        LambdaUtil.read();
        GamesenseUtil.read();
        OzarkUtil.read();
    }

    public static void write() {
        FutureUtil.write();
        RusherhackUtil.write();
        PyroUtil.write();
        AbyssUtil.write();
        LambdaUtil.write();
        GamesenseUtil.write();
        OzarkUtil.write();
    }


    private static class FutureUtil {
        private static File futureFriends;
        private static Gson gson;
        private static Boolean exists = false;

        public static void init() {
            gson = new GsonBuilder().setPrettyPrinting().create();
            futureFriends = new File(System.getProperty("user.home") + "/Future/friends.json");
            if(futureFriends.exists())exists = true;
        }

        public static void read() {
            if(!exists) return;
            JsonArray array = null;
            try {
                array = gson.fromJson(new FileReader(futureFriends), JsonArray.class);
            } catch (FileNotFoundException ignored) {}
            for (int i = 0; i < Objects.requireNonNull(array).size(); i++) {
                JsonObject object = (JsonObject) array.get(i);
                FriendSyncUtil.addFriend(object.get("friend-label").getAsString(), object.get("friend-alias").getAsString(),null);
            }
        }

        public static void write() {
            if(!exists) return;
            JsonArray array = new JsonArray();
            for (Map.Entry<String, String> entry : FriendSyncUtil.getFriendMap().entrySet()) {
                JsonObject object = new JsonObject();
                object.add("friend-label", new JsonPrimitive(entry.getKey()));
                object.add("friend-alias", new JsonPrimitive(entry.getValue()));
                array.add(object);
            }
            try {
                FileWriter writer = new FileWriter(futureFriends);
                writer.write(gson.toJson(array));
                writer.flush();
                writer.close();
            } catch (IOException ignored) {}
        }
    }

    private static class PyroUtil {
        private static File pyroFriends;
        private static Gson gson;
        private static Boolean enabled = true;
        private static JsonArray enemies;
        private static Boolean exists = false;

        public static void init() {
            gson = new GsonBuilder().setPrettyPrinting().create();
            pyroFriends = new File("pyro/friends.json");
            if(pyroFriends.exists()) exists=true;
        }

        public static void read() {
            if(!exists) return;
            JsonObject object = null;
            try {
                object = gson.fromJson(new FileReader(pyroFriends), JsonObject.class);
            } catch (FileNotFoundException ignored) {}
            enabled = object.get("enabled").getAsBoolean();
            enemies = object.getAsJsonArray("enemies");
            for (JsonElement element : object.getAsJsonArray("friends")) {
                FriendSyncUtil.addFriend(element.getAsJsonObject().get("c").getAsString(), element.getAsJsonObject().get("0").getAsString(),null);
            }
        }

        public static void write() {
            if(!exists)return;
            JsonObject object = new JsonObject();
            object.add("enabled", new JsonPrimitive(enabled));
            JsonArray friends = new JsonArray();
            for (Map.Entry<String, String> entry :FriendSyncUtil.getFriendMap().entrySet()) {
                JsonObject friendObj = new JsonObject();
                friendObj.add("c", new JsonPrimitive(entry.getKey()));
                friendObj.add("0", new JsonPrimitive(entry.getValue()));
                friends.add(friendObj);
            }
            object.add("enemies", enemies);
            try {
                FileWriter writer = new FileWriter(pyroFriends);
                writer.write(gson.toJson(object));
                writer.flush();
                writer.close();
            } catch (IOException ignored) {}
        }
    }

    private static class RusherhackUtil {
        private static File rusherFriends;
        private static Gson gson;
        private static Boolean exists = false;

        public static void init() {
            gson = new GsonBuilder().setPrettyPrinting().create();
            rusherFriends = new File("rusherhack/friends.json");
            if(rusherFriends.exists()) exists = true;
        }

        public static void read() {
            if(!exists) return;
            JsonArray array = null;
            try {
                array = gson.fromJson(new FileReader(rusherFriends), JsonArray.class);
            } catch (FileNotFoundException ignored) {}
            for (int i = 0; i < Objects.requireNonNull(array).size(); i++) {
                JsonObject object = (JsonObject) array.get(i);
                FriendSyncUtil.addFriend(object.get("name").getAsString(), object.get("name").getAsString(),null);
            }
        }

        public static void write() {
            if(!exists) return;
            JsonArray array = new JsonArray();
            for (Map.Entry<String, String> entry : FriendSyncUtil.getFriendMap().entrySet()) {
                JsonObject object = new JsonObject();
                object.add("name", new JsonPrimitive(entry.getKey()));
                array.add(object);
            }
            try {
                FileWriter writer = new FileWriter(rusherFriends);
                writer.write(gson.toJson(array));
                writer.flush();
                writer.close();
            } catch (IOException ignored) {}
        }

    }

    private static class AbyssUtil{
        private static File abyssFriends;
        private static Gson gson;
        private static Boolean exists = false;

        public static void init() {
            gson = new GsonBuilder().setPrettyPrinting().create();
            abyssFriends = new File("Abyss/FriendList.json");
            if(abyssFriends.exists()) exists = true;
        }

        public static void read() {
            if(!exists) return;
            JsonObject object = null;
            try {
                object = gson.fromJson(new FileReader(abyssFriends), JsonObject.class);
            } catch (FileNotFoundException ignored) {}
            if(object == null) return;
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                FriendSyncUtil.addFriend(entry.getKey(),entry.getKey(),entry.getValue().getAsString());
            }
        }

        public static void write() {
            if(!exists) return;
            JsonObject object = new JsonObject();
            for (Map.Entry<String, String> entry : FriendSyncUtil.getFriendUUIDMap().entrySet()) {
                object.add(entry.getKey(),new JsonPrimitive(entry.getValue()));
            }
            try {
                FileWriter writer = new FileWriter(abyssFriends);
                writer.write(gson.toJson(object));
                writer.flush();
                writer.close();
            } catch (IOException ignored) {}
        }
    }

    private static class LambdaUtil{
        private static File lambdaFriends;
        private static Gson gson;
        private static Boolean enabled = true;
        private static Boolean exists = false;

        public static void init() {
            gson = new GsonBuilder().setPrettyPrinting().create();
            lambdaFriends = new File("lambda/friends.json");
            if(lambdaFriends.exists()) exists = true;
        }

        public static void read() {
            if(!exists) return;
            JsonObject object = null;
            try {
                object = gson.fromJson(new FileReader(lambdaFriends), JsonObject.class);
            } catch (FileNotFoundException ignored) {}
            if(object == null) return;
            enabled = object.get("Enabled").getAsBoolean();
            for(JsonElement element : object.getAsJsonArray("Friends")){
                JsonObject friendObject = element.getAsJsonObject();
                FriendSyncUtil.addFriend(friendObject.get("name").getAsString(),friendObject.get("name").getAsString(),friendObject.get("uuid").getAsString());
            }
        }

        public static void write() {
            if(!exists) return;
            JsonObject object = new JsonObject();
            object.add("Enabled",new JsonPrimitive(enabled));
            JsonArray friendArray = new JsonArray();
            for (Map.Entry<String, String> entry : FriendSyncUtil.getFriendUUIDMap().entrySet()) {
                JsonObject friendObject = new JsonObject();
                friendObject.add(entry.getKey(),new JsonPrimitive(entry.getValue()));
                friendArray.add(friendObject);
            }
            try {
                FileWriter writer = new FileWriter(lambdaFriends);
                writer.write(gson.toJson(object));
                writer.flush();
                writer.close();
            } catch (IOException ignored) {}
        }
    }

    private static class GamesenseUtil{
        private static File gamesenseFriends;
        private static Gson gson;
        private static Boolean exists = false;

        public static void init() {
            gson = new GsonBuilder().setPrettyPrinting().create();
            gamesenseFriends = new File("run/GameSense/Misc/Friends.json");
            if(gamesenseFriends.exists()) exists = true;
        }

        public static void read() {
            if(!exists) return;
            JsonObject object = null;
            try {
                object = gson.fromJson(new FileReader(gamesenseFriends), JsonObject.class);
            } catch (FileNotFoundException ignored) {}
            if(object == null) return;
            JsonArray array = object.getAsJsonArray("Friends");
            for(int i = 0; i<array.size();i++){
                JsonPrimitive primitiveFriend = array.get(i).getAsJsonPrimitive();
                FriendSyncUtil.addFriend(primitiveFriend.getAsString(), primitiveFriend.getAsString(),null);

            }
        }

        public static void write() {
            if(!exists) return;
            JsonObject object = new JsonObject();
            JsonArray friendArray = new JsonArray();
            for (Map.Entry<String, String> entry : FriendSyncUtil.getFriendUUIDMap().entrySet()) {
                friendArray.add(new JsonPrimitive(entry.getKey()));
            }
            object.add("Friends", friendArray);
            try {
                FileWriter writer = new FileWriter(gamesenseFriends);
                writer.write(gson.toJson(object));
                writer.flush();
                writer.close();
            } catch (IOException ignored) {}
        }
    }
    private static class OzarkUtil{ //TODO: fix this shit lol im gonna make it later - kambing
                                    //TODO: Trambled do u know how to make this working because no one uses wp2 friend util format
        private static File ozarkFriends;
        private static Gson gson;
        private static Boolean enabled = true;
        private static Boolean exists = false;

        public static void init() {
            gson = new GsonBuilder().setPrettyPrinting().create();
            ozarkFriends = new File("OZARKCLIENT/friends.json");
            if(ozarkFriends.exists()) exists = true;
        }

        public static void read() {
            if(!exists) return;
            JsonObject object = null;
            try {
                object = gson.fromJson(new FileReader(ozarkFriends), JsonObject.class);
            } catch (FileNotFoundException ignored) {}
            if(object == null) return;
            enabled = object.get("Enabled").getAsBoolean();
            for(JsonElement element : object.getAsJsonArray("Friends")){
                JsonObject friendObject = element.getAsJsonObject();
                FriendSyncUtil.addFriend(friendObject.get("name").getAsString(),friendObject.get("name").getAsString(),friendObject.get("uuid").getAsString());
            }
        }

        public static void write() {
            if(!exists) return;
            JsonObject object = new JsonObject();
            object.add("Enabled",new JsonPrimitive(enabled));
            JsonArray friendArray = new JsonArray();
            for (Map.Entry<String, String> entry : FriendSyncUtil.getFriendUUIDMap().entrySet()) {
                JsonObject friendObject = new JsonObject();
                friendObject.add(entry.getKey(),new JsonPrimitive(entry.getValue()));
                friendArray.add(friendObject);
            }
            try {
                FileWriter writer = new FileWriter(ozarkFriends);
                writer.write(gson.toJson(object));
                writer.flush();
                writer.close();
            } catch (IOException ignored) {}
        }
    }}

