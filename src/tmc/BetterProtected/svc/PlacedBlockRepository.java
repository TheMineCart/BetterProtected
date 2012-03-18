package tmc.BetterProtected.svc;

import com.mongodb.DBCollection;

public class PlacedBlockRepository {
    private DBCollection collection;

    public PlacedBlockRepository(DBCollection collection) {
        this.collection = collection;
    }
}
