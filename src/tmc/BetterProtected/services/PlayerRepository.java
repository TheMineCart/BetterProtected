package tmc.BetterProtected.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.joda.time.DateTime;
import tmc.BetterProtected.adaptors.DateTimeAdaptor;
import tmc.BetterProtected.domain.Player;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class PlayerRepository {
    private DBCollection collection;
    private final Gson gson;

    public PlayerRepository(DBCollection collection) {
        this.collection = collection;
        gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeAdaptor()).create();
    }

    public void save(Player player) {
        DBObject object = (DBObject) JSON.parse(gson.toJson(player, Player.class));
        BasicDBObject updateWhere = new BasicDBObject();
        updateWhere.put("username", player.getUsername());
        collection.update(updateWhere, object, true, false);
    }

    public Player findByName(String name) {
        BasicDBObject query = new BasicDBObject();
        query.put("username", name);
        DBObject dbObject = collection.findOne(query);
        if(dbObject == null) return null;
        return gson.fromJson(dbObject.toString(), Player.class);
    }

    public Set<String> findFriendsByName(String playerName) {
        Player player = findByName(playerName);
        if(player == null) return newHashSet();
        return player.getFriends();
    }

    public Long count() {
        return collection.count();
    }
}
