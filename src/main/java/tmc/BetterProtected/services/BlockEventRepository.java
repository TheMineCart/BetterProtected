// Copyright (C) 2012 Cyrus Innovation
package tmc.BetterProtected.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.joda.time.DateTime;
import tmc.BetterProtected.adaptors.DateTimeAdaptor;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.ChunkCoordinate;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class BlockEventRepository {
    private static DBCollection collection;
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeAdaptor()).create();;

    public static void initialize(DBCollection collection) {
        BlockEventRepository.collection = collection;
    }

    public static void save(BlockEvent blockEvent) {
        DBObject block = (DBObject) JSON.parse(gson.toJson(blockEvent, BlockEvent.class));
        collection.insert(block);
    }

    public static List<BlockEvent> findByBlockCoordinate(BlockCoordinate coordinate, String world) {
        BasicDBObject query = new BasicDBObject();
        query.put("w", world);
        query.put("b", JSON.parse(gson.toJson(coordinate, BlockCoordinate.class)));

        return buildBlockList(query);
    }

    public static BlockEvent findMostRecent(BlockCoordinate coordinate, String world) {
        List<BlockEvent> blockEvents = findByBlockCoordinate(coordinate, world);
        if (blockEvents.size() == 0) return null;
        return blockEvents.get(0);
    }

    public static List<BlockEvent> findByChunkCoordinate(ChunkCoordinate coordinate, String world) {
        BasicDBObject query = new BasicDBObject();
        query.put("w", world);
        query.put("c", JSON.parse(gson.toJson(coordinate, ChunkCoordinate.class)));

        return buildBlockList(query);
    }

    private static List<BlockEvent> buildBlockList(BasicDBObject query) {
        List<BlockEvent> list = newArrayList();
        DBCursor cursor = collection.find(query);

        BasicDBObject order = new BasicDBObject();
        order.put("i", -1);
        cursor.sort(order);

        while(cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            list.add(gson.fromJson(dbObject.toString(), BlockEvent.class));
        }
        return list;
    }

    public static List<BlockEvent> all() {
        return buildBlockList(new BasicDBObject());
    }

    public static Long count() {
        return collection.count();
    }

    public static void initializeIndexes() {
        collection.ensureIndex(new BasicDBObject("b", 1), new BasicDBObject("w", 1));
        collection.ensureIndex(new BasicDBObject("b", 1), new BasicDBObject("w", 1));
    }
}
