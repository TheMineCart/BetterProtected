package tmc.BetterProtected.domain;

import org.bukkit.Material;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProtectedBlockTest {

    @Test
    public void shouldCreateKeyWhenMade() {
        ProtectedBlock jasonBlock = new ProtectedBlock(1, 2, 3, Material.DIRT, "Jason733i");

        assertThat(jasonBlock.getKey().getY(), is(2));
    }
}
