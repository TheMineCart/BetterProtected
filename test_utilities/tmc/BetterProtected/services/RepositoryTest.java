package tmc.BetterProtected.services;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import java.net.UnknownHostException;

import static junit.framework.Assert.fail;

public class RepositoryTest {
    private static final String TEST_DATABASE_NAME = "test";
    private Mongo mongo = null;

    protected DBCollection getCollection(String collection) {
        DB database = getTestDatabase();

        return database.getCollection(collection);
    }

    protected void clearTestData() {
        DB database = getTestDatabase();
        database.dropDatabase();
        mongo.close();
    }

    private DB getTestDatabase() {
        try {
            mongo = new Mongo();
        } catch (UnknownHostException e) {
            mongo.close();
            e.printStackTrace();
            fail("Unable to connect to a MongoDB Session.");
        }
        return mongo.getDB(TEST_DATABASE_NAME);
    }
}
