package tmc.BetterProtected.domain;

import org.bukkit.Material;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ProtectedWorldTest {

    @Test
    public void shouldGetChunkFromKey() {
        ProtectedWorld world = new ProtectedWorld();
        ProtectedChunk chunk = new ProtectedChunk(3, 4);
        world.addChunk(chunk);

        assertThat(world.getChunkFrom(chunk.getKey()), is(chunk));
    }

    @Test
    public void shouldGetEmptyChunkFromBadKey() {
        ProtectedWorld world = new ProtectedWorld();

        ProtectedChunk actualChunk = world.getChunkFrom(new ProtectedChunkKey(0, 0));
        assertThat(actualChunk, is(not(nullValue())));
        assertThat(actualChunk.getChunk().size(), is(0));
    }

    @Test
    public void shouldGetNewChunkForKey() {
        ProtectedWorld world = new ProtectedWorld();
        ProtectedChunkKey newKey = new ProtectedChunkKey(0, 0);
        
        world.addNewChunkForKey(newKey);
        
        assertThat(world.numberOfChunks(), is(1));
        assertThat(world.getChunkFrom(newKey).getChunk().size(), is(0));
    }

    @Test
    public void shouldGetNumberOfBlocks() {
        ProtectedWorld world = new ProtectedWorld();
        ProtectedChunkKey aKey = new ProtectedChunkKey(1, 1);

        world.addNewChunkForKey(aKey);
        ProtectedChunk chunk = world.getChunkFrom(aKey);
        chunk.addBlock(new ProtectedBlock(1, 2, 3, Material.DIRT, "Jason"));
        chunk.addBlock(new ProtectedBlock(2, 2, 3, Material.DIRT, "Bob"));
        chunk.addBlock(new ProtectedBlock(3, 2, 3, Material.DIRT, "Jeremy"));
        chunk.addBlock(new ProtectedBlock(4, 2, 3, Material.DIRT, "James"));

        ProtectedChunkKey anotherKey = new ProtectedChunkKey(2, 2);

        world.addNewChunkForKey(anotherKey);
        ProtectedChunk anotherChunk = world.getChunkFrom(anotherKey);
        anotherChunk.addBlock(new ProtectedBlock(1, 3, 4, Material.DIRT, "Jason"));
        anotherChunk.addBlock(new ProtectedBlock(2, 3, 4, Material.DIRT, "Bob"));
        anotherChunk.addBlock(new ProtectedBlock(3, 3, 4, Material.DIRT, "Jeremy"));
        anotherChunk.addBlock(new ProtectedBlock(4, 3, 4, Material.DIRT, "James"));

        assertThat(world.numberOfChunks(), is(2));
        assertThat(world.numberOfBlocks(), is(8));
    }
}
