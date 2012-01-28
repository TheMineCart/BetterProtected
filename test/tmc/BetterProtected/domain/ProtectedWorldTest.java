package tmc.BetterProtected.domain;

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
}
