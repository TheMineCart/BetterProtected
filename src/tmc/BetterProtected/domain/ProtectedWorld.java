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
        return chunks.get(key);
    }
}
