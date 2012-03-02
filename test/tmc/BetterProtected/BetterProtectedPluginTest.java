package tmc.BetterProtected;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BetterProtectedPluginTest {

    @Test
    public void testGetHibernateSession() throws Exception {
        BetterProtectedPlugin plugin = new BetterProtectedPlugin();

        assertThat(plugin.getHibernateSession(), is(nullValue()));

        plugin.onEnable();
        assertThat(plugin.getHibernateSession(), is(notNullValue()));
        assertThat(plugin.getHibernateSession().isOpen(), is(true));

        plugin.onDisable();
        assertThat(plugin.getHibernateSession().isOpen(), is(false));
    }
}
