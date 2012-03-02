package tmc.BetterProtected.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import tmc.BetterProtected.domain.Player;

public class HibernateUtilsTest {
    @Test
    public void testHibernateConfiguration() {
        SessionFactory sessionFactory = HibernateUtils.configureSessionFactory();

        Player player = new Player();
        player.setId(10L);
        player.setUsername("jason");

        Session session = sessionFactory.openSession();
        try {
            session.save(player);
        } catch (RuntimeException e) {
            System.out.println("**************************");
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
    }
}
