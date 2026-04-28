package swmg.murilo.legendaryRaces.manager;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import swmg.murilo.legendaryRaces.LegendaryRaces;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class RaceSkinManager {

    private final String PROFILE_URL,SKIN_URL;
    private final Map<String, Collection<ProfileProperty>> cache;
    private static RaceSkinManager instance;
    private final File file;
    private final YamlConfiguration config;

    private RaceSkinManager() {
       PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft/";
       SKIN_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
       cache = new HashMap<>();
       file = new File(LegendaryRaces.getInstance().getDataFolder(), "config.yml");
       config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static RaceSkinManager getInstance() {
        if(instance == null){
            instance = new RaceSkinManager();
        }
        return instance;
    }

public void setPlayerSkin(Player player, String raceName) {
    String targetSkin = config.getString("racas." + raceName + ".skin");
    final PlayerProfile playerProfile = player.getPlayerProfile();
    playerProfile.setProperties(getTextureProperty(targetSkin));
    player.setPlayerProfile(playerProfile);

}

    private Collection<ProfileProperty> getTextureProperty(String targetSkin) {
        if (cache.containsKey(targetSkin))
            return cache.get(targetSkin);

        final String profileResponse = makeRequest(PROFILE_URL + targetSkin);
        final JsonObject profileObject = JsonParser.parseString(profileResponse).getAsJsonObject();
        final String uuid = profileObject.get("id").getAsString();

        final String skinResponse = makeRequest(SKIN_URL.formatted(uuid));
        final JsonObject skinObject = JsonParser.parseString(skinResponse).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
        final String value = skinObject.get("value").getAsString();
        final String signature = skinObject.get("signature").getAsString();
        final ProfileProperty profileProperty = new ProfileProperty("textures", value, signature);

        cache.put(targetSkin, List.of(profileProperty));

        return List.of(profileProperty);
    }

    private String makeRequest(String url) {

        try (final HttpClient client = HttpClient.newBuilder().build()) {
            final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
