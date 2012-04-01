package tmc.BetterProtected.svc;

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
import tmc.BetterProtected.domain.World;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class BlockEventRepository {
    private DBCollection collection;
    private final Gson gson;

    public BlockEventRepository(DBCollection collection) {
        this.collection = collection;
        gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeAdaptor()).create();
    }

    public void save(BlockEvent blockEvent) {
        DBObject block = (DBObject) JSON.parse(gson.toJson(blockEvent, BlockEvent.class));
        collection.insert(block);
    }

    public List<BlockEvent> findByBlockCoordinate(BlockCoordinate coordinate, World world) {
        BasicDBObject query = new BasicDBObject();
        query.put("world", JSON.parse(gson.toJson(world, World.class)));
        query.put("blockCoordinate", JSON.parse(gson.toJson(coordinate, BlockCoordinate.class)));

        return buildBlockList(query);
    }

    public BlockEvent findMostRecent(BlockCoordinate coordinate, World world) {
        List<BlockEvent> blockEvents = findByBlockCoordinate(coordinate, world);
        if (blockEvents.size() == 0) return null;
        return blockEvents.get(0);
    }

    public List<BlockEvent> findByChunkCoordinate(ChunkCoordinate coordinate, World world) {
        BasicDBObject query = new BasicDBObject();
        query.put("world", JSON.parse(gson.toJson(world, World.class)));
        query.put("chunkCoordinate", JSON.parse(gson.toJson(coordinate, ChunkCoordinate.class)));

        return buildBlockList(query);
    }

    private List<BlockEvent> buildBlockList(BasicDBObject query) {
        List<BlockEvent> list = newArrayList();
        DBCursor cursor = collection.find(query);

        BasicDBObject order = new BasicDBObject();
        order.put("instant", -1);
        cursor.sort(order);

        while(cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            list.add(gson.fromJson(dbObject.toString(), BlockEvent.class));
        }
        return list;
    }

    public List<BlockEvent> all() {
        return buildBlockList(new BasicDBObject());
    }

    public Long count() {
        return collection.count();
    }

    public void initializeIndexes() {
        collection.ensureIndex(new BasicDBObject("blockCoordinate", 1), new BasicDBObject("world", 1));
        collection.ensureIndex(new BasicDBObject("chunkCoordinate", 1), new BasicDBObject("world", 1));
    }
}
