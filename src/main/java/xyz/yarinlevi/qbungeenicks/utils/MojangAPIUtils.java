package xyz.yarinlevi.qbungeenicks.utils;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import xyz.yarinlevi.qbungeenicks.exceptions.UUIDNotFoundException;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MojangAPIUtils {

    public static boolean isAccount(String name) {
        try {
            return getUUIDOfUsername(name) != null;
        } catch (IOException | UUIDNotFoundException e) {
            return false;
        }
    }


    public static String getUUIDOfUsername(String username) throws IOException, UUIDNotFoundException {
        Gson gson = new Gson();

        InputStream inputStream = new URL("https://api.mojang.com/users/profiles/minecraft/" + username).openStream();

        if (inputStream == null) throw new UUIDNotFoundException("&cThe UUID of the player you requested was not found.");

        Reader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        PlayerProfile playerProfile = gson.fromJson(readAll(in), PlayerProfile.class);

        if (playerProfile != null) {
            return playerProfile.getId();
        } else {
            throw new UUIDNotFoundException("&cThe UUID of the player you requested was not found.");
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static class PlayerProfile {
        @Getter @Setter String name;
        @Getter @Setter String id;
    }
}
