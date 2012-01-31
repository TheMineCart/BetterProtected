package tmc.BetterProtected.domain;

import org.bukkit.Material;
import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.svc.TimeFreezeService;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProtectedBlockTest {

    @Before
    public void setUp() throws Exception {
        TimeFreezeService.freeze();
    }

    @After
    public void tearDown() throws Exception {
        TimeFreezeService.unfreeze();
    }

    @Test
    public void shouldCreateKeyWhenMade() {
        ProtectedBlock jasonBlock = new ProtectedBlock(1, 2, 3, Material.DIRT, "Jason733i");

        assertThat(jasonBlock.getKey().getY(), is(2));
    }

    @Test
    public void shouldTellMeIfAnotherBlockHasSameCoordinates() {
        ProtectedBlock jasonBlock = new ProtectedBlock(1, 2, 3, Material.DIRT, "Jason");
        ProtectedBlock bobBlock = new ProtectedBlock(1, 2, 3, Material.DIRT, "Bob");
        ProtectedBlock samBlock = new ProtectedBlock(4, 2, 3, Material.DIRT, "Sam");
        
        assertThat(jasonBlock.isSamePositionAs(bobBlock), is(true));
        assertThat(jasonBlock.isSamePositionAs(samBlock), is(false));
    }

    @Test
    public void shouldTimestampABlock() {
        ProtectedBlock jasonBlock = new ProtectedBlock(1, 2, 3, Material.DIRT, "Jason");
        
        assertThat(jasonBlock.getTimestamp(), is(LocalDateTime.now()));
    }
}
