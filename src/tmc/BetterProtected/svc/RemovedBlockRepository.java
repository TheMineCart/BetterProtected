package tmc.BetterProtected.svc;

import com.mongodb.DBCollection;

public class RemovedBlockRepository {
    private DBCollection collection;

    public RemovedBlockRepository(DBCollection collection) {
        this.collection = collection;
    }
}
