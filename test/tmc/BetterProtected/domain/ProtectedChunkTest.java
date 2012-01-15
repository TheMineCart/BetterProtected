package tmc.BetterProtected.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProtectedChunkTest {
    @Test
    public void shouldGenerateKeyWhenConstructed() {
        ProtectedChunk protectedChunk = new ProtectedChunk(12, -13);
        ProtectedChunkKey expectedKey = new ProtectedChunkKey(12, -13);

        assertThat(protectedChunk.getKey().getX(), is(expectedKey.getX()));
        assertThat(protectedChunk.getKey().getZ(), is(expectedKey.getZ()));
    }
}
