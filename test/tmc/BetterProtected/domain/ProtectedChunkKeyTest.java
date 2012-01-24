package tmc.BetterProtected.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProtectedChunkKeyTest {

    @Test
    public void shouldCompareAgainstObjectValues() {
        ProtectedChunk originalChunk = new ProtectedChunk(-50, 100);
        ProtectedChunkKey originalChunkKey = originalChunk.getKey();
        ProtectedChunkKey copyKey = new ProtectedChunkKey(-50, 100);

        assertThat(originalChunkKey, is(copyKey));
    }
}
