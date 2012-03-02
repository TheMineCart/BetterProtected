package tmc.BetterProtected.svc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.BetterProtectedPlugin;
import tmc.BetterProtected.domain.Player;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class PlayerRepositoryTest {
    private BetterProtectedPlugin plugin;
    private PlayerRepository playerRepository;

    @Before
    public void setUp() throws Exception {
        plugin = new BetterProtectedPlugin();
        plugin.onEnable();
        playerRepository = new PlayerRepository(plugin.getHibernateSession());
    }

    @After
    public void tearDown() throws Exception {
        plugin.onDisable();
    }

    @Test
    public void shouldSaveAndGetPlayer() {
        Player bob = new Player("Bob");

        playerRepository.save(bob);

        assertThat(playerRepository.findById(1L), is(nullValue()));
        assertThat(playerRepository.findById(10L), is(bob));
    }
}
