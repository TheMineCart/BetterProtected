package tmc.BetterProtected.svc;

import com.google.gson.Gson;
import com.mongodb.*;
import org.junit.Test;
import tmc.BetterProtected.domain.Player;

import java.net.UnknownHostException;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PlayerRepositoryTest {

    @Test
    public void canOpenAMongoDBConnection() throws UnknownHostException {

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

    @Test
    public void canDeserializePlayer() {
        String playerString = "{\"username\":\"Jason\"}";

        Gson gson = new Gson();
        Player player = gson.fromJson(playerString, Player.class);

        assertThat(player.getUsername(), is("Jason"));
    }

    @Test
    public void canSerializePlayer() {
        String expectedJson = "{\"username\":\"Jason\"}";
        Player jason = new Player("Jason");

        assertThat(new Gson().toJson(jason, Player.class), is(expectedJson));
    }
}
