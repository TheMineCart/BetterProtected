package tmc.BetterProtected.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

public class ProtectedChunk {
    private final ProtectedChunkKey key;
    private Map<ProtectedBlockKey, List<ProtectedBlock>> chunk;

    public ProtectedChunk(int x, int z) {
        this.key = new ProtectedChunkKey(x, z);
        this.chunk = new HashMap<ProtectedBlockKey, List<ProtectedBlock>>();
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

    public List<ProtectedBlock> getBlocksAt(ProtectedBlockKey key) {
        return chunk.get(key);
    }

    public Map<ProtectedBlockKey, List<ProtectedBlock>> getChunk() {
        return chunk;
    }

    public void addBlock(ProtectedBlock block) {
        List<ProtectedBlock> slice = chunk.get(block.getKey());
        if(slice == null) {
            slice = new ArrayList<ProtectedBlock>();
            chunk.put(block.getKey(), slice);
        }
        slice.add(block);
    }

    public Integer numberOfBlocks() {
        int count = 0;
        for (List<ProtectedBlock> blocks : Lists.newArrayList(chunk.values())) {
            count += blocks.size();
        }
        return count;
    }
}
