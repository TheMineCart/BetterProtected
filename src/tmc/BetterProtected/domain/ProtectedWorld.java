package tmc.BetterProtected.domain;

import java.util.HashMap;
import java.util.Map;

public class ProtectedWorld {
    private Map<ProtectedChunkKey, ProtectedChunk> chunks;

    public ProtectedWorld() {
        this.chunks = new HashMap<ProtectedChunkKey, ProtectedChunk>();
    }

    public void addChunk(ProtectedChunk chunk) {
        chunks.put(chunk.getKey(), chunk);
    }

    public ProtectedChunk getChunkFrom(ProtectedChunkKey key) {
        ProtectedChunk chunk = chunks.get(key);
        if(null != chunk) 
            return chunk;

        addNewChunkForKey(key);
        return getChunkFrom(key);
    }

    public void addNewChunkForKey(ProtectedChunkKey key) {
        ProtectedChunk newKey = new ProtectedChunk(key.getX(), key.getZ());
        addChunk(newKey);
    }

    public Integer numberOfChunks() {
        return chunks.size();
    }

    public Integer numberOfBlocks() {
        int count = 0;
        for (ProtectedChunk chunk : chunks.values()) {
            count += chunk.numberOfBlocks();
        }
        return count;
    }
}
