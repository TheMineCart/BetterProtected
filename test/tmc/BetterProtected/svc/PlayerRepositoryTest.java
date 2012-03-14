package tmc.BetterProtected.svc;

import com.mongodb.*;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.Set;

public class PlayerRepositoryTest {

    @Test
    public void testingMongoDbConnection() throws UnknownHostException {

        Mongo m = new Mongo();

        DB db = m.getDB("test");

        Set<String> collections = db.getCollectionNames();

        for (String s : collections) {
            System.out.println(s);
        }

        DBCollection collection = db.getCollection("foo");


        BasicDBObject doc = new BasicDBObject();

        doc.put("name", "MongoDB");
        doc.put("type", "database");
        doc.put("count", 1);

        BasicDBObject info = new BasicDBObject();

        info.put("x", 203);
        info.put("y", 102);

        doc.put("info", info);

        collection.insert(doc);


        DBCursor dbObjects = collection.find();
        for (DBObject myDoc : dbObjects) {

            System.out.println(myDoc);
        }
    }

    @Test
    public void deleteCollection() throws UnknownHostException {
        Mongo m = new Mongo();

        DB db = m.getDB("test");

        DBCollection collection = db.getCollection("foo");

        collection.drop();

        long count = collection.getCount();
        System.out.println(count);
    }
}
