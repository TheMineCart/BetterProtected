package tmc.BetterProtected.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProtectedWorldTest {

    @Test
    public void shouldGetChunkFromKey() {
        ProtectedWorld world = new ProtectedWorld();
        ProtectedChunk chunk = new ProtectedChunk(3, 4);
        world.addChunk(chunk);

        assertThat(world.getChunkFrom(chunk.getKey()), is(chunk));
    }
}
