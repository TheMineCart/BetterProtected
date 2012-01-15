package tmc.BetterProtected.domain;

import java.util.HashMap;
import java.util.Map;

public class ProtectedChunk {
    private final ProtectedChunkKey key;
    private Map<String, ProtectedBlock> blocks;

    public ProtectedChunk(int x, int z) {
        this.key = new ProtectedChunkKey(x, z);
        this.blocks = new HashMap<String, ProtectedBlock>();
    }

    public int getX() {
        return key.getX();
    }

    public int getZ() {
        return key.getZ();
    }

    public ProtectedChunkKey getKey() {
        return key;
    }

    public Map<String, ProtectedBlock> getBlocks() {
        return blocks;
    }
}
