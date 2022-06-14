package net.listerily.NinjaAdventure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class OptionsManager {
    private String nickname;
    private String character;
    private final ArrayList<String> characters;

    public OptionsManager(App app) {
        Random random = new Random();
        try {
            JSONObject optionsObject = new JSONObject(app.getResourceManager().getCachedResources().readText("Characters/characters.json"));
            JSONArray nicknameArray = optionsObject.getJSONArray("nicknames");
            JSONArray characterArray = optionsObject.getJSONArray("characters");
            this.nickname = nicknameArray.getString(random.nextInt(nicknameArray.length()));
            this.character = characterArray.getString(random.nextInt(characterArray.length()));
            characters = new ArrayList<>();
            characterArray.forEach(characterName -> characters.add((String) characterName));
        } catch (IOException e) {
            app.getAppLogger().log(Level.SEVERE, "FATAL: Unable to read characters config json. Throwing an exception.", e);
            throw new RuntimeException(e);
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getCharacter() {
        return character;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public ArrayList<String> getCharacters() {
        return characters;
    }
}
