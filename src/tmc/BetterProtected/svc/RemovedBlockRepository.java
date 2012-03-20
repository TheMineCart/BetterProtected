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
import tmc.BetterProtected.domain.ChunkCoordinate;
import tmc.BetterProtected.domain.RemovedBlock;
import tmc.BetterProtected.domain.World;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RemovedBlockRepository {
    private DBCollection collection;
    private final Gson gson;

    public RemovedBlockRepository(DBCollection collection) {
        this.collection = collection;
        gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeAdaptor()).create();
    }

    public void save(RemovedBlock removedBlock) {
        DBObject block = (DBObject) JSON.parse(gson.toJson(removedBlock, RemovedBlock.class));
        collection.insert(block);
    }

    public List<RemovedBlock> findByBlockCoordinate(BlockCoordinate coordinate, World world) {
        BasicDBObject query = new BasicDBObject();
        query.put("world", JSON.parse(gson.toJson(world, World.class)));
        query.put("blockCoordinate", JSON.parse(gson.toJson(coordinate, BlockCoordinate.class)));

        return buildBlockList(query);
    }

    public List<RemovedBlock> findByChunkCoordinate(ChunkCoordinate coordinate, World world) {
        BasicDBObject query = new BasicDBObject();
        query.put("world", JSON.parse(gson.toJson(world, World.class)));
        query.put("chunkCoordinate", JSON.parse(gson.toJson(coordinate, ChunkCoordinate.class)));

        return buildBlockList(query);
    }

    private List<RemovedBlock> buildBlockList(BasicDBObject query) {
        List<RemovedBlock> list = newArrayList();
        DBCursor cursor = collection.find(query);

        while(cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            list.add(gson.fromJson(dbObject.toString(), RemovedBlock.class));
        }
        return list;
    }
}
