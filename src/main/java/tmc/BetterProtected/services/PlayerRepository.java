// Copyright (C) 2012 Cyrus Innovation
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
    private static DBCollection collection;
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeAdaptor()).create();

    public static void initialize(DBCollection collection) {
        PlayerRepository.collection = collection;
    }

    public static void save(Player player) {
        DBObject object = (DBObject) JSON.parse(gson.toJson(player, Player.class));
        BasicDBObject updateWhere = new BasicDBObject();
        updateWhere.put("username", player.getUsername());
        collection.update(updateWhere, object, true, false);
    }

    public static Player findByName(String name) {
        BasicDBObject query = new BasicDBObject();
        query.put("username", name);
        DBObject dbObject = collection.findOne(query);
        if(dbObject == null) return null;
        return gson.fromJson(dbObject.toString(), Player.class);
    }

    public static boolean findPlayerProtectionByName(String name) {
        Player player = findByName(name);
        if(player == null) return false;
        return player.getProtectionEnabled();
    }

    public static Set<String> findFriendsByName(String playerName) {
        Player player = findByName(playerName);
        if(player == null) return newHashSet();
        return player.getFriends();
    }

    public static Long count() {
        return collection.count();
    }

    public static void initializeIndexes(){
        collection.ensureIndex(new BasicDBObject("username", 1));
    }

    public static boolean validPlayerName(String playerName) {
        return findByName(playerName) != null;
    }
}
