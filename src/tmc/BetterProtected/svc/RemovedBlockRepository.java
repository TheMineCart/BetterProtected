package tmc.BetterProtected.svc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.joda.time.DateTime;
import tmc.BetterProtected.adaptors.DateTimeAdaptor;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.ChunkCoordinate;
import tmc.BetterProtected.domain.RemovedBlock;

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

    public List<RemovedBlock> findByBlockCoordinate(BlockCoordinate coordinate) {
        BasicDBList list = new BasicDBList();

        list.add(JSON.parse(gson.toJson(coordinate, BlockCoordinate.class)));
        BasicDBObject in = new BasicDBObject("$in", list);
        DBCursor dbCursor = collection.find(new BasicDBObject("blockCoordinate", in));

        return buildBlockList(dbCursor);
    }

    public List<RemovedBlock> findByChunkCoordinate(ChunkCoordinate coordinate) {
        BasicDBList list = new BasicDBList();

        list.add(JSON.parse(gson.toJson(coordinate, ChunkCoordinate.class)));
        BasicDBObject in = new BasicDBObject("$in", list);
        DBCursor dbCursor = collection.find(new BasicDBObject("chunkCoordinate", in));

        return buildBlockList(dbCursor);
    }

    private List<RemovedBlock> buildBlockList(DBCursor cursor) {
        List<RemovedBlock> list = newArrayList();

        while(cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            list.add(gson.fromJson(dbObject.toString(), RemovedBlock.class));
        }
        return list;
    }
}
