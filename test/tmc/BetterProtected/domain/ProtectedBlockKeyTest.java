package tmc.BetterProtected.domain;

import org.bukkit.Material;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProtectedBlockKeyTest {

    @Test
    public void shouldCompareAgainstObjectValues() {                  
        ProtectedBlock originalBlock = new ProtectedBlock(-50, 100, 30, Material.DIRT, "Jason733i");
        ProtectedBlockKey originalBlockKey = originalBlock.getKey();
        ProtectedBlockKey copyKey = new ProtectedBlockKey(100);

        assertThat(originalBlockKey, is(copyKey));
    }
}
